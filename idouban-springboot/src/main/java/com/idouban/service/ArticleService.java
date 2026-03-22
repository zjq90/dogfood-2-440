package com.idouban.service;

import com.idouban.common.BusinessException;
import com.idouban.mapper.ArticleMapper;
import com.idouban.mapper.UserMapper;
import com.idouban.model.Article;
import com.idouban.model.ArticleList;
import com.idouban.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章服务类
 */
@Slf4j
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 根据文章ID查询文章详情
     */
    public Article getArticleById(Integer articleId) {
        log.debug("查询文章详情, articleId: {}", articleId);
        Article article = articleMapper.selectByArticleId(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        
        articleMapper.updatePageView(articleId);
        
        if (article.getAuthorId() != null) {
            User author = userMapper.selectByUserId(article.getAuthorId());
            if (author != null) {
                article.setAuthorNick(author.getNickname());
                article.setAuthorImg(author.getPortrait());
            }
        }
        return article;
    }

    /**
     * 发布文章
     */
    @Transactional(rollbackFor = Exception.class)
    public Article publishArticle(Article article) {
        log.info("发布文章, authorId: {}, title: {}", article.getAuthorId(), article.getTitle());
        articleMapper.insert(article);
        log.info("文章发布成功, articleId: {}", article.getArticleId());
        return article;
    }

    /**
     * 更新文章
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(Article article) {
        log.info("更新文章, articleId: {}", article.getArticleId());
        int result = articleMapper.update(article);
        return result > 0;
    }

    /**
     * 删除文章
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(Integer articleId) {
        log.info("删除文章, articleId: {}", articleId);
        int result = articleMapper.delete(articleId);
        return result > 0;
    }

    /**
     * 分页查询文章列表
     */
    public Page<ArticleList> getArticleList(int currentPage, int pageSize) {
        log.debug("分页查询文章列表, currentPage: {}, pageSize: {}", currentPage, pageSize);
        int offset = (currentPage - 1) * pageSize;
        List<ArticleList> articles = articleMapper.selectArticleListByPage(offset, pageSize);
        int totalCount = articleMapper.selectArticleCount();
        
        articles = fillAuthorInfo(articles);
        
        Page<ArticleList> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(articles);
        return page;
    }

    /**
     * 查询用户的文章列表
     */
    public Page<ArticleList> getArticleListByAuthor(Integer authorId, int currentPage, int pageSize) {
        log.debug("查询用户文章列表, authorId: {}, currentPage: {}", authorId, currentPage);
        int offset = (currentPage - 1) * pageSize;
        List<ArticleList> articles = articleMapper.selectByAuthorId(authorId, offset, pageSize);
        int totalCount = articleMapper.selectCountByAuthorId(authorId);
        
        articles = fillAuthorInfo(articles);
        
        Page<ArticleList> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(articles);
        return page;
    }

    /**
     * 搜索文章
     */
    public Page<ArticleList> searchArticles(String searchContent, int currentPage, int pageSize) {
        log.debug("搜索文章, searchContent: {}, currentPage: {}", searchContent, currentPage);
        int offset = (currentPage - 1) * pageSize;
        List<ArticleList> articles = articleMapper.selectSearchByPage(searchContent, offset, pageSize);
        int totalCount = articleMapper.selectSearchCount(searchContent);
        
        articles = fillAuthorInfo(articles);
        
        Page<ArticleList> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(articles);
        return page;
    }

    /**
     * 查询用户收藏的文章列表
     */
    public Page<ArticleList> getCollectionArticles(Integer userId, int currentPage, int pageSize) {
        log.debug("查询用户收藏文章, userId: {}, currentPage: {}", userId, currentPage);
        int offset = (currentPage - 1) * pageSize;
        List<ArticleList> articles = articleMapper.selectCollectionByPage(userId, offset, pageSize);
        int totalCount = articleMapper.selectCollectionCount(userId);
        
        articles = fillAuthorInfo(articles);
        
        Page<ArticleList> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(articles);
        return page;
    }

    /**
     * 填充文章作者信息
     */
    private List<ArticleList> fillAuthorInfo(List<ArticleList> articles) {
        List<ArticleList> result = new ArrayList<>();
        for (ArticleList article : articles) {
            if (article.getAuthorId() != null) {
                User author = userMapper.selectByUserId(article.getAuthorId());
                if (author != null) {
                    article.setAuthorNick(author.getNickname());
                    article.setAuthorImg(author.getPortrait());
                }
            }
            result.add(article);
        }
        return result;
    }
}
