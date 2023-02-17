package cn.xxxxxx.iser.jmtrace;

import java.util.Arrays;

public class Utils {
    public static boolean isStdClassName(String name) {
        var stdNames = Arrays.asList("java/", "sun/", "jdk/");
        return stdNames.stream().anyMatch(name::startsWith);
    }
}
