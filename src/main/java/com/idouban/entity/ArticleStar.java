package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 点赞实体类
 * 对应数据库表 a_star
 * 
 * @author iDouban Team
 */
@Data
public class ArticleStar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞ID
     */
    private Integer starId;

    /**
     * 被点赞对象ID
     */
    private Integer typeId;

    /**
     * 被点赞对象类型：1-文章；2-评论；3-回复
     */
    private Integer type;

    /**
     * 用户ID
     */
    private Integer userId;
}
