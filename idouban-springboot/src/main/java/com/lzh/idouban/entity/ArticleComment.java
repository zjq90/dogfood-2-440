package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文章评论实体类 - 对应数据库 a_comment 表
 * @author 林泽鸿
 */
@Data
public class ArticleComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Integer commentId;

    /** 被评论的文章ID */
    private Integer articleId;

    /** 评论用户ID */
    private Integer userId;

    /** 评论内容 */
    private String commentMsg;

    /** 点赞数 */
    private Integer commentStar = 0;

    /** 评论时间 */
    private Timestamp commentTime;

    // ========== 扩展字段（用于展示） ==========

    /** 评论用户昵称 */
    private String userNick;

    /** 评论用户头像 */
    private String userPortrait;

}
