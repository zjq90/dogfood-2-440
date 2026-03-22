package com.lzh.idouban.service.impl;

import com.lzh.idouban.entity.Doumail;
import com.lzh.idouban.mapper.DoumailMapper;
import com.lzh.idouban.service.DoumailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 豆邮（私信）服务实现类
 * @author 林泽鸿
 */
@Slf4j
@Service
public class DoumailServiceImpl implements DoumailService {

    @Resource
    private DoumailMapper doumailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendDoumail(Doumail doumail) {
        if (doumail == null || doumail.getFromUserId() == null || doumail.getToUserId() == null) {
            log.warn("发送豆邮失败：信息不完整");
            return false;
        }

        if (doumail.getChatMsg() == null || doumail.getChatMsg().trim().isEmpty()) {
            log.warn("发送豆邮失败：内容为空");
            return false;
        }

        // 不能给自己发送
        if (doumail.getFromUserId().equals(doumail.getToUserId())) {
            log.warn("发送豆邮失败：不能给自己发送");
            return false;
        }

        doumail.setStatus(0); // 双方都未删除
        doumail.setRead(0);   // 未读

        int result = doumailMapper.insert(doumail);
        if (result > 0) {
            log.info("发送豆邮成功：fromUserId={}, toUserId={}", doumail.getFromUserId(), doumail.getToUserId());
            return true;
        }
        return false;
    }

    @Override
    public List<Doumail> getChatHistory(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null) {
            return null;
        }
        return doumailMapper.selectChatHistory(userId1, userId2);
    }

    @Override
    public List<Doumail> getChatList(Integer userId) {
        if (userId == null) {
            return null;
        }
        return doumailMapper.selectChatList(userId);
    }

    @Override
    public List<Doumail> getUnreadMessages(Integer userId) {
        if (userId == null) {
            return null;
        }
        return doumailMapper.selectUnreadByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Integer doumailId, Integer userId) {
        if (doumailId == null || userId == null) {
            return false;
        }

        Doumail doumail = doumailMapper.selectById(doumailId);
        if (doumail == null) {
            log.warn("标记已读失败：消息不存在，doumailId={}", doumailId);
            return false;
        }

        // 验证是否是接收者
        if (!doumail.getToUserId().equals(userId)) {
            log.warn("标记已读失败：无权限，doumailId={}, userId={}", doumailId, userId);
            return false;
        }

        int result = doumailMapper.markAsRead(doumailId);
        if (result > 0) {
            log.info("标记已读成功：doumailId={}", doumailId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAllAsRead(Integer userId) {
        if (userId == null) {
            return false;
        }

        int result = doumailMapper.markAllAsRead(userId);
        log.info("标记所有消息已读成功：userId={}, count={}", userId, result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDoumail(Integer doumailId, Integer userId) {
        if (doumailId == null || userId == null) {
            return false;
        }

        Doumail doumail = doumailMapper.selectById(doumailId);
        if (doumail == null) {
            log.warn("删除豆邮失败：消息不存在，doumailId={}", doumailId);
            return false;
        }

        int status;
        // 发送者删除
        if (doumail.getFromUserId().equals(userId)) {
            status = 1;
        }
        // 接收者删除
        else if (doumail.getToUserId().equals(userId)) {
            status = 2;
        }
        else {
            log.warn("删除豆邮失败：无权限，doumailId={}, userId={}", doumailId, userId);
            return false;
        }

        int result = doumailMapper.updateStatus(doumailId, status);
        if (result > 0) {
            log.info("删除豆邮成功：doumailId={}, userId={}, status={}", doumailId, userId, status);
            return true;
        }
        return false;
    }

    @Override
    public int getUnreadCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        return doumailMapper.countUnread(userId);
    }

}
