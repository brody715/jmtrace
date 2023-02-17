package cn.xxxxxx.iser.jmtrace.testcases;

public class OnlyStatic {
    public static int intField = 1;
    public static double doubleField = 2.0;
    public static Inner objectField = new Inner();

    public static void dumb() {}

    public static void staticPut() {
        OnlyStatic.intField = 2;
        OnlyStatic.doubleField = 3.0;
        OnlyStatic.objectField = new Inner();
    }

    public static void staticGet() {
        var a3 = OnlyStatic.objectField;
        var a2 = OnlyStatic.doubleField;
        var a1 = OnlyStatic.intField;
    }

    public static class Inner {
    }
}
