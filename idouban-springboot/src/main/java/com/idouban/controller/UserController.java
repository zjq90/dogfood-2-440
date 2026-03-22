package com.idouban.controller;

import com.idouban.common.Result;
import com.idouban.model.User;
import com.idouban.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session) {
        log.info("登录请求, username: {}", username);
        User user = userService.login(username, password);
        session.setAttribute("userInfo", user);
        session.setAttribute("uname", username);
        return Result.success("登录成功", user);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        log.info("注册请求, username: {}", user.getUsername());
        User registeredUser = userService.register(user);
        return Result.success("注册成功", registeredUser);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        log.info("退出登录");
        session.invalidate();
        return Result.success("退出成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        User userInfo = userService.getUserById(user.getUserId());
        return Result.success(userInfo);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    public Result<Void> updateUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("userInfo");
        if (currentUser == null) {
            return Result.error("未登录");
        }
        user.setUserId(currentUser.getUserId());
        boolean success = userService.updateUser(user);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/portrait")
    public Result<Void> updatePortrait(@RequestParam String portrait, HttpSession session) {
        User currentUser = (User) session.getAttribute("userInfo");
        if (currentUser == null) {
            return Result.error("未登录");
        }
        boolean success = userService.updatePortrait(currentUser.getUserId(), portrait);
        if (success) {
            currentUser.setPortrait(portrait);
            session.setAttribute("userInfo", currentUser);
        }
        return success ? Result.success("头像更新成功") : Result.error("头像更新失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPassword,
                                        @RequestParam String newPassword,
                                        HttpSession session) {
        User currentUser = (User) session.getAttribute("userInfo");
        if (currentUser == null) {
            return Result.error("未登录");
        }
        boolean success = userService.updatePassword(currentUser.getUserId(), oldPassword, newPassword);
        return success ? Result.success("密码修改成功") : Result.error("密码修改失败");
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.checkUsernameExist(username);
        return Result.success(exists);
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public Result<?> searchUsers(@RequestParam(required = false) String searchContent,
                                  @RequestParam(defaultValue = "1") int currentPage,
                                  @RequestParam(defaultValue = "10") int pageSize) {
        if (searchContent == null || searchContent.isEmpty()) {
            return Result.success(userService.getUserList(currentPage, pageSize));
        }
        return Result.success(userService.searchUsers(searchContent, currentPage, pageSize));
    }
}
