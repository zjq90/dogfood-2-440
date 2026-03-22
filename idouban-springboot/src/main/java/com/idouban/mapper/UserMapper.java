package com.idouban.mapper;

import com.idouban.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 用户登录验证
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户ID查询用户信息
     */
    User selectByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户名查询用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 用户注册
     */
    int insert(User user);

    /**
     * 更新用户信息
     */
    int update(User user);

    /**
     * 更新用户头像
     */
    int updatePortrait(@Param("userId") Integer userId, @Param("portrait") String portrait);

    /**
     * 更新用户密码
     */
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    /**
     * 分页查询所有用户
     */
    List<User> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询用户总数
     */
    int selectCount();

    /**
     * 模糊搜索用户总数
     */
    int selectSearchCount(@Param("searchContent") String searchContent);

    /**
     * 模糊搜索用户列表
     */
    List<User> selectSearchByPage(@Param("searchContent") String searchContent, 
                                   @Param("offset") int offset, 
                                   @Param("pageSize") int pageSize);

    /**
     * 检查用户名是否存在
     */
    int checkUsername(@Param("username") String username);
}
