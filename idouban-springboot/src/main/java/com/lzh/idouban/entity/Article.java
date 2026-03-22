package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 文章实体类 - 对应数据库 a_article 表
 * @author 林泽鸿
 */
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Integer articleId;

    /** 文章标题 */
    private String title;

    /** 文章作者ID */
    private Integer authorId;

    /** 发表时间 */
    private Timestamp publishedTime;

    /** 文章内容（HTML） */
    private String content;

    /** 收藏数 */
    private Integer collection = 0;

    /** 转发数 */
    private Integer share = 0;

    /** 评论数 */
    private Integer comment = 0;

    /** 点赞数 */
    private Integer star = 0;

    /** 是否置顶：0-否，1-是 */
    private Integer stick = 0;

    /** 浏览量 */
    private Integer pageView = 0;

    // ========== 扩展字段（用于展示） ==========

    /** 作者昵称 */
    private String authorNick;

    /** 作者头像 */
    private String authorImg;

    /** 分类名称 */
    private String tagName;

    /** 分类ID */
    private Integer tagId;

    /**
     * 判断是否置顶
     */
    public boolean isStick() {
        return stick != null && stick == 1;
    }

}
