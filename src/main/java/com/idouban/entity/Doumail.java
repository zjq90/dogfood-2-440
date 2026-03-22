package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 豆邮实体类
 * 对应数据库表 doumail
 * 
 * @author iDouban Team
 */
@Data
public class Doumail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 豆邮ID
     */
    private Integer doumailId;

    /**
     * 发送者用户ID
     */
    private Integer fromUserId;

    /**
     * 发送者昵称（非数据库字段）
     */
    private String fromUserNick;

    /**
     * 发送者头像（非数据库字段）
     */
    private String fromUserPortrait;

    /**
     * 接收者用户ID
     */
    private Integer toUserId;

    /**
     * 接收者昵称（非数据库字段）
     */
    private String toUserNick;

    /**
     * 豆邮内容
     */
    private String chatMsg;

    /**
     * 发送时间
     */
    private Timestamp chatTime;

    /**
     * 删除状态：0-双方都未删除；1-发送者删除；2-接收者删除
     */
    private Integer status;

    /**
     * 阅读状态：0-未读；1-已读
     */
    private Integer read;
}
