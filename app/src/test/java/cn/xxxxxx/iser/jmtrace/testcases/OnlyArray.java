package cn.xxxxxx.iser.jmtrace.testcases;

public class OnlyArray {
    public static void storeArray() {
        int k = 1;
        var intArr = new int[k];
        for (int i = 0; i < k; i++) {
            intArr[i] = 0;
        }

        var doubleArr = new double[k];
        for (int i = 0; i < k; i++) {
            doubleArr[i] = 0.0;
        }

        var objectArr = new Object[k];
        for (int i = 0; i < k; i++) {
            objectArr[i] = new Object();
        }
    }

    public static void loadArray() {
        int k = 1;
        var intArr = new int[k];
        for (int i = 0; i < k; i++) {
            var res = intArr[i];
        }

        var doubleArr = new double[k];
        for (int i = 0; i < k; i++) {
            var res = doubleArr[i];
        }

        var objectArr = new Object[k];
        for (int i = 0; i < k; i++) {
            var res = objectArr[i];
        }
    }
}
