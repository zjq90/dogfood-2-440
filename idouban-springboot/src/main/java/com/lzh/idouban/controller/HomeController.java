package com.lzh.idouban.controller;

import com.lzh.idouban.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 首页控制器
 * @author 林泽鸿
 */
@Controller
public class HomeController {

    /**
     * 首页
     */
    @GetMapping({"/", "/index"})
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }
        return "redirect:/article/list";
    }

    /**
     * 个人主页
     */
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

}
