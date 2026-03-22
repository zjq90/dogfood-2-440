package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 好友关系数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface FriendMapper {

    /**
     * 根据ID查询好友关系
     * @param friendId 好友关系ID
     * @return 好友关系对象
     */
    Friend selectById(@Param("friendId") Integer friendId);

    /**
     * 查询两个用户之间的好友关系
     * @param fromUserId 发起用户ID
     * @param toUserId 目标用户ID
     * @return 好友关系对象
     */
    Friend selectByUserIds(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);

    /**
     * 查询用户的关注列表
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> selectFollowingByUserId(@Param("userId") Integer userId);

    /**
     * 查询用户的粉丝列表
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> selectFollowersByUserId(@Param("userId") Integer userId);

    /**
     * 查询用户的好友列表（双向关注）
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> selectFriendsByUserId(@Param("userId") Integer userId);

    /**
     * 查询用户的黑名单
     * @param userId 用户ID
     * @return 好友关系列表
     */
    List<Friend> selectBlacklistByUserId(@Param("userId") Integer userId);

    /**
     * 插入新好友关系
     * @param friend 好友关系对象
     * @return 影响行数
     */
    int insert(Friend friend);

    /**
     * 更新好友关系状态
     * @param friendId 好友关系ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("friendId") Integer friendId, @Param("status") Integer status);

    /**
     * 删除好友关系
     * @param friendId 好友关系ID
     * @return 影响行数
     */
    int deleteById(@Param("friendId") Integer friendId);

    /**
     * 查询关注数
     * @param userId 用户ID
     * @return 关注数
     */
    int countFollowing(@Param("userId") Integer userId);

    /**
     * 查询粉丝数
     * @param userId 用户ID
     * @return 粉丝数
     */
    int countFollowers(@Param("userId") Integer userId);

    /**
     * 查询好友数
     * @param userId 用户ID
     * @return 好友数
     */
    int countFriends(@Param("userId") Integer userId);

}
