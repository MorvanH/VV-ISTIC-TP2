package Test;

import fr.istic.vv.CFGNode;

public class Test {
    private int a = 0;

    public int getA() {
        return a;
    }

    public static int max(int a, int b, int c) {
        if (a > b) {
            if(a > c) {
                return a;
            }
            else {
                return c;
            }
        }
        else {
            if (b > c) {
                return b;
            }
            else {
                return c;
            }
        }
    }

    public static int CFGWhileTest(int a) {
        int b = 10;
        while(a > 0) {
            a--;
            if(a % b == 0) {
                return a;
            }
        }

        return a;
    }

    public static int CFGIfTest(int a) {
        int b = 10;
        if(a > b) {
            return a;
        }
        return b;
    }

    public static int CFGIfElseTest(int a) {
        int b = 10;
        if(a > b) {
            return a;
        }
        else {
            return b;
        }
    }

    public static int CFGDoWhileTest(int a) {
        int b = 10;
        do {
            a--;
            if(a % b == 0) {
                return a;
            }

            int c = 10;
            while (c > 0) {
                c--;
            }
        } while(a > 0);

        return a;
    }

    public static int CFGForTest(int a) {
        int b = 10;
        for(int i = 0; i < a; i++) {
            if(i - b == 0) {
                return i;
            }
            b--;
        }

        return a;
    }
}
