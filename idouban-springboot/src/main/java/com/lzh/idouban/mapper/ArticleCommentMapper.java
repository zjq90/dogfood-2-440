package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.ArticleComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章评论数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface ArticleCommentMapper {

    /**
     * 根据ID查询评论
     * @param commentId 评论ID
     * @return 评论对象
     */
    ArticleComment selectById(@Param("commentId") Integer commentId);

    /**
     * 查询文章的所有评论（带用户信息）
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<ArticleComment> selectByArticleId(@Param("articleId") Integer articleId);

    /**
     * 插入新评论
     * @param comment 评论对象
     * @return 影响行数
     */
    int insert(ArticleComment comment);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 影响行数
     */
    int deleteById(@Param("commentId") Integer commentId);

    /**
     * 增加点赞数
     * @param commentId 评论ID
     * @return 影响行数
     */
    int incrementStar(@Param("commentId") Integer commentId);

    /**
     * 减少点赞数
     * @param commentId 评论ID
     * @return 影响行数
     */
    int decrementStar(@Param("commentId") Integer commentId);

    /**
     * 查询评论数
     * @param articleId 文章ID
     * @return 评论数
     */
    int countByArticleId(@Param("articleId") Integer articleId);

}
