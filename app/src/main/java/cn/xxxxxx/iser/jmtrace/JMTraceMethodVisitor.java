package cn.xxxxxx.iser.jmtrace;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class JMTraceMethodVisitor extends MethodVisitor {
    private static final String tracerName = Type.getInternalName(Tracer.class);

    public JMTraceMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (Utils.isStdClassName(name)) {
            return;
        }

        var type = concatObjectType(owner, name);
        run(() -> {
            if (opcode == Opcodes.PUTSTATIC) {
                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "tracePutStatic", "(Ljava/lang/String;)V", false);
                return;
            }

            if (opcode == Opcodes.GETSTATIC) {
                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "traceGetStatic", "(Ljava/lang/String;)V", false);
                return;
            }

            if (opcode == Opcodes.GETFIELD) {
                super.visitInsn(Opcodes.DUP);
                super.visitLdcInsn(name);
                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "traceGetField", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", false);
                return;
            }

            if (opcode == Opcodes.PUTFIELD) {
                // category 2 type
                if (isCategory2Descriptor(descriptor)) {
                    // @see https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-6.html#jvms-6.5.dup2_x1
                    // <obj_c1> <value_c2> -> <value_c2> <obj_c1> <value2_c2>
                    super.visitInsn(Opcodes.DUP2_X1);
                    // -> <value_c2> <obj_c1>
                    super.visitInsn(Opcodes.POP2);
                    // -> <obj_c1> <value2_c2> <obj_c1>
                    super.visitInsn(Opcodes.DUP_X2);
                } else {
                    // category 1 type
                    super.visitInsn(Opcodes.DUP2);
                    super.visitInsn(Opcodes.POP);
                }

                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "tracePutField", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
            }
        });

        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitInsn(int opcode) {
        run(() -> {
            // array load
            if (opcode >= Opcodes.IALOAD && opcode <= Opcodes.SALOAD) {
                var type = opcodeToArrayType(opcode);
                // <arr> <index>
                super.visitInsn(Opcodes.DUP2);
                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "traceArrayLoad", "(Ljava/lang/Object;ILjava/lang/String;)V", false);
                return;
            }

            // array store
            if (opcode >= Opcodes.IASTORE && opcode <= Opcodes.SASTORE) {
                var type = opcodeToArrayType(opcode);
                // <arr_c1> <index_c1> <value_c(1|2)>
                if (isCategory2Opcode(opcode)) {
                    // <value_c2> <arr_c1> <index_c1> <value_c2>
                    super.visitInsn(Opcodes.DUP2_X2);
                    super.visitInsn(Opcodes.POP2);
                    // <arr_c1> <index_c1> <value_c2> <arr_c1> <index_c1>
                    super.visitInsn(Opcodes.DUP2_X2);
                } else {
                    // <value_c1> <arr_c1> <index_c1> <value_c1>
                    super.visitInsn(Opcodes.DUP_X2);
                    super.visitInsn(Opcodes.POP);
                    // <arr_c1> <index_c1> <value_c1> <arr_c1> <index_c1>
                    super.visitInsn(Opcodes.DUP2_X1);
                }

                super.visitLdcInsn(type);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, tracerName, "traceArrayStore", "(Ljava/lang/Object;ILjava/lang/String;)V", false);
                return;
            }
        });

        super.visitInsn(opcode);
    }

    private void run(Runnable fn) {
        fn.run();
    }

    private String concatObjectType(String owner, String field) {
        return owner.replace("/", ".") + "." + field;
    }

    private boolean isCategory2Descriptor(String descriptor) {
        return descriptor.equals("D") || descriptor.equals("J");
    }

    private boolean isCategory2Opcode(int opcode) {
        return opcode == Opcodes.DASTORE || opcode == Opcodes.LASTORE;
    }

    private String opcodeToArrayType(int opcode) {
        switch (opcode) {
            case Opcodes.IALOAD:
            case Opcodes.IASTORE:
                return "int";
            case Opcodes.LALOAD:
            case Opcodes.LASTORE:
                return "long";
            case Opcodes.FALOAD:
            case Opcodes.FASTORE:
                return "float";
            case Opcodes.DALOAD:
            case Opcodes.DASTORE:
                return "double";
            case Opcodes.AALOAD:
            case Opcodes.AASTORE:
                return "java.lang.Object";
            case Opcodes.BALOAD:
            case Opcodes.BASTORE:
                return "byte";
            case Opcodes.CALOAD:
            case Opcodes.CASTORE:
                return "char";
            case Opcodes.SALOAD:
            case Opcodes.SASTORE:
                return "short";
        }
        throw new IllegalArgumentException(String.format("unknown opcode: %s", opcode));
    }
}
