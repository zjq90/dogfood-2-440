package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类 - 对应数据库 user 表
 * @author 林泽鸿
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Integer userId;

    /** 用户名（通常为邮箱） */
    private String username;

    /** 密码（MD5加密） */
    private String password;

    /** 用户权限：0-普通用户，1-管理员 */
    private Integer status = 0;

    /** 是否被举报：0-未举报，1-被举报 */
    private Integer reported = 0;

    /** 封号截止时间 */
    private Timestamp titleTime;

    /** 用户头像URL */
    private String portrait;

    /** 个性签名 */
    private String signature;

    /** 自我介绍 */
    private String selfIntroduction;

    /** 用户昵称 */
    private String nickname;

    /** 用户地址 */
    private String address;

    /** 用户注册时间 */
    private Timestamp time;

    /** 找回密码的UUID */
    private Integer code;

    /** 找回密码链接失效时间 */
    private Timestamp outTime;

    /**
     * 判断是否为管理员
     */
    public boolean isAdmin() {
        return status != null && status == 1;
    }

    /**
     * 判断账号是否被封禁
     */
    public boolean isBanned() {
        if (titleTime == null) {
            return false;
        }
        return titleTime.after(new Timestamp(System.currentTimeMillis()));
    }

}
