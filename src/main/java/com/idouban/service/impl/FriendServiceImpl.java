package com.idouban.service.impl;

import com.idouban.dto.PageResult;
import com.idouban.dto.Result;
import com.idouban.entity.Friend;
import com.idouban.mapper.FriendMapper;
import com.idouban.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 好友关系服务实现类
 * 
 * @author iDouban Team
 */
@Service
public class FriendServiceImpl implements FriendService {

    private static final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendMapper friendMapper;

    /**
     * 关注用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> follow(Integer fromUserId, Integer toUserId) {
        logger.info("用户关注，fromUserId：{}，toUserId：{}", fromUserId, toUserId);

        // 参数校验
        if (fromUserId == null || toUserId == null) {
            return Result.fail("用户ID不能为空");
        }
        if (fromUserId.equals(toUserId)) {
            return Result.fail("不能关注自己");
        }

        // 检查是否已关注
        Friend existRelation = friendMapper.selectRelation(fromUserId, toUserId);
        if (existRelation != null && existRelation.getStatus() != null && existRelation.getStatus() == 3) {
            // 已拉黑，先取消拉黑再关注
            friendMapper.deleteFriend(fromUserId, toUserId);
        } else if (existRelation != null && existRelation.getStatus() != null && existRelation.getStatus() >= 1) {
            return Result.fail("已关注该用户");
        }

        // 新增关注关系
        Friend friend = new Friend();
        friend.setFromUserId(fromUserId);
        friend.setToUserId(toUserId);
        friend.setStatus(1); // 单向关注

        int result = friendMapper.insertFriend(friend);

        // 检查是否双向关注，如果是则更新双方状态为好友
        Friend reverseRelation = friendMapper.selectRelation(toUserId, fromUserId);
        if (reverseRelation != null && reverseRelation.getStatus() != null && reverseRelation.getStatus() == 1) {
            // 更新双方状态为好友
            Friend updateFriend = new Friend();
            updateFriend.setFromUserId(fromUserId);
            updateFriend.setToUserId(toUserId);
            updateFriend.setStatus(2);
            friendMapper.updateFriendStatus(updateFriend);

            Friend updateReverseFriend = new Friend();
            updateReverseFriend.setFromUserId(toUserId);
            updateReverseFriend.setToUserId(fromUserId);
            updateReverseFriend.setStatus(2);
            friendMapper.updateFriendStatus(updateReverseFriend);
        }

        logger.info("关注成功，fromUserId：{}，toUserId：{}", fromUserId, toUserId);
        return Result.success("关注成功", true);
    }

    /**
     * 取消关注
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> unfollow(Integer fromUserId, Integer toUserId) {
        logger.info("取消关注，fromUserId：{}，toUserId：{}", fromUserId, toUserId);

        // 参数校验
        if (fromUserId == null || toUserId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 检查关系状态
        Friend relation = friendMapper.selectRelation(fromUserId, toUserId);
        if (relation == null || relation.getStatus() == null || relation.getStatus() == 0) {
            return Result.fail("未关注该用户");
        }

        // 如果是好友状态（双向关注），需要将对方状态改为单向关注
        if (relation.getStatus() == 2) {
            Friend reverseFriend = new Friend();
            reverseFriend.setFromUserId(toUserId);
            reverseFriend.setToUserId(fromUserId);
            reverseFriend.setStatus(1);
            friendMapper.updateFriendStatus(reverseFriend);
        }

        // 删除关注关系
        int result = friendMapper.deleteFriend(fromUserId, toUserId);

        logger.info("取消关注成功，fromUserId：{}，toUserId：{}", fromUserId, toUserId);
        return Result.success("取消关注成功", true);
    }

    /**
     * 拉黑用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> block(Integer fromUserId, Integer toUserId) {
        logger.info("拉黑用户，fromUserId：{}，toUserId：{}", fromUserId, toUserId);

        // 参数校验
        if (fromUserId == null || toUserId == null) {
            return Result.fail("用户ID不能为空");
        }
        if (fromUserId.equals(toUserId)) {
            return Result.fail("不能拉黑自己");
        }

        // 检查是否已拉黑
        Friend existRelation = friendMapper.selectRelation(fromUserId, toUserId);
        if (existRelation != null && existRelation.getStatus() != null && existRelation.getStatus() == 3) {
            return Result.fail("已拉黑该用户");
        }

        // 删除原有关注关系（如果有），然后拉黑
        friendMapper.deleteFriend(fromUserId, toUserId);

        Friend friend = new Friend();
        friend.setFromUserId(fromUserId);
        friend.setToUserId(toUserId);
        friend.setStatus(3); // 拉黑

        int result = friendMapper.insertFriend(friend);

        logger.info("拉黑成功，fromUserId：{}，toUserId：{}", fromUserId, toUserId);
        return Result.success("拉黑成功", true);
    }

    /**
     * 取消拉黑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> unblock(Integer fromUserId, Integer toUserId) {
        logger.info("取消拉黑，fromUserId：{}，toUserId：{}", fromUserId, toUserId);

        // 参数校验
        if (fromUserId == null || toUserId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 检查是否拉黑
        Friend relation = friendMapper.selectRelation(fromUserId, toUserId);
        if (relation == null || relation.getStatus() == null || relation.getStatus() != 3) {
            return Result.fail("未拉黑该用户");
        }

        int result = friendMapper.deleteFriend(fromUserId, toUserId);

        logger.info("取消拉黑成功，fromUserId：{}，toUserId：{}", fromUserId, toUserId);
        return Result.success("取消拉黑成功", true);
    }

    /**
     * 获取关注列表
     */
    @Override
    public Result<PageResult<Friend>> getFollowingList(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("获取关注列表，用户ID：{}，页码：{}", userId, pageNum);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Friend> followingList = friendMapper.selectFollowingList(userId, offset, pageSize);
        Integer count = friendMapper.selectFollowingCount(userId);
        Long total = count != null ? count.longValue() : 0L;

        PageResult<Friend> pageResult = new PageResult<>(total, pageNum, pageSize, followingList);
        return Result.success(pageResult);
    }

    /**
     * 获取粉丝列表
     */
    @Override
    public Result<PageResult<Friend>> getFollowerList(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("获取粉丝列表，用户ID：{}，页码：{}", userId, pageNum);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Friend> followerList = friendMapper.selectFollowerList(userId, offset, pageSize);
        Integer count = friendMapper.selectFollowerCount(userId);
        Long total = count != null ? count.longValue() : 0L;

        PageResult<Friend> pageResult = new PageResult<>(total, pageNum, pageSize, followerList);
        return Result.success(pageResult);
    }

    /**
     * 获取好友列表（双向关注）
     */
    @Override
    public Result<PageResult<Friend>> getFriendList(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("获取好友列表，用户ID：{}，页码：{}", userId, pageNum);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Friend> friendList = friendMapper.selectFriendList(userId, offset, pageSize);
        Integer count = friendMapper.selectFriendCount(userId);
        Long total = count != null ? count.longValue() : 0L;

        PageResult<Friend> pageResult = new PageResult<>(total, pageNum, pageSize, friendList);
        return Result.success(pageResult);
    }

    /**
     * 获取黑名单列表
     */
    @Override
    public Result<List<Friend>> getBlacklist(Integer userId) {
        logger.info("获取黑名单列表，用户ID：{}", userId);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        List<Friend> blacklist = friendMapper.selectBlacklist(userId);
        return Result.success(blacklist);
    }

    /**
     * 检查是否已关注
     */
    @Override
    public Result<Boolean> isFollowing(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return Result.success(false);
        }
        Integer count = friendMapper.checkIsFollowing(fromUserId, toUserId);
        boolean isFollowing = count != null && count > 0;
        return Result.success(isFollowing);
    }

    /**
     * 获取关注数
     */
    @Override
    public Result<Integer> getFollowingCount(Integer userId) {
        if (userId == null) {
            return Result.success(0);
        }
        Integer count = friendMapper.selectFollowingCount(userId);
        return Result.success(count != null ? count : 0);
    }

    /**
     * 获取粉丝数
     */
    @Override
    public Result<Integer> getFollowerCount(Integer userId) {
        if (userId == null) {
            return Result.success(0);
        }
        Integer count = friendMapper.selectFollowerCount(userId);
        return Result.success(count != null ? count : 0);
    }

    /**
     * 获取好友数
     */
    @Override
    public Result<Integer> getFriendCount(Integer userId) {
        if (userId == null) {
            return Result.success(0);
        }
        Integer count = friendMapper.selectFriendCount(userId);
        return Result.success(count != null ? count : 0);
    }
}
