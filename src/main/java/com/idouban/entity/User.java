package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实体类
 * 对应数据库表 user
 * 
 * @author iDouban Team
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名（通常为邮箱）
     */
    private String username;

    /**
     * 密码（MD5加密）
     */
    private String password;

    /**
     * 用户权限（0为普通用户，1为管理员）
     */
    private Integer status;

    /**
     * 用户是否被举报（0-未被举报，1-被举报）
     */
    private Integer reported;

    /**
     * 封号截止时间
     */
    private Timestamp titleTime;

    /**
     * 用户头像（存储路径）
     */
    private String portrait;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 自我介绍
     */
    private String selfIntroduction;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 用户注册时间
     */
    private Timestamp time;

    /**
     * 找回密码凭证
     */
    private Integer code;

    /**
     * 找回密码过期时间
     */
    private Timestamp outTime;

    /**
     * 无参构造方法
     */
    public User() {
    }

    /**
     * 登录注册用构造方法
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
