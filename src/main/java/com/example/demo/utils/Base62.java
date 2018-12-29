package com.example.demo.utils;

public class Base62 {
    static private final int BASE = 62;
    static private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static private final String MAX_INTEGER = to(Integer.MAX_VALUE);

    public static int from(String x) {
        if (MAX_INTEGER.compareTo(x) < 0) {
            String msg = String.format("translation of '%s' will cause integer overflow", x);
            throw new Base62Exception(msg);
        }
        int result = 0;
        for (char ch : x.toCharArray()) {
            result *= BASE;
            int current = ALPHABET.indexOf(ch);
            if (current == -1) {
                String msg = String.format("string '%s' is not a base62 number", x);
                throw new Base62Exception(msg);
            }
            result += current;
        }
        return result;
    }

    public static String to(int x) {
        if (x < 0) {
            String msg = String.format("x must not be negative, have '%d'", x);
            throw new Base62Exception(msg);
        }
        if (x == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder();
        while (x != 0) {
            result.append(ALPHABET.charAt(x % BASE));
            x /= BASE;
        }
        return result.reverse().toString();
    }
}

