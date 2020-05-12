package com.example.im.util;

/**
 * @author HuJun
 * @date 2020/4/10 2:33 下午
 */
public class CharUtil {
    public static boolean isLower(char ch) {
        return ((ch-'a')|('z'-ch)) >= 0;
    }

    public static boolean isUpper(char ch) {
        return ((ch-'A')|('Z'-ch)) >= 0;
    }

    public static boolean isLetter(char ch) {
        return isUpper(ch) || isLower(ch);
    }

    public static char toLower(char ch) {return isUpper(ch) ? (char)(ch + 0x20) : ch;}

    public static char toUpper(char ch) {return isLower(ch) ? (char)(ch - 0x20) : ch;}

    public static boolean isChinese(char ch) {
        return (ch >= 0x4e00) && (ch <= 0x9fa5);
    }
}
