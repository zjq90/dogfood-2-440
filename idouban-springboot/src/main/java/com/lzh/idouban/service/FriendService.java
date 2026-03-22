package com.lzh.idouban.service;

import com.lzh.idouban.entity.Friend;

import java.util.List;

/**
 * 好友关系服务接口
 * @author 林泽鸿
 */
public interface FriendService {

    /**
     * 关注用户
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return 是否成功
     */
    boolean follow(Integer fromUserId, Integer toUserId);

    /**
     * 取消关注
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return 是否成功
     */
    boolean unfollow(Integer fromUserId, Integer toUserId);

    /**
     * 拉黑用户
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return 是否成功
     */
    boolean blacklist(Integer fromUserId, Integer toUserId);

    /**
     * 取消拉黑
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return 是否成功
     */
    boolean unblacklist(Integer fromUserId, Integer toUserId);

    /**
     * 获取关注列表
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> getFollowingList(Integer userId);

    /**
     * 获取粉丝列表
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> getFollowerList(Integer userId);

    /**
     * 获取好友列表（双向关注）
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> getFriendList(Integer userId);

    /**
     * 获取黑名单
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> getBlacklist(Integer userId);

    /**
     * 检查是否已关注
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return true-已关注
     */
    boolean isFollowing(Integer fromUserId, Integer toUserId);

    /**
     * 检查是否是好友（双向关注）
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return true-是好友
     */
    boolean isFriend(Integer userId1, Integer userId2);

    /**
     * 检查是否被拉黑
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return true-已拉黑
     */
    boolean isBlacklisted(Integer fromUserId, Integer toUserId);

    /**
     * 获取关注数
     * @param userId 用户ID
     * @return 关注数
     */
    int getFollowingCount(Integer userId);

    /**
     * 获取粉丝数
     * @param userId 用户ID
     * @return 粉丝数
     */
    int getFollowerCount(Integer userId);

    /**
     * 获取好友数
     * @param userId 用户ID
     * @return 好友数
     */
    int getFriendCount(Integer userId);

}
