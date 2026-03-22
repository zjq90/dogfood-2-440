package com.idouban.service;

import com.idouban.mapper.FriendMapper;
import com.idouban.model.Friend;
import com.idouban.model.Page;
import com.idouban.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 好友服务类
 */
@Slf4j
@Service
public class FriendService {

    private static final int STATUS_FOLLOW = 1;
    private static final int STATUS_FRIEND = 2;
    private static final int STATUS_BLACKLIST = 3;

    @Resource
    private FriendMapper friendMapper;

    /**
     * 关注用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean followUser(Integer fromUserId, Integer toUserId) {
        log.info("关注用户, fromUserId: {}, toUserId: {}", fromUserId, toUserId);
        
        if (fromUserId.equals(toUserId)) {
            log.warn("不能关注自己");
            return false;
        }
        
        int count = friendMapper.checkFollow(fromUserId, toUserId);
        if (count > 0) {
            log.warn("已关注该用户");
            return false;
        }
        
        Friend friend = new Friend();
        friend.setFromUserId(fromUserId);
        friend.setToUserId(toUserId);
        friend.setStatus(STATUS_FOLLOW);
        friendMapper.insert(friend);
        
        Friend reverse = friendMapper.selectRelation(toUserId, fromUserId);
        if (reverse != null && reverse.getStatus() == STATUS_FOLLOW) {
            friendMapper.updateStatus(fromUserId, toUserId, STATUS_FRIEND);
            friendMapper.updateStatus(toUserId, fromUserId, STATUS_FRIEND);
            log.info("双方互相关注，成为好友");
        }
        
        return true;
    }

    /**
     * 取消关注
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean unfollowUser(Integer fromUserId, Integer toUserId) {
        log.info("取消关注, fromUserId: {}, toUserId: {}", fromUserId, toUserId);
        
        Friend friend = friendMapper.selectRelation(fromUserId, toUserId);
        if (friend == null) {
            log.warn("未关注该用户");
            return false;
        }
        
        friendMapper.delete(fromUserId, toUserId);
        
        if (friend.getStatus() == STATUS_FRIEND) {
            friendMapper.updateStatus(toUserId, fromUserId, STATUS_FOLLOW);
            log.info("解除好友关系");
        }
        
        return true;
    }

    /**
     * 拉黑用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean blacklistUser(Integer fromUserId, Integer toUserId) {
        log.info("拉黑用户, fromUserId: {}, toUserId: {}", fromUserId, toUserId);
        
        Friend friend = friendMapper.selectRelation(fromUserId, toUserId);
        if (friend != null) {
            friendMapper.updateStatus(fromUserId, toUserId, STATUS_BLACKLIST);
        } else {
            Friend newFriend = new Friend();
            newFriend.setFromUserId(fromUserId);
            newFriend.setToUserId(toUserId);
            newFriend.setStatus(STATUS_BLACKLIST);
            friendMapper.insert(newFriend);
        }
        
        return true;
    }

    /**
     * 取消拉黑
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean unblacklistUser(Integer fromUserId, Integer toUserId) {
        log.info("取消拉黑, fromUserId: {}, toUserId: {}", fromUserId, toUserId);
        int result = friendMapper.delete(fromUserId, toUserId);
        return result > 0;
    }

    /**
     * 获取关注列表
     */
    public Page<User> getFollowingList(Integer userId, int currentPage, int pageSize) {
        log.debug("获取关注列表, userId: {}", userId);
        return getFriendList(userId, STATUS_FOLLOW, currentPage, pageSize, null);
    }

    /**
     * 获取好友列表
     */
    public Page<User> getFriendList(Integer userId, int currentPage, int pageSize) {
        log.debug("获取好友列表, userId: {}", userId);
        return getFriendList(userId, STATUS_FRIEND, currentPage, pageSize, null);
    }

    /**
     * 获取黑名单列表
     */
    public Page<User> getBlacklist(Integer userId, int currentPage, int pageSize) {
        log.debug("获取黑名单列表, userId: {}", userId);
        return getFriendList(userId, STATUS_BLACKLIST, currentPage, pageSize, null);
    }

    /**
     * 搜索好友列表
     */
    public Page<User> searchFriends(Integer userId, Integer status, String searchContent, 
                                     int currentPage, int pageSize) {
        log.debug("搜索好友, userId: {}, status: {}, searchContent: {}", userId, status, searchContent);
        return getFriendList(userId, status, currentPage, pageSize, searchContent);
    }

    /**
     * 获取好友列表(通用方法)
     */
    private Page<User> getFriendList(Integer userId, Integer status, int currentPage, 
                                      int pageSize, String searchContent) {
        int offset = (currentPage - 1) * pageSize;
        List<User> users;
        int totalCount;
        
        if (searchContent != null && !searchContent.isEmpty()) {
            users = friendMapper.selectSearchFriendsByPage(userId, status, searchContent, offset, pageSize);
            totalCount = friendMapper.selectSearchFriendCount(userId, status, searchContent);
        } else {
            users = friendMapper.selectFriendsByPage(userId, status, offset, pageSize);
            totalCount = friendMapper.selectFriendCount(userId, status);
        }
        
        Page<User> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(users);
        return page;
    }

    /**
     * 检查是否关注
     */
    public boolean isFollowing(Integer fromUserId, Integer toUserId) {
        return friendMapper.checkFollow(fromUserId, toUserId) > 0;
    }

    /**
     * 获取好友关系状态
     */
    public Integer getFriendStatus(Integer fromUserId, Integer toUserId) {
        Friend friend = friendMapper.selectRelation(fromUserId, toUserId);
        return friend != null ? friend.getStatus() : null;
    }

    /**
     * 创建好友分组
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroup(String groupName, Integer userId) {
        log.info("创建好友分组, userId: {}, groupName: {}", userId, groupName);
        
        int count = friendMapper.checkGroupExist(groupName, userId);
        if (count > 0) {
            log.warn("分组已存在");
            return false;
        }
        
        friendMapper.insertGroup(groupName, userId);
        return true;
    }

    /**
     * 设置好友分组
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setFriendGroup(Integer fromUserId, Integer toUserId, String groupName) {
        log.info("设置好友分组, fromUserId: {}, toUserId: {}, groupName: {}", fromUserId, toUserId, groupName);
        
        Integer groupId = friendMapper.selectGroupId(groupName, fromUserId);
        if (groupId == null) {
            friendMapper.insertGroup(groupName, fromUserId);
            groupId = friendMapper.selectGroupId(groupName, fromUserId);
        }
        
        return friendMapper.updateFriendGroup(fromUserId, toUserId, groupId) > 0;
    }
}
