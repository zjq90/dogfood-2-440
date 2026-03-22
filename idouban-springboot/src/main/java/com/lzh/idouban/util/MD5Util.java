package com.lzh.idouban.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author 林泽鸿
 */
public class MD5Util {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * MD5加密
     * @param input 输入字符串
     * @return 32位大写MD5字符串
     */
    public static String encrypt(String input) {
        return encrypt(input, "UTF-8");
    }

    /**
     * MD5加密（指定字符集）
     * @param input 输入字符串
     * @param charset 字符集
     * @return 32位大写MD5字符串
     */
    public static String encrypt(String input, String charset) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes(charset));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * 字节数组转16进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            sb.append(HEX_DIGITS[b & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 验证密码
     * @param input 输入密码
     * @param encrypted 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String input, String encrypted) {
        if (input == null || encrypted == null) {
            return false;
        }
        return encrypt(input).equalsIgnoreCase(encrypted);
    }

}
