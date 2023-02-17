package cn.xxxxxx.iser.jmtrace;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JMTraceClassVisitor extends ClassVisitor {
    public JMTraceClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        var baseVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        return new JMTraceMethodVisitor(this.api, baseVisitor);
    }
}
