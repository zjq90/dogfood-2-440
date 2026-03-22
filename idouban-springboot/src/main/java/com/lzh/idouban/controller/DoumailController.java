package com.lzh.idouban.controller;

import com.lzh.idouban.common.Result;
import com.lzh.idouban.entity.Doumail;
import com.lzh.idouban.entity.User;
import com.lzh.idouban.service.DoumailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 豆邮（私信）控制器
 * @author 林泽鸿
 */
@Slf4j
@Controller
@RequestMapping("/doumail")
public class DoumailController {

    @Resource
    private DoumailService doumailService;

    /**
     * 豆邮列表页面
     */
    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Doumail> chatList = doumailService.getChatList(user.getUserId());
        model.addAttribute("chatList", chatList);
        return "doumail_list";
    }

    /**
     * 聊天详情页面
     */
    @GetMapping("/chat/{otherUserId}")
    public String chat(@PathVariable Integer otherUserId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return "redirect:/login";
        }

        List<Doumail> messages = doumailService.getChatHistory(user.getUserId(), otherUserId);
        // 标记消息为已读
        for (Doumail msg : messages) {
            if (msg.getToUserId().equals(user.getUserId()) && msg.getRead() == 0) {
                doumailService.markAsRead(msg.getDoumailId(), user.getUserId());
            }
        }

        model.addAttribute("messages", messages);
        model.addAttribute("otherUserId", otherUserId);
        return "doumail_chat";
    }

    /**
     * 发送豆邮
     */
    @PostMapping("/send")
    @ResponseBody
    public Result<Void> send(@RequestParam Integer toUserId,
                             @RequestParam String content,
                             HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        Doumail doumail = new Doumail();
        doumail.setFromUserId(user.getUserId());
        doumail.setToUserId(toUserId);
        doumail.setChatMsg(content);

        boolean success = doumailService.sendDoumail(doumail);
        if (success) {
            return Result.success("发送成功", null);
        }
        return Result.error("发送失败");
    }

    /**
     * 获取未读消息数
     */
    @GetMapping("/unreadCount")
    @ResponseBody
    public Result<Integer> getUnreadCount(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        int count = doumailService.getUnreadCount(user.getUserId());
        return Result.success(count);
    }

    /**
     * 标记所有消息为已读
     */
    @PostMapping("/markAllRead")
    @ResponseBody
    public Result<Void> markAllRead(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = doumailService.markAllAsRead(user.getUserId());
        if (success) {
            return Result.success("标记成功", null);
        }
        return Result.error("标记失败");
    }

    /**
     * 删除豆邮
     */
    @PostMapping("/delete/{doumailId}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Integer doumailId, HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }

        boolean success = doumailService.deleteDoumail(doumailId, user.getUserId());
        if (success) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

}
