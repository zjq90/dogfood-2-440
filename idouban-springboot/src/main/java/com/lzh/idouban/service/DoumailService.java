package com.lzh.idouban.service;

import com.lzh.idouban.entity.Doumail;

import java.util.List;

/**
 * 豆邮（私信）服务接口
 * @author 林泽鸿
 */
public interface DoumailService {

    /**
     * 发送豆邮
     * @param doumail 豆邮对象
     * @return 是否成功
     */
    boolean sendDoumail(Doumail doumail);

    /**
     * 获取两个用户之间的聊天记录
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 豆邮列表
     */
    List<Doumail> getChatHistory(Integer userId1, Integer userId2);

    /**
     * 获取用户的聊天会话列表
     * @param userId 用户ID
     * @return 豆邮列表
     */
    List<Doumail> getChatList(Integer userId);

    /**
     * 获取用户的未读消息
     * @param userId 用户ID
     * @return 豆邮列表
     */
    List<Doumail> getUnreadMessages(Integer userId);

    /**
     * 标记消息为已读
     * @param doumailId 豆邮ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Integer doumailId, Integer userId);

    /**
     * 标记所有消息为已读
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAllAsRead(Integer userId);

    /**
     * 删除豆邮
     * @param doumailId 豆邮ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteDoumail(Integer doumailId, Integer userId);

    /**
     * 获取未读消息数
     * @param userId 用户ID
     * @return 未读消息数
     */
    int getUnreadCount(Integer userId);

}
