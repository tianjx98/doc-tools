package com.example.application.utils;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/4/29 9:54
 */
public class StringUtil {

    public static String firstCharToLowerCase(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String firstCharToUpperCase(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String toLowerCamelCase(String name) {
        return StrUtil.toCamelCase(name);
    }

    public static String toUpperCamelCase(String name) {
        return firstCharToUpperCase(toLowerCamelCase(name));
    }
}
