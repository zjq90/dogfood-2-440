package com.idouban.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 页面控制器
 * 处理页面跳转请求
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/my_page")
    public String myPage(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "my_page";
    }

    @GetMapping("/alter")
    public String alter(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "alter";
    }

    @GetMapping("/article_list")
    public String articleList() {
        return "article_list";
    }

    @GetMapping("/article_show")
    public String articleShow() {
        return "article_show";
    }

    @GetMapping("/article_edit")
    public String articleEdit(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "article_edit";
    }

    @GetMapping("/everyone")
    public String everyone() {
        return "everyone";
    }

    @GetMapping("/friend")
    public String friend(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "friend";
    }

    @GetMapping("/attention")
    public String attention(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "attention";
    }

    @GetMapping("/blacklist")
    public String blacklist(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "blacklist";
    }

    @GetMapping("/doumail")
    public String doumail(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "doumail";
    }

    @GetMapping("/doumail_show")
    public String doumailShow(HttpSession session) {
        if (session.getAttribute("userInfo") == null) {
            return "redirect:/login";
        }
        return "doumail_show";
    }
}
