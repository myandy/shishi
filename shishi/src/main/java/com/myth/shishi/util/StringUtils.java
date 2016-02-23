package com.myth.shishi.util;

import android.text.TextUtils;

public class StringUtils {

    public static boolean isEmpty(String s) {

        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s.replaceAll("\n", "").replaceAll(" ", ""))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 长句子逗号后智能换行，数据库处理麻烦，暂时如此
     *
     * @param p
     */
    public static String autoLineFeed(String p) {
        String[] s = p.split("\r\n");
        for (int i = 0; i < s.length; i++) {
            if (s[i].replaceAll("[\\[\\]0-9]", "").length() > 13 && s[i].indexOf("，") >= 3) {
                s[i] = s[i].replaceFirst("，", "，\r\n");
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s1 : s) {
            sb.append(s1);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
