package com.xiaojia.xiaojiaaddons.utils;

import java.util.HashMap;

public class StringUtils {
    private static final HashMap<String, Integer> romanToInteger = new HashMap<String, Integer>() {{
        put("I", 1);
        put("II", 2);
        put("III", 3);
        put("IV", 4);
        put("V", 5);
        put("VI", 6);
        put("VII", 7);
        put("VIII", 8);
        put("IX", 9);
        put("X", 10);
    }};

    public static int getNumberFromRoman(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return romanToInteger.get(s);
        }
    }
}
