package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评论回复实体类 - 对应数据库 a_reply 表
 * @author 林泽鸿
 */
@Data
public class ArticleReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 回复ID */
    private Integer replyId;

    /** 被回复的评论ID */
    private Integer commentId;

    /** 发出回复的用户ID */
    private Integer fromUserId;

    /** 接收回复的用户ID */
    private Integer toUserId;

    /** 回复内容 */
    private String replyMsg;

    /** 回复时间 */
    private Timestamp replyTime;

    /** 回复点赞数 */
    private Integer replyStar = 0;

    // ========== 扩展字段（用于展示） ==========

    /** 发出回复的用户昵称 */
    private String fromUserNick;

    /** 接收回复的用户昵称 */
    private String toUserNick;

}
