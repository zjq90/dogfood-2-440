package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评论回复实体类
 * 对应数据库表 a_reply
 * 
 * @author iDouban Team
 */
@Data
public class ArticleReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复ID
     */
    private Integer replyId;

    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 发送回复用户ID
     */
    private Integer fromUserId;

    /**
     * 发送回复用户昵称（非数据库字段）
     */
    private String fromUserNick;

    /**
     * 发送回复用户头像（非数据库字段）
     */
    private String fromUserPortrait;

    /**
     * 接收回复用户ID
     */
    private Integer toUserId;

    /**
     * 接收回复用户昵称（非数据库字段）
     */
    private String toUserNick;

    /**
     * 回复内容
     */
    private String replyMsg;

    /**
     * 回复时间
     */
    private Timestamp replyTime;

    /**
     * 回复点赞数
     */
    private Integer rStar;

    /**
     * 当前用户是否已点赞（非数据库字段）
     */
    private Boolean isStar;
}
