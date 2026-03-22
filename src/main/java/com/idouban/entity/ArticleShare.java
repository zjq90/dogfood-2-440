package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 文章转发实体类
 * 对应数据库表 a_share
 * 
 * @author iDouban Team
 */
@Data
public class ArticleShare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 转发ID
     */
    private Integer shareId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 文章ID
     */
    private Integer articleId;
}
