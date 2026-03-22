package com.idouban.mapper;

import com.idouban.model.Friend;
import com.idouban.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 好友关系Mapper接口
 */
@Mapper
public interface FriendMapper {

    /**
     * 添加好友关系
     */
    int insert(Friend friend);

    /**
     * 更新好友关系状态
     */
    int updateStatus(@Param("fromUserId") Integer fromUserId,
                     @Param("toUserId") Integer toUserId,
                     @Param("status") Integer status);

    /**
     * 删除好友关系
     */
    int delete(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);

    /**
     * 查询好友关系
     */
    Friend selectRelation(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);

    /**
     * 查询用户的好友列表
     */
    List<User> selectFriendsByPage(@Param("userId") Integer userId,
                                    @Param("status") Integer status,
                                    @Param("offset") int offset,
                                    @Param("pageSize") int pageSize);

    /**
     * 查询用户好友总数
     */
    int selectFriendCount(@Param("userId") Integer userId, @Param("status") Integer status);

    /**
     * 模糊搜索好友列表
     */
    List<User> selectSearchFriendsByPage(@Param("userId") Integer userId,
                                          @Param("status") Integer status,
                                          @Param("searchContent") String searchContent,
                                          @Param("offset") int offset,
                                          @Param("pageSize") int pageSize);

    /**
     * 模糊搜索好友总数
     */
    int selectSearchFriendCount(@Param("userId") Integer userId,
                                 @Param("status") Integer status,
                                 @Param("searchContent") String searchContent);

    /**
     * 检查是否已关注
     */
    int checkFollow(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);

    /**
     * 添加好友分组
     */
    int insertGroup(@Param("groupName") String groupName, @Param("userId") Integer userId);

    /**
     * 检查分组是否存在
     */
    int checkGroupExist(@Param("groupName") String groupName, @Param("userId") Integer userId);

    /**
     * 获取分组ID
     */
    Integer selectGroupId(@Param("groupName") String groupName, @Param("userId") Integer userId);

    /**
     * 更新好友分组
     */
    int updateFriendGroup(@Param("fromUserId") Integer fromUserId,
                          @Param("toUserId") Integer toUserId,
                          @Param("groupId") Integer groupId);

    /**
     * 查询分组内好友列表
     */
    List<User> selectFriendsByGroup(@Param("userId") Integer userId,
                                     @Param("groupId") Integer groupId,
                                     @Param("offset") int offset,
                                     @Param("pageSize") int pageSize);

    /**
     * 查询分组内好友总数
     */
    int selectFriendCountByGroup(@Param("userId") Integer userId, @Param("groupId") Integer groupId);
}
