package com.idouban.mapper;

import com.idouban.model.Doumail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 豆邮(私信)Mapper接口
 */
@Mapper
public interface DoumailMapper {

    /**
     * 发送豆邮
     */
    int insert(Doumail doumail);

    /**
     * 查询两个用户之间的豆邮列表
     */
    List<Doumail> selectByUsers(@Param("userId") Integer userId,
                                 @Param("toUserId") Integer toUserId,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);

    /**
     * 查询两个用户之间的豆邮总数
     */
    int selectCountByUsers(@Param("userId") Integer userId, @Param("toUserId") Integer toUserId);

    /**
     * 查询用户的豆邮联系人列表
     */
    List<Doumail> selectContacts(@Param("userId") Integer userId);

    /**
     * 更新豆邮阅读状态
     */
    int updateReadStatus(@Param("doumailId") Integer doumailId);

    /**
     * 查询用户未读豆邮数量
     */
    int selectUnreadCount(@Param("userId") Integer userId);

    /**
     * 删除豆邮
     */
    int delete(@Param("doumailId") Integer doumailId);
}
