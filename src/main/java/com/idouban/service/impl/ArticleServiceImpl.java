package com.idouban.service.impl;

import com.idouban.dto.PageResult;
import com.idouban.dto.Result;
import com.idouban.entity.Article;
import com.idouban.mapper.ArticleMapper;
import com.idouban.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 文章服务实现类
 * 
 * @author iDouban Team
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取文章详情
     */
    @Override
    public Result<Article> getArticleDetail(Integer articleId, Integer userId) {
        logger.info("获取文章详情，文章ID：{}", articleId);
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.fail("文章不存在");
        }
        return Result.success(article);
    }

    /**
     * 分页获取文章列表
     */
    @Override
    public Result<PageResult<Article>> getArticleList(Integer pageNum, Integer pageSize, Integer tagId, String keyword) {
        logger.info("获取文章列表，页码：{}，每页条数：{}，标签ID：{}，关键词：{}", pageNum, pageSize, tagId, keyword);

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Article> articles = articleMapper.selectArticleList(offset, pageSize, tagId, keyword);
        Long total = articleMapper.selectArticleCount(tagId, keyword);

        PageResult<Article> pageResult = new PageResult<>(total, pageNum, pageSize, articles);
        return Result.success(pageResult);
    }

    /**
     * 获取用户的文章列表
     */
    @Override
    public Result<PageResult<Article>> getUserArticles(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("获取用户文章列表，用户ID：{}，页码：{}，每页条数：{}", userId, pageNum, pageSize);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Article> articles = articleMapper.selectByUserId(userId, offset, pageSize);
        Long total = articleMapper.selectCountByUserId(userId);

        PageResult<Article> pageResult = new PageResult<>(total, pageNum, pageSize, articles);
        return Result.success(pageResult);
    }

    /**
     * 发布文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Article> publishArticle(Article article, List<Integer> tagIds) {
        logger.info("发布文章，作者ID：{}，标题：{}", article.getAuthor(), article.getTitle());

        // 参数校验
        if (article.getAuthor() == null) {
            return Result.fail("作者ID不能为空");
        }
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            return Result.fail("文章标题不能为空");
        }
        if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            return Result.fail("文章内容不能为空");
        }

        // 插入文章
        int result = articleMapper.insertArticle(article);
        if (result <= 0) {
            return Result.fail("文章发布失败");
        }

        // 插入文章-标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Integer tagId : tagIds) {
                articleMapper.insertArticleTag(article.getArticleId(), tagId);
            }
        }

        logger.info("文章发布成功，文章ID：{}", article.getArticleId());
        return Result.success("发布成功", article);
    }

    /**
     * 编辑文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Article> updateArticle(Article article, List<Integer> tagIds) {
        logger.info("编辑文章，文章ID：{}", article.getArticleId());

        // 参数校验
        if (article.getArticleId() == null) {
            return Result.fail("文章ID不能为空");
        }

        // 检查文章是否存在
        Article existArticle = articleMapper.selectById(article.getArticleId());
        if (existArticle == null) {
            return Result.fail("文章不存在");
        }

        // 更新文章
        int result = articleMapper.updateArticle(article);
        if (result <= 0) {
            return Result.fail("文章更新失败");
        }

        // 更新文章-标签关联（先删除，再插入）
        if (tagIds != null) {
            articleMapper.deleteArticleTags(article.getArticleId());
            for (Integer tagId : tagIds) {
                articleMapper.insertArticleTag(article.getArticleId(), tagId);
            }
        }

        logger.info("文章更新成功，文章ID：{}", article.getArticleId());
        return Result.success("更新成功", article);
    }

    /**
     * 删除文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteArticle(Integer articleId, Integer userId) {
        logger.info("删除文章，文章ID：{}，操作人ID：{}", articleId, userId);

        // 参数校验
        if (articleId == null) {
            return Result.fail("文章ID不能为空");
        }
        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 检查文章是否存在及权限
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.fail("文章不存在");
        }
        if (!article.getAuthor().equals(userId)) {
            return Result.fail("无权限删除此文章");
        }

        // 删除文章标签关联
        articleMapper.deleteArticleTags(articleId);

        // 删除文章
        int result = articleMapper.deleteById(articleId);
        if (result > 0) {
            logger.info("文章删除成功，文章ID：{}", articleId);
            return Result.success("删除成功", true);
        } else {
            return Result.fail("删除失败");
        }
    }

    /**
     * 获取热门文章
     */
    @Override
    public Result<List<Article>> getHotArticles(Integer limit) {
        logger.info("获取热门文章，数量：{}", limit);
        if (limit == null || limit < 1 || limit > 20) {
            limit = 10;
        }
        List<Article> articles = articleMapper.selectHotArticles(limit);
        return Result.success(articles);
    }

    /**
     * 增加文章浏览量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> incrementPageView(Integer articleId) {
        int result = articleMapper.incrementPageView(articleId);
        return result > 0 ? Result.success(true) : Result.fail("操作失败");
    }
}
