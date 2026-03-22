package com.idouban.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证工具类
 * 
 * @author iDouban Team
 */
public class ValidateUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidateUtil.class);

    /**
     * 验证用户名是否有效（非空）
     * 
     * @param username 用户名
     * @return true-无效；false-有效
     */
    public static boolean isInvalidUserName(String username) {
        return username == null || username.trim().isEmpty();
    }

    /**
     * 验证密码是否有效（非空）
     * 
     * @param password 密码
     * @return true-无效；false-有效
     */
    public static boolean isInvalidPassword(String password) {
        return password == null || password.trim().isEmpty();
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @return true-格式正确；false-格式错误
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    /**
     * 验证字符串长度
     * 
     * @param str 字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return true-符合；false-不符合
     */
    public static boolean checkLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
}
