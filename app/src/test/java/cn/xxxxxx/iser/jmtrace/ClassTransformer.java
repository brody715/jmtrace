package cn.xxxxxx.iser.jmtrace;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

// fork from https://github.com/codebennet/java-asm-test/blob/master/src/main/java/com/acestime/proxy/service/AddFieldTransformation.java

public class ClassTransformer {
    private final BytesClassLoader classLoader = new BytesClassLoader();

    public <T> Class<T> transform(Class<T> clazz) throws Exception {
        String className = clazz.getName();
        String classAsPath = className.replace('.', '/') + ".class";

        ClassReader cr = new ClassReader(clazz.getClassLoader().getResourceAsStream(classAsPath));
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        cr.accept(new JMTraceClassVisitor(cw), ClassReader.EXPAND_FRAMES);
        return classLoader.defineClass(clazz.getName(), cw.toByteArray());
    }

    static class BytesClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
