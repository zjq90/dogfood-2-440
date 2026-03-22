package com.lzh.idouban.service.impl;

import com.lzh.idouban.entity.ArticleComment;
import com.lzh.idouban.mapper.ArticleCommentMapper;
import com.lzh.idouban.mapper.ArticleMapper;
import com.lzh.idouban.service.ArticleCommentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章评论服务实现类
 * @author 林泽鸿
 */
@Slf4j
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    @Resource
    private ArticleCommentMapper commentMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public ArticleComment getCommentById(Integer commentId) {
        if (commentId == null) {
            return null;
        }
        return commentMapper.selectById(commentId);
    }

    @Override
    public List<ArticleComment> getCommentsByArticleId(Integer articleId) {
        if (articleId == null) {
            return null;
        }
        return commentMapper.selectByArticleId(articleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(ArticleComment comment) {
        if (comment == null || comment.getArticleId() == null || comment.getUserId() == null) {
            log.warn("发表评论失败：评论信息不完整");
            return false;
        }

        if (comment.getCommentMsg() == null || comment.getCommentMsg().trim().isEmpty()) {
            log.warn("发表评论失败：评论内容为空");
            return false;
        }

        comment.setCommentStar(0);

        int result = commentMapper.insert(comment);
        if (result > 0) {
            // 增加文章评论数
            articleMapper.incrementComment(comment.getArticleId());
            log.info("发表评论成功：articleId={}, userId={}", comment.getArticleId(), comment.getUserId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(Integer commentId, Integer userId) {
        if (commentId == null || userId == null) {
            return false;
        }

        ArticleComment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            log.warn("删除评论失败：评论不存在，commentId={}", commentId);
            return false;
        }

        // 验证是否是评论作者
        if (!comment.getUserId().equals(userId)) {
            log.warn("删除评论失败：无权限，commentId={}, userId={}", commentId, userId);
            return false;
        }

        int result = commentMapper.deleteById(commentId);
        if (result > 0) {
            // 减少文章评论数
            articleMapper.decrementComment(comment.getArticleId());
            log.info("删除评论成功：commentId={}", commentId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean starComment(Integer commentId, Integer userId) {
        if (commentId == null || userId == null) {
            return false;
        }

        int result = commentMapper.incrementStar(commentId);
        if (result > 0) {
            log.info("评论点赞成功：commentId={}, userId={}", commentId, userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unstarComment(Integer commentId, Integer userId) {
        if (commentId == null || userId == null) {
            return false;
        }

        int result = commentMapper.decrementStar(commentId);
        if (result > 0) {
            log.info("评论取消点赞成功：commentId={}, userId={}", commentId, userId);
            return true;
        }
        return false;
    }

}
