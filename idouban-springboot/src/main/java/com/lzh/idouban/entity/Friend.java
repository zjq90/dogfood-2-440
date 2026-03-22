package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 好友关系实体类 - 对应数据库 friend 表
 * @author 林泽鸿
 */
@Data
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 好友关系ID */
    private Integer friendId;

    /** 发起关注用户ID */
    private Integer fromUserId;

    /** 被关注用户ID */
    private Integer toUserId;

    /**
     * 关系状态：
     * 1 - A关注B（单向）
     * 2 - AB互相关注（好友）
     * 3 - B被A拉黑
     */
    private Integer status = 1;

    /** 分组ID */
    private Integer groupId;

    // ========== 扩展字段（用于展示） ==========

    /** 好友昵称 */
    private String friendNick;

    /** 好友头像 */
    private String friendPortrait;

    /** 好友签名 */
    private String friendSignature;

    /**
     * 判断是否为双向好友
     */
    public boolean isMutual() {
        return status != null && status == 2;
    }

    /**
     * 判断是否被拉黑
     */
    public boolean isBlacklisted() {
        return status != null && status == 3;
    }

}
