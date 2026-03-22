package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.Doumail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 豆邮（私信）数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface DoumailMapper {

    /**
     * 根据ID查询豆邮
     * @param doumailId 豆邮ID
     * @return 豆邮对象
     */
    Doumail selectById(@Param("doumailId") Integer doumailId);

    /**
     * 查询两个用户之间的聊天记录
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 豆邮列表
     */
    List<Doumail> selectChatHistory(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    /**
     * 查询用户的所有聊天会话列表
     * @param userId 用户ID
     * @return 豆邮列表
     */
    List<Doumail> selectChatList(@Param("userId") Integer userId);

    /**
     * 查询用户收到的未读消息
     * @param userId 用户ID
     * @return 豆邮列表
     */
    List<Doumail> selectUnreadByUserId(@Param("userId") Integer userId);

    /**
     * 插入新豆邮
     * @param doumail 豆邮对象
     * @return 影响行数
     */
    int insert(Doumail doumail);

    /**
     * 标记消息为已读
     * @param doumailId 豆邮ID
     * @return 影响行数
     */
    int markAsRead(@Param("doumailId") Integer doumailId);

    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsRead(@Param("userId") Integer userId);

    /**
     * 删除豆邮（更新状态）
     * @param doumailId 豆邮ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("doumailId") Integer doumailId, @Param("status") Integer status);

    /**
     * 查询未读消息数
     * @param userId 用户ID
     * @return 未读消息数
     */
    int countUnread(@Param("userId") Integer userId);

}
