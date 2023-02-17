package cn.xxxxxx.iser.jmtrace;

import java.util.Objects;

public class Tracer {
    private static boolean debug = true;
    private static StringBuffer stringBuffer = new StringBuffer();

    public static void setDebug(boolean debug) {
        Tracer.debug = debug;
    }

    public static synchronized String consumeOutput() {
        var output = stringBuffer.toString();
        stringBuffer = new StringBuffer();
        return output;
    }

    private static void trace(String op, long objectId, String objectType) {
        trace(op, objectId, objectType, -1);
    }

    private static synchronized void trace(String op, long objectId, String objectType, int index) {
        long threadId = Thread.currentThread().getId();

        var format = "%s\t %d\t %016x\t %s\n";
        if (index != -1) {
            format = "%s\t %d\t %016x\t %s[%d]\n";
        }

        var str = String.format(format, op, threadId, objectId, objectType, index);

        stringBuffer.append(str);
        if (debug) {
            System.out.print(str);
        }
    }

    public static void traceGetStatic(String type) {
        trace("R", System.identityHashCode(type), type);
    }

    public static void tracePutStatic(String type) {
        trace("W", System.identityHashCode(type), type);
    }

    public static void traceGetField(Object obj, String field, String type) {
        trace("R", Objects.hash(obj, field), type);
    }

    public static void tracePutField(Object obj, String type) {
        trace("W", System.identityHashCode(obj), type);
    }

    public static void traceArrayLoad(Object arr, int index, String type) {
        trace("R", System.identityHashCode(arr) + index, type, index);
    }

    public static void traceArrayStore(Object arr, int index, String type) {
        trace("W", System.identityHashCode(arr) + index, type, index);
    }
}
