package cn.xxxxxx.iser.jmtrace.testcases;

public class Compose {
    public static Inner1 inner1;

    public static void reset() {
        inner1 = new Inner1();
    }

    public static void run() {
        var a1 = inner1.inner2Field;
        var a2 = a1.intField;
        var a3 = Inner1.Inner2.doubleField;

        a1.objectsField[0] = new Inner1();
        a1.objectsField[0].inner2Field.intField = 2;
    }

    public static class Inner1 {
        public Inner2 inner2Field = new Inner2();

        public static class Inner2 {
            public int intField = 0;
            public static double doubleField = 1.0;
            public Inner1[] objectsField = new Inner1[4];
        }
    }
}
