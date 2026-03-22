package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文章实体类
 * 对应数据库表 a_article
 * 
 * @author iDouban Team
 */
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章作者ID
     */
    private Integer author;

    /**
     * 作者昵称（非数据库字段，用于关联查询）
     */
    private String authorNick;

    /**
     * 作者头像（非数据库字段，用于关联查询）
     */
    private String authorImg;

    /**
     * 发表时间
     */
    private Timestamp publishedTime;

    /**
     * 文章内容（HTML格式）
     */
    private String content;

    /**
     * 收藏数
     */
    private Integer collection;

    /**
     * 转发数
     */
    private Integer share;

    /**
     * 评论数
     */
    private Integer comment;

    /**
     * 点赞数
     */
    private Integer star;

    /**
     * 是否置顶（0-不置顶，1-置顶）
     */
    private Integer stick;

    /**
     * 浏览量
     */
    private Integer pageView;

    /**
     * 分类ID（非数据库字段）
     */
    private Integer tagId;

    /**
     * 分类名称（非数据库字段）
     */
    private String tagName;

    /**
     * 无参构造方法
     */
    public Article() {
    }
}
