package com.idouban.controller;

import com.idouban.common.Result;
import com.idouban.model.Article;
import com.idouban.model.ArticleList;
import com.idouban.model.Page;
import com.idouban.model.User;
import com.idouban.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 文章控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 获取文章详情
     */
    @GetMapping("/{articleId}")
    public Result<Article> getArticle(@PathVariable Integer articleId) {
        log.info("获取文章详情, articleId: {}", articleId);
        Article article = articleService.getArticleById(articleId);
        return Result.success(article);
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    public Result<Article> publishArticle(@RequestBody Article article, HttpSession session) {
        log.info("发布文章请求");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        article.setAuthorId(user.getUserId());
        Article publishedArticle = articleService.publishArticle(article);
        return Result.success("发布成功", publishedArticle);
    }

    /**
     * 更新文章
     */
    @PostMapping("/update")
    public Result<Void> updateArticle(@RequestBody Article article, HttpSession session) {
        log.info("更新文章请求, articleId: {}", article.getArticleId());
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = articleService.updateArticle(article);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{articleId}")
    public Result<Void> deleteArticle(@PathVariable Integer articleId, HttpSession session) {
        log.info("删除文章请求, articleId: {}", articleId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = articleService.deleteArticle(articleId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 获取文章列表
     */
    @GetMapping("/list")
    public Result<Page<ArticleList>> getArticleList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("获取文章列表, currentPage: {}, pageSize: {}", currentPage, pageSize);
        Page<ArticleList> page = articleService.getArticleList(currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 获取我的文章列表
     */
    @GetMapping("/my")
    public Result<Page<ArticleList>> getMyArticles(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("获取我的文章列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<ArticleList> page = articleService.getArticleListByAuthor(user.getUserId(), currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 搜索文章
     */
    @GetMapping("/search")
    public Result<Page<ArticleList>> searchArticles(
            @RequestParam String searchContent,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("搜索文章, searchContent: {}", searchContent);
        Page<ArticleList> page = articleService.searchArticles(searchContent, currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 获取收藏的文章列表
     */
    @GetMapping("/collection")
    public Result<Page<ArticleList>> getCollectionArticles(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("获取收藏文章列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<ArticleList> page = articleService.getCollectionArticles(user.getUserId(), currentPage, pageSize);
        return Result.success(page);
    }
}
