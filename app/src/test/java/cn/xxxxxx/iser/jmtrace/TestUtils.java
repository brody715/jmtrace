package cn.xxxxxx.iser.jmtrace;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    private static ClassTransformer transformer = new ClassTransformer();

    public static <T> Class<T> transform(Class<T> clazz) throws Exception {
        return transformer.transform(clazz);
    }

    public static void invokeStaticVoid(Class clazz, String method) throws Exception {
        Method staticMethod = clazz.getDeclaredMethod(method);
        staticMethod.invoke(null);
    }

    public static void assertOutputMatch(List<String> expected, String output) {
        Assertions.assertLinesMatch(expected, Arrays.asList(output.split("\n")));
    }
}