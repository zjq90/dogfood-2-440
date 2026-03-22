package com.lzh.idouban.service.impl;

import com.lzh.idouban.entity.Friend;
import com.lzh.idouban.mapper.FriendMapper;
import com.lzh.idouban.service.FriendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 好友关系服务实现类
 * @author 林泽鸿
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendMapper friendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean follow(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }

        // 不能关注自己
        if (fromUserId.equals(toUserId)) {
            log.warn("关注失败：不能关注自己");
            return false;
        }

        // 检查是否已存在关系
        Friend existing = friendMapper.selectByUserIds(fromUserId, toUserId);
        if (existing != null) {
            if (existing.getStatus() == 3) {
                // 如果已拉黑，先取消拉黑
                friendMapper.updateStatus(existing.getFriendId(), 1);
                log.info("关注成功（取消拉黑）：fromUserId={}, toUserId={}", fromUserId, toUserId);
                return true;
            }
            // 已关注
            log.warn("关注失败：已关注该用户");
            return false;
        }

        // 检查对方是否已关注我
        Friend reverse = friendMapper.selectByUserIds(toUserId, fromUserId);
        int status = (reverse != null) ? 2 : 1; // 2-双向好友，1-单向关注

        Friend friend = new Friend();
        friend.setFromUserId(fromUserId);
        friend.setToUserId(toUserId);
        friend.setStatus(status);

        int result = friendMapper.insert(friend);
        if (result > 0) {
            // 如果对方已关注我，更新对方状态为双向好友
            if (reverse != null) {
                friendMapper.updateStatus(reverse.getFriendId(), 2);
            }
            log.info("关注成功：fromUserId={}, toUserId={}, status={}", fromUserId, toUserId, status);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfollow(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }

        Friend friend = friendMapper.selectByUserIds(fromUserId, toUserId);
        if (friend == null) {
            log.warn("取消关注失败：未关注该用户");
            return false;
        }

        // 如果是双向好友，更新为单向关注
        if (friend.getStatus() == 2) {
            friendMapper.updateStatus(friend.getFriendId(), 1);
            // 更新对方状态为单向关注
            Friend reverse = friendMapper.selectByUserIds(toUserId, fromUserId);
            if (reverse != null) {
                friendMapper.updateStatus(reverse.getFriendId(), 1);
            }
            log.info("取消关注成功（变为单向）：fromUserId={}, toUserId={}", fromUserId, toUserId);
            return true;
        }

        // 删除关注关系
        int result = friendMapper.deleteById(friend.getFriendId());
        if (result > 0) {
            log.info("取消关注成功：fromUserId={}, toUserId={}", fromUserId, toUserId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean blacklist(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }

        // 不能拉黑自己
        if (fromUserId.equals(toUserId)) {
            log.warn("拉黑失败：不能拉黑自己");
            return false;
        }

        Friend existing = friendMapper.selectByUserIds(fromUserId, toUserId);
        if (existing != null) {
            // 更新状态为拉黑
            int result = friendMapper.updateStatus(existing.getFriendId(), 3);
            if (result > 0) {
                log.info("拉黑成功：fromUserId={}, toUserId={}", fromUserId, toUserId);
                return true;
            }
        } else {
            // 新建拉黑关系
            Friend friend = new Friend();
            friend.setFromUserId(fromUserId);
            friend.setToUserId(toUserId);
            friend.setStatus(3);
            int result = friendMapper.insert(friend);
            if (result > 0) {
                log.info("拉黑成功：fromUserId={}, toUserId={}", fromUserId, toUserId);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unblacklist(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }

        Friend friend = friendMapper.selectByUserIds(fromUserId, toUserId);
        if (friend == null || friend.getStatus() != 3) {
            log.warn("取消拉黑失败：未拉黑该用户");
            return false;
        }

        int result = friendMapper.deleteById(friend.getFriendId());
        if (result > 0) {
            log.info("取消拉黑成功：fromUserId={}, toUserId={}", fromUserId, toUserId);
            return true;
        }
        return false;
    }

    @Override
    public List<Friend> getFollowingList(Integer userId) {
        if (userId == null) {
            return null;
        }
        return friendMapper.selectFollowingByUserId(userId);
    }

    @Override
    public List<Friend> getFollowerList(Integer userId) {
        if (userId == null) {
            return null;
        }
        return friendMapper.selectFollowersByUserId(userId);
    }

    @Override
    public List<Friend> getFriendList(Integer userId) {
        if (userId == null) {
            return null;
        }
        return friendMapper.selectFriendsByUserId(userId);
    }

    @Override
    public List<Friend> getBlacklist(Integer userId) {
        if (userId == null) {
            return null;
        }
        return friendMapper.selectBlacklistByUserId(userId);
    }

    @Override
    public boolean isFollowing(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }
        Friend friend = friendMapper.selectByUserIds(fromUserId, toUserId);
        return friend != null && (friend.getStatus() == 1 || friend.getStatus() == 2);
    }

    @Override
    public boolean isFriend(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null) {
            return false;
        }
        Friend friend = friendMapper.selectByUserIds(userId1, userId2);
        return friend != null && friend.getStatus() == 2;
    }

    @Override
    public boolean isBlacklisted(Integer fromUserId, Integer toUserId) {
        if (fromUserId == null || toUserId == null) {
            return false;
        }
        Friend friend = friendMapper.selectByUserIds(fromUserId, toUserId);
        return friend != null && friend.getStatus() == 3;
    }

    @Override
    public int getFollowingCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        return friendMapper.countFollowing(userId);
    }

    @Override
    public int getFollowerCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        return friendMapper.countFollowers(userId);
    }

    @Override
    public int getFriendCount(Integer userId) {
        if (userId == null) {
            return 0;
        }
        return friendMapper.countFriends(userId);
    }

}
