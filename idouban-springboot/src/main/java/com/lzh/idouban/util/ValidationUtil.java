package com.lzh.idouban.util;

import java.util.regex.Pattern;

/**
 * 参数校验工具类
 * @author 林泽鸿
 */
public class ValidationUtil {

    /**
     * 用户名正则：邮箱格式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * 用户名正则：允许字母、数字、下划线，长度3-20
     */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    /**
     * 校验用户名是否有效
     * @param username 用户名
     * @return true-有效
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.length() >= 3 && username.length() <= 50;
    }

    /**
     * 校验密码是否有效
     * @param password 密码
     * @return true-有效
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 1 && password.length() <= 20;
    }

    /**
     * 校验邮箱格式
     * @param email 邮箱
     * @return true-有效
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 校验搜索关键词
     * @param keyword 关键词
     * @return true-有效
     */
    public static boolean isValidKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        return keyword.length() <= 100;
    }

    /**
     * 校验文章标题
     * @param title 标题
     * @return true-有效
     */
    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return title.length() <= 200;
    }

    /**
     * 校验文章内容
     * @param content 内容
     * @return true-有效
     */
    public static boolean isValidContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        return content.length() <= 50000;
    }

    /**
     * 校验评论内容
     * @param content 内容
     * @return true-有效
     */
    public static boolean isValidComment(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        return content.length() <= 800;
    }

    /**
     * 校验豆邮内容
     * @param content 内容
     * @return true-有效
     */
    public static boolean isValidDoumail(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        return content.length() <= 800;
    }

}
