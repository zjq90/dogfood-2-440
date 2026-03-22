package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 文章标签实体类
 * 对应数据库表 a_tag
 * 
 * @author iDouban Team
 */
@Data
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 该标签下的文章数量（非数据库字段）
     */
    private Integer articleCount;
}
