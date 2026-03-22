package com.idouban.controller;

import com.idouban.common.Result;
import com.idouban.model.Page;
import com.idouban.model.User;
import com.idouban.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 好友控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/friend")
public class FriendController {

    @Resource
    private FriendService friendService;

    /**
     * 关注用户
     */
    @PostMapping("/follow/{toUserId}")
    public Result<Void> followUser(@PathVariable Integer toUserId, HttpSession session) {
        log.info("关注用户, toUserId: {}", toUserId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.followUser(user.getUserId(), toUserId);
        return success ? Result.success("关注成功") : Result.error("关注失败");
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/follow/{toUserId}")
    public Result<Void> unfollowUser(@PathVariable Integer toUserId, HttpSession session) {
        log.info("取消关注, toUserId: {}", toUserId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.unfollowUser(user.getUserId(), toUserId);
        return success ? Result.success("取消关注成功") : Result.error("取消关注失败");
    }

    /**
     * 拉黑用户
     */
    @PostMapping("/blacklist/{toUserId}")
    public Result<Void> blacklistUser(@PathVariable Integer toUserId, HttpSession session) {
        log.info("拉黑用户, toUserId: {}", toUserId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.blacklistUser(user.getUserId(), toUserId);
        return success ? Result.success("拉黑成功") : Result.error("拉黑失败");
    }

    /**
     * 取消拉黑
     */
    @DeleteMapping("/blacklist/{toUserId}")
    public Result<Void> unblacklistUser(@PathVariable Integer toUserId, HttpSession session) {
        log.info("取消拉黑, toUserId: {}", toUserId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.unblacklistUser(user.getUserId(), toUserId);
        return success ? Result.success("取消拉黑成功") : Result.error("取消拉黑失败");
    }

    /**
     * 获取关注列表
     */
    @GetMapping("/following")
    public Result<Page<User>> getFollowingList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("获取关注列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<User> page = friendService.getFollowingList(user.getUserId(), currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public Result<Page<User>> getFriendList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("获取好友列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<User> page = friendService.getFriendList(user.getUserId(), currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 获取黑名单列表
     */
    @GetMapping("/blacklist")
    public Result<Page<User>> getBlacklist(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("获取黑名单列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<User> page = friendService.getBlacklist(user.getUserId(), currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 搜索好友
     */
    @GetMapping("/search")
    public Result<Page<User>> searchFriends(
            @RequestParam Integer status,
            @RequestParam(required = false) String searchContent,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session) {
        log.info("搜索好友, status: {}, searchContent: {}", status, searchContent);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<User> page = friendService.searchFriends(user.getUserId(), status, searchContent, currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 检查是否关注
     */
    @GetMapping("/check/{toUserId}")
    public Result<Integer> checkFollow(@PathVariable Integer toUserId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.success(0);
        }
        Integer status = friendService.getFriendStatus(user.getUserId(), toUserId);
        return Result.success(status != null ? status : 0);
    }

    /**
     * 创建好友分组
     */
    @PostMapping("/group")
    public Result<Void> createGroup(@RequestParam String groupName, HttpSession session) {
        log.info("创建好友分组, groupName: {}", groupName);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.createGroup(groupName, user.getUserId());
        return success ? Result.success("创建成功") : Result.error("分组已存在");
    }

    /**
     * 设置好友分组
     */
    @PostMapping("/group/set")
    public Result<Void> setFriendGroup(@RequestParam Integer toUserId,
                                        @RequestParam String groupName,
                                        HttpSession session) {
        log.info("设置好友分组, toUserId: {}, groupName: {}", toUserId, groupName);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = friendService.setFriendGroup(user.getUserId(), toUserId, groupName);
        return success ? Result.success("设置成功") : Result.error("设置失败");
    }
}
