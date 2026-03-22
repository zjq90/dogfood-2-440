package com.lzh.idouban.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzh.idouban.entity.Article;
import com.lzh.idouban.mapper.ArticleMapper;
import com.lzh.idouban.service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章服务实现类
 * @author 林泽鸿
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public Article getArticleById(Integer articleId) {
        if (articleId == null) {
            return null;
        }
        return articleMapper.selectById(articleId);
    }

    @Override
    public PageInfo<Article> getArticleList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectArticleList();
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Article> getArticlesByUserId(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectByUserId(userId);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishArticle(Article article) {
        if (article == null || article.getTitle() == null || article.getContent() == null) {
            log.warn("发布文章失败：文章信息不完整");
            return false;
        }

        // 初始化统计数据
        article.setCollection(0);
        article.setShare(0);
        article.setComment(0);
        article.setStar(0);
        article.setStick(0);
        article.setPageView(0);

        int result = articleMapper.insert(article);
        if (result > 0) {
            log.info("文章发布成功：articleId={}, title={}", article.getArticleId(), article.getTitle());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(Article article) {
        if (article == null || article.getArticleId() == null) {
            return false;
        }

        int result = articleMapper.update(article);
        if (result > 0) {
            log.info("文章更新成功：articleId={}", article.getArticleId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(Integer articleId, Integer userId) {
        if (articleId == null) {
            return false;
        }

        // 查询文章，验证权限
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            log.warn("删除文章失败：文章不存在，articleId={}", articleId);
            return false;
        }

        // 验证是否是文章作者
        if (!article.getAuthorId().equals(userId)) {
            log.warn("删除文章失败：无权限，articleId={}, userId={}", articleId, userId);
            return false;
        }

        int result = articleMapper.deleteById(articleId);
        if (result > 0) {
            log.info("文章删除成功：articleId={}", articleId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementPageView(Integer articleId) {
        if (articleId != null) {
            articleMapper.incrementPageView(articleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean starArticle(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            return false;
        }

        // TODO: 需要添加a_star表记录点赞关系，这里简化处理
        int result = articleMapper.incrementStar(articleId);
        if (result > 0) {
            log.info("文章点赞成功：articleId={}, userId={}", articleId, userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unstarArticle(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            return false;
        }

        int result = articleMapper.decrementStar(articleId);
        if (result > 0) {
            log.info("文章取消点赞成功：articleId={}, userId={}", articleId, userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean collectArticle(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            return false;
        }

        // TODO: 需要添加a_collection表记录收藏关系
        int result = articleMapper.incrementCollection(articleId);
        if (result > 0) {
            log.info("文章收藏成功：articleId={}, userId={}", articleId, userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncollectArticle(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            return false;
        }

        int result = articleMapper.decrementCollection(articleId);
        if (result > 0) {
            log.info("文章取消收藏成功：articleId={}, userId={}", articleId, userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean shareArticle(Integer articleId, Integer userId) {
        if (articleId == null || userId == null) {
            return false;
        }

        // TODO: 需要添加a_share表记录转发关系
        int result = articleMapper.incrementShare(articleId);
        if (result > 0) {
            log.info("文章转发成功：articleId={}, userId={}", articleId, userId);
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<Article> searchArticles(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.searchByKeyword(keyword);
        return new PageInfo<>(list);
    }

}
