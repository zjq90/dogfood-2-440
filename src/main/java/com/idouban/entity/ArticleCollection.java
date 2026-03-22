package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 文章收藏实体类
 * 对应数据库表 a_collection
 * 
 * @author iDouban Team
 */
@Data
public class ArticleCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    private Integer collectionId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 收藏的文章信息（非数据库字段）
     */
    private Article article;
}
