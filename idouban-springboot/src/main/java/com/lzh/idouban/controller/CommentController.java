package com.lzh.idouban.controller;

import com.lzh.idouban.common.Result;
import com.lzh.idouban.entity.ArticleComment;
import com.lzh.idouban.entity.User;
import com.lzh.idouban.service.ArticleCommentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 评论控制器
 * @author 林泽鸿
 */
@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ArticleCommentService commentService;

    /**
     * 发表评论
     */
    @PostMapping("/add")
    @ResponseBody
    public Result<Void> addComment(@RequestParam Integer articleId,
                                   @RequestParam String content,
                                   HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        ArticleComment comment = new ArticleComment();
        comment.setArticleId(articleId);
        comment.setUserId(user.getUserId());
        comment.setCommentMsg(content);

        boolean success = commentService.addComment(comment);
        if (success) {
            return Result.success("评论成功", null);
        }
        return Result.error("评论失败");
    }

    /**
     * 删除评论
     */
    @PostMapping("/delete/{commentId}")
    @ResponseBody
    public Result<Void> deleteComment(@PathVariable Integer commentId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = commentService.deleteComment(commentId, user.getUserId());
        if (success) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

    /**
     * 获取文章评论列表
     */
    @GetMapping("/list/{articleId}")
    @ResponseBody
    public Result<List<ArticleComment>> getComments(@PathVariable Integer articleId) {
        List<ArticleComment> comments = commentService.getCommentsByArticleId(articleId);
        return Result.success(comments);
    }

    /**
     * 点赞评论
     */
    @PostMapping("/star/{commentId}")
    @ResponseBody
    public Result<Void> starComment(@PathVariable Integer commentId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = commentService.starComment(commentId, user.getUserId());
        if (success) {
            return Result.success("点赞成功", null);
        }
        return Result.error("点赞失败");
    }

    /**
     * 取消点赞评论
     */
    @PostMapping("/unstar/{commentId}")
    @ResponseBody
    public Result<Void> unstarComment(@PathVariable Integer commentId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = commentService.unstarComment(commentId, user.getUserId());
        if (success) {
            return Result.success("取消点赞成功", null);
        }
        return Result.error("取消点赞失败");
    }

}
