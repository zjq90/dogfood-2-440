package com.lzh.idouban.controller;

import com.lzh.idouban.common.Result;
import com.lzh.idouban.entity.User;
import com.lzh.idouban.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制器
 * @author 林泽鸿
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false) String remember,
                        HttpSession session,
                        HttpServletResponse response,
                        Model model) {

        log.info("用户登录请求：username={}", username);

        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("errorMsg", "用户名为空");
            return "login";
        }

        // 登录验证
        User user = userService.login(username, password);
        if (user == null) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "login";
        }

        // 登录成功，设置Session
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userInfo", user);

        // 记住密码
        if ("on".equals(remember)) {
            Cookie cookie = new Cookie("rememberUsername", username);
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7天
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        log.info("用户登录成功：username={}", username);
        return "redirect:/article/list";
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        log.info("用户注册请求：username={}", username);

        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("errorMsg", "用户名为空");
            return "register";
        }
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorMsg", "密码为空");
            return "register";
        }

        // 检查用户名是否已存在
        if (userService.checkUsernameExists(username)) {
            model.addAttribute("errorMsg", "用户名已存在");
            return "register";
        }

        // 注册用户
        User user = userService.register(username, password);
        if (user == null) {
            model.addAttribute("errorMsg", "注册失败，请重试");
            return "register";
        }

        log.info("用户注册成功：username={}", username);
        model.addAttribute("successMsg", "注册成功，请登录");
        return "login";
    }

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 清除Session
        session.invalidate();

        // 清除Cookie
        Cookie cookie = new Cookie("rememberUsername", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("用户登出");
        return "redirect:/login";
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    @ResponseBody
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<Void> updateUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("userInfo");
        if (currentUser == null) {
            return Result.error("未登录");
        }

        user.setUserId(currentUser.getUserId());
        boolean success = userService.updateUser(user);
        if (success) {
            // 更新Session中的用户信息
            User updatedUser = userService.getUserById(currentUser.getUserId());
            session.setAttribute("userInfo", updatedUser);
            return Result.success("更新成功", null);
        }
        return Result.error("更新失败");
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    @ResponseBody
    public Result<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return Result.success(users);
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/checkUsername")
    @ResponseBody
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsernameExists(username);
        return Result.success(exists);
    }

}
