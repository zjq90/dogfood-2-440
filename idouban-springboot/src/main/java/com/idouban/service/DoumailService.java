package com.idouban.service;

import com.idouban.mapper.DoumailMapper;
import com.idouban.mapper.UserMapper;
import com.idouban.model.Doumail;
import com.idouban.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 豆邮(私信)服务类
 */
@Slf4j
@Service
public class DoumailService {

    @Resource
    private DoumailMapper doumailMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 发送豆邮
     */
    @Transactional(rollbackFor = Exception.class)
    public Doumail sendDoumail(Doumail doumail) {
        log.info("发送豆邮, fromUserId: {}, toUserId: {}", doumail.getFromUserId(), doumail.getToUserId());
        doumailMapper.insert(doumail);
        log.info("豆邮发送成功, doumailId: {}", doumail.getDoumailId());
        return doumail;
    }

    /**
     * 获取两个用户之间的豆邮列表
     */
    public Page<Doumail> getDoumailList(Integer userId, Integer toUserId, int currentPage, int pageSize) {
        log.debug("获取豆邮列表, userId: {}, toUserId: {}", userId, toUserId);
        int offset = (currentPage - 1) * pageSize;
        List<Doumail> doumails = doumailMapper.selectByUsers(userId, toUserId, offset, pageSize);
        int totalCount = doumailMapper.selectCountByUsers(userId, toUserId);
        
        doumails = fillUserInfo(doumails);
        
        Page<Doumail> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(doumails);
        return page;
    }

    /**
     * 获取用户的联系人列表
     */
    public List<Doumail> getContacts(Integer userId) {
        log.debug("获取联系人列表, userId: {}", userId);
        List<Doumail> contacts = doumailMapper.selectContacts(userId);
        return fillUserInfo(contacts);
    }

    /**
     * 标记豆邮为已读
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Integer doumailId) {
        log.info("标记豆邮已读, doumailId: {}", doumailId);
        return doumailMapper.updateReadStatus(doumailId) > 0;
    }

    /**
     * 获取未读豆邮数量
     */
    public int getUnreadCount(Integer userId) {
        return doumailMapper.selectUnreadCount(userId);
    }

    /**
     * 删除豆邮
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDoumail(Integer doumailId) {
        log.info("删除豆邮, doumailId: {}", doumailId);
        return doumailMapper.delete(doumailId) > 0;
    }

    /**
     * 填充用户信息
     */
    private List<Doumail> fillUserInfo(List<Doumail> doumails) {
        List<Doumail> result = new ArrayList<>();
        for (Doumail doumail : doumails) {
            if (doumail.getFromUserId() != null) {
                User fromUser = userMapper.selectByUserId(doumail.getFromUserId());
                if (fromUser != null) {
                    doumail.setFromUserNick(fromUser.getNickname());
                    doumail.setFromUserImg(fromUser.getPortrait());
                }
            }
            if (doumail.getToUserId() != null) {
                User toUser = userMapper.selectByUserId(doumail.getToUserId());
                if (toUser != null) {
                    doumail.setToUserNick(toUser.getNickname());
                    doumail.setToUserImg(toUser.getPortrait());
                }
            }
            result.add(doumail);
        }
        return result;
    }
}
