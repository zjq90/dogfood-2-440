package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 文章-标签中间表实体类
 * 对应数据库表 article_to_tag
 * 
 * @author iDouban Team
 */
@Data
public class ArticleToTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 中间表ID
     */
    private Integer middleId;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 标签ID
     */
    private Integer tagId;
}
