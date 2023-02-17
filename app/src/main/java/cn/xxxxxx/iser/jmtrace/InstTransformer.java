package cn.xxxxxx.iser.jmtrace;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class InstTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);

        if (Utils.isStdClassName(className)) {
            return classfileBuffer;
        }

        var reader = new ClassReader(classfileBuffer);
        var writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        reader.accept(new JMTraceClassVisitor(writer), ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }
}
