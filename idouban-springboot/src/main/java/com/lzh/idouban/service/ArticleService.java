package com.lzh.idouban.service;

import com.lzh.idouban.entity.Article;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 文章服务接口
 * @author 林泽鸿
 */
public interface ArticleService {

    /**
     * 根据ID查询文章
     * @param articleId 文章ID
     * @return 文章对象
     */
    Article getArticleById(Integer articleId);

    /**
     * 分页查询文章列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<Article> getArticleList(int pageNum, int pageSize);

    /**
     * 查询用户的文章列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<Article> getArticlesByUserId(Integer userId, int pageNum, int pageSize);

    /**
     * 发布文章
     * @param article 文章对象
     * @return 是否成功
     */
    boolean publishArticle(Article article);

    /**
     * 更新文章
     * @param article 文章对象
     * @return 是否成功
     */
    boolean updateArticle(Article article);

    /**
     * 删除文章
     * @param articleId 文章ID
     * @param userId 操作用户ID（用于权限验证）
     * @return 是否成功
     */
    boolean deleteArticle(Integer articleId, Integer userId);

    /**
     * 增加浏览量
     * @param articleId 文章ID
     */
    void incrementPageView(Integer articleId);

    /**
     * 点赞文章
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean starArticle(Integer articleId, Integer userId);

    /**
     * 取消点赞
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unstarArticle(Integer articleId, Integer userId);

    /**
     * 收藏文章
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean collectArticle(Integer articleId, Integer userId);

    /**
     * 取消收藏
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean uncollectArticle(Integer articleId, Integer userId);

    /**
     * 转发文章
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean shareArticle(Integer articleId, Integer userId);

    /**
     * 搜索文章
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<Article> searchArticles(String keyword, int pageNum, int pageSize);

}
