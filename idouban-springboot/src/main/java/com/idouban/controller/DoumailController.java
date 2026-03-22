package com.idouban.controller;

import com.idouban.common.Result;
import com.idouban.model.Doumail;
import com.idouban.model.Page;
import com.idouban.model.User;
import com.idouban.service.DoumailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 豆邮(私信)控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/doumail")
public class DoumailController {

    @Resource
    private DoumailService doumailService;

    /**
     * 发送豆邮
     */
    @PostMapping("/send")
    public Result<Doumail> sendDoumail(@RequestBody Doumail doumail, HttpSession session) {
        log.info("发送豆邮, toUserId: {}", doumail.getToUserId());
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        doumail.setFromUserId(user.getUserId());
        Doumail savedDoumail = doumailService.sendDoumail(doumail);
        return Result.success("发送成功", savedDoumail);
    }

    /**
     * 获取与某用户的豆邮列表
     */
    @GetMapping("/list/{toUserId}")
    public Result<Page<Doumail>> getDoumailList(
            @PathVariable Integer toUserId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpSession session) {
        log.info("获取豆邮列表, toUserId: {}", toUserId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        Page<Doumail> page = doumailService.getDoumailList(user.getUserId(), toUserId, currentPage, pageSize);
        return Result.success(page);
    }

    /**
     * 获取联系人列表
     */
    @GetMapping("/contacts")
    public Result<List<Doumail>> getContacts(HttpSession session) {
        log.info("获取联系人列表");
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        List<Doumail> contacts = doumailService.getContacts(user.getUserId());
        return Result.success(contacts);
    }

    /**
     * 标记豆邮已读
     */
    @PostMapping("/read/{doumailId}")
    public Result<Void> markAsRead(@PathVariable Integer doumailId) {
        log.info("标记豆邮已读, doumailId: {}", doumailId);
        boolean success = doumailService.markAsRead(doumailId);
        return success ? Result.success("标记成功") : Result.error("标记失败");
    }

    /**
     * 获取未读豆邮数量
     */
    @GetMapping("/unread")
    public Result<Integer> getUnreadCount(HttpSession session) {
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.success(0);
        }
        int count = doumailService.getUnreadCount(user.getUserId());
        return Result.success(count);
    }

    /**
     * 删除豆邮
     */
    @DeleteMapping("/{doumailId}")
    public Result<Void> deleteDoumail(@PathVariable Integer doumailId, HttpSession session) {
        log.info("删除豆邮, doumailId: {}", doumailId);
        User user = (User) session.getAttribute("userInfo");
        if (user == null) {
            return Result.error("未登录");
        }
        boolean success = doumailService.deleteDoumail(doumailId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
