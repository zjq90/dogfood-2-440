package com.lzh.idouban.controller;

import com.github.pagehelper.PageInfo;
import com.lzh.idouban.common.PageResult;
import com.lzh.idouban.common.Result;
import com.lzh.idouban.entity.Article;
import com.lzh.idouban.entity.ArticleComment;
import com.lzh.idouban.entity.User;
import com.lzh.idouban.service.ArticleCommentService;
import com.lzh.idouban.service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 文章控制器
 * @author 林泽鸿
 */
@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleCommentService commentService;

    /**
     * 文章列表页面
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model) {
        PageInfo<Article> pageInfo = articleService.getArticleList(pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("articles", pageInfo.getList());
        return "article_list";
    }

    /**
     * 文章详情页面
     */
    @GetMapping("/detail/{articleId}")
    public String detail(@PathVariable Integer articleId, Model model, HttpSession session) {
        // 增加浏览量
        articleService.incrementPageView(articleId);

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            model.addAttribute("errorMsg", "文章不存在");
            return "error";
        }

        // 获取评论列表
        List<ArticleComment> comments = commentService.getCommentsByArticleId(articleId);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article_detail";
    }

    /**
     * 发布文章页面
     */
    @GetMapping("/publish")
    public String publishPage() {
        return "article_edit";
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    @ResponseBody
    public Result<Void> publish(@RequestBody Article article, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        article.setAuthorId(user.getUserId());
        boolean success = articleService.publishArticle(article);
        if (success) {
            return Result.success("发布成功", null);
        }
        return Result.error("发布失败");
    }

    /**
     * 更新文章
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> update(@RequestBody Article article, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        // 验证是否是文章作者
        Article existing = articleService.getArticleById(article.getArticleId());
        if (existing == null) {
            return Result.error("文章不存在");
        }
        if (!existing.getAuthorId().equals(user.getUserId())) {
            return Result.error("无权限修改");
        }

        boolean success = articleService.updateArticle(article);
        if (success) {
            return Result.success("更新成功", null);
        }
        return Result.error("更新失败");
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete/{articleId}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Integer articleId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = articleService.deleteArticle(articleId, user.getUserId());
        if (success) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

    /**
     * 点赞文章
     */
    @PostMapping("/star/{articleId}")
    @ResponseBody
    public Result<Void> star(@PathVariable Integer articleId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = articleService.starArticle(articleId, user.getUserId());
        if (success) {
            return Result.success("点赞成功", null);
        }
        return Result.error("点赞失败");
    }

    /**
     * 取消点赞
     */
    @PostMapping("/unstar/{articleId}")
    @ResponseBody
    public Result<Void> unstar(@PathVariable Integer articleId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = articleService.unstarArticle(articleId, user.getUserId());
        if (success) {
            return Result.success("取消点赞成功", null);
        }
        return Result.error("取消点赞失败");
    }

    /**
     * 收藏文章
     */
    @PostMapping("/collect/{articleId}")
    @ResponseBody
    public Result<Void> collect(@PathVariable Integer articleId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = articleService.collectArticle(articleId, user.getUserId());
        if (success) {
            return Result.success("收藏成功", null);
        }
        return Result.error("收藏失败");
    }

    /**
     * 取消收藏
     */
    @PostMapping("/uncollect/{articleId}")
    @ResponseBody
    public Result<Void> uncollect(@PathVariable Integer articleId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = articleService.uncollectArticle(articleId, user.getUserId());
        if (success) {
            return Result.success("取消收藏成功", null);
        }
        return Result.error("取消收藏失败");
    }

    /**
     * 搜索文章
     */
    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                         @RequestParam(defaultValue = "1") int pageNum,
                         @RequestParam(defaultValue = "10") int pageSize,
                         Model model) {
        PageInfo<Article> pageInfo = articleService.searchArticles(keyword, pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("articles", pageInfo.getList());
        model.addAttribute("keyword", keyword);
        return "article_list";
    }

    /**
     * 获取文章列表（API）
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<PageResult<Article>> apiList(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<Article> pageInfo = articleService.getArticleList(pageNum, pageSize);
        return Result.success(PageResult.of(pageInfo));
    }

}
