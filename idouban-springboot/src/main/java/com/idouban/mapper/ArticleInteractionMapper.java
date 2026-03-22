package com.idouban.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章互动Mapper接口
 * 处理点赞、收藏、转发等互动操作
 */
@Mapper
public interface ArticleInteractionMapper {

    /**
     * 检查用户是否点赞文章
     */
    int checkArticleStar(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 添加文章点赞记录
     */
    int insertArticleStar(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 删除文章点赞记录
     */
    int deleteArticleStar(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 检查用户是否收藏文章
     */
    int checkArticleCollection(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 添加文章收藏记录
     */
    int insertArticleCollection(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 删除文章收藏记录
     */
    int deleteArticleCollection(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 检查用户是否转发文章
     */
    int checkArticleShare(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    /**
     * 添加文章转发记录
     */
    int insertArticleShare(@Param("userId") Integer userId, @Param("articleId") Integer articleId);
}
