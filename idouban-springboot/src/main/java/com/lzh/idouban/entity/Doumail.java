package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 豆邮（私信）实体类 - 对应数据库 doumail 表
 * @author 林泽鸿
 */
@Data
public class Doumail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 豆邮ID */
    private Integer doumailId;

    /** 发送者用户ID */
    private Integer fromUserId;

    /** 接收者用户ID */
    private Integer toUserId;

    /** 豆邮内容 */
    private String chatMsg;

    /** 发送时间 */
    private Timestamp chatTime;

    /**
     * 删除状态：
     * 0 - 双方都未删除
     * 1 - 发送者删除
     * 2 - 接收者删除
     */
    private Integer status = 0;

    /**
     * 阅读状态：
     * 0 - 未读
     * 1 - 已读
     */
    private Integer read = 0;

    // ========== 扩展字段（用于展示） ==========

    /** 发送者昵称 */
    private String fromUserNick;

    /** 接收者昵称 */
    private String toUserNick;

    /** 发送者头像 */
    private String fromUserPortrait;

    /**
     * 判断是否已读
     */
    public boolean isRead() {
        return read != null && read == 1;
    }

}
