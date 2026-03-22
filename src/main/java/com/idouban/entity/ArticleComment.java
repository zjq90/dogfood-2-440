package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文章评论实体类
 * 对应数据库表 a_comment
 * 
 * @author iDouban Team
 */
@Data
public class ArticleComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户昵称（非数据库字段）
     */
    private String userNick;

    /**
     * 用户头像（非数据库字段）
     */
    private String userPortrait;

    /**
     * 评论内容
     */
    private String cMsg;

    /**
     * 点赞数
     */
    private Integer cStar;

    /**
     * 评论时间
     */
    private Timestamp cTime;

    /**
     * 当前用户是否已点赞（非数据库字段）
     */
    private Boolean isStar;
}
