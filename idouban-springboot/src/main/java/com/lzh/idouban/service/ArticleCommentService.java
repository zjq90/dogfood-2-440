package com.lzh.idouban.service;

import com.lzh.idouban.entity.ArticleComment;

import java.util.List;

/**
 * 文章评论服务接口
 * @author 林泽鸿
 */
public interface ArticleCommentService {

    /**
     * 根据ID查询评论
     * @param commentId 评论ID
     * @return 评论对象
     */
    ArticleComment getCommentById(Integer commentId);

    /**
     * 查询文章的所有评论
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<ArticleComment> getCommentsByArticleId(Integer articleId);

    /**
     * 发表评论
     * @param comment 评论对象
     * @return 是否成功
     */
    boolean addComment(ArticleComment comment);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean deleteComment(Integer commentId, Integer userId);

    /**
     * 点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean starComment(Integer commentId, Integer userId);

    /**
     * 取消点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unstarComment(Integer commentId, Integer userId);

}
