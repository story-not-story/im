package com.example.im.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author HuJun
 * @date 2020/5/11 9:48 下午
 */
public class PinYinUtil {
    public static char getFirstChar(String str) {
        if (!StringUtil.isNullOrEmpty(str)) {
            char first = str.charAt(0);
            if (CharUtil.isLetter(first)) {
                return CharUtil.toUpper(first);
            } else if (CharUtil.isChinese(first)) {
                HanyuPinyinOutputFormat hypy = new HanyuPinyinOutputFormat();
                hypy.setCaseType(HanyuPinyinCaseType.UPPERCASE);
                try {
                    return PinyinHelper.toHanyuPinyinStringArray(first, hypy)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        return '#';
    }
}
