package com.lzh.idouban.controller;

import com.lzh.idouban.common.Result;
import com.lzh.idouban.entity.Friend;
import com.lzh.idouban.entity.User;
import com.lzh.idouban.service.FriendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 好友关系控制器
 * @author 林泽鸿
 */
@Slf4j
@Controller
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private FriendService friendService;

    /**
     * 关注用户
     */
    @PostMapping("/follow/{toUserId}")
    @ResponseBody
    public Result<Void> follow(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = friendService.follow(user.getUserId(), toUserId);
        if (success) {
            return Result.success("关注成功", null);
        }
        return Result.error("关注失败");
    }

    /**
     * 取消关注
     */
    @PostMapping("/unfollow/{toUserId}")
    @ResponseBody
    public Result<Void> unfollow(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = friendService.unfollow(user.getUserId(), toUserId);
        if (success) {
            return Result.success("取消关注成功", null);
        }
        return Result.error("取消关注失败");
    }

    /**
     * 拉黑用户
     */
    @PostMapping("/blacklist/{toUserId}")
    @ResponseBody
    public Result<Void> blacklist(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = friendService.blacklist(user.getUserId(), toUserId);
        if (success) {
            return Result.success("拉黑成功", null);
        }
        return Result.error("拉黑失败");
    }

    /**
     * 取消拉黑
     */
    @PostMapping("/unblacklist/{toUserId}")
    @ResponseBody
    public Result<Void> unblacklist(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = friendService.unblacklist(user.getUserId(), toUserId);
        if (success) {
            return Result.success("取消拉黑成功", null);
        }
        return Result.error("取消拉黑失败");
    }

    /**
     * 关注列表页面
     */
    @GetMapping("/following")
    public String followingList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Friend> following = friendService.getFollowingList(user.getUserId());
        model.addAttribute("following", following);
        return "friend_following";
    }

    /**
     * 粉丝列表页面
     */
    @GetMapping("/followers")
    public String followerList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Friend> followers = friendService.getFollowerList(user.getUserId());
        model.addAttribute("followers", followers);
        return "friend_followers";
    }

    /**
     * 好友列表页面
     */
    @GetMapping("/list")
    public String friendList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Friend> friends = friendService.getFriendList(user.getUserId());
        model.addAttribute("friends", friends);
        return "friend_list";
    }

    /**
     * 黑名单页面
     */
    @GetMapping("/blacklist")
    public String blacklist(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Friend> blacklist = friendService.getBlacklist(user.getUserId());
        model.addAttribute("blacklist", blacklist);
        return "friend_blacklist";
    }

    /**
     * 检查关注状态
     */
    @GetMapping("/isFollowing/{toUserId}")
    @ResponseBody
    public Result<Boolean> isFollowing(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean isFollowing = friendService.isFollowing(user.getUserId(), toUserId);
        return Result.success(isFollowing);
    }

    /**
     * 获取关注数
     */
    @GetMapping("/followingCount")
    @ResponseBody
    public Result<Integer> getFollowingCount(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        int count = friendService.getFollowingCount(user.getUserId());
        return Result.success(count);
    }

    /**
     * 获取粉丝数
     */
    @GetMapping("/followerCount")
    @ResponseBody
    public Result<Integer> getFollowerCount(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        int count = friendService.getFollowerCount(user.getUserId());
        return Result.success(count);
    }

    /**
     * 获取好友数
     */
    @GetMapping("/friendCount")
    @ResponseBody
    public Result<Integer> getFriendCount(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        int count = friendService.getFriendCount(user.getUserId());
        return Result.success(count);
    }

}
