package cn.xxxxxx.iser.jmtrace.testcases;

public class OnlyField {
    public int intField = 1;
    public double doubleField = 2.0;
    public long longField = 2l;
    public Inner objectField = new Inner();

    // object
    private static OnlyField obj;

    public static void reset() {
        obj = new OnlyField();
    }

    public static void fieldPut() {
        double doubleValue = 3.0;
        obj.intField = 2;
        obj.doubleField = doubleValue;
        obj.longField = 5l;
        obj.objectField = new Inner();
    }

    public static void fieldGet() {
        var a1 = obj.objectField;
        var a2 = obj.doubleField;
        var a3 = obj.longField;
        var a4 = obj.intField;
    }

    public static class Inner {}
}
