package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象
     */
    User selectByUserId(@Param("userId") Integer userId);

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 用户对象
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回1，不存在返回0
     */
    int checkUsernameExists(@Param("username") String username);

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响行数
     */
    int update(User user);

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param portrait 头像URL
     * @return 影响行数
     */
    int updatePortrait(@Param("userId") Integer userId, @Param("portrait") String portrait);

    /**
     * 更新找回密码信息
     * @param user 用户对象
     * @return 影响行数
     */
    int updateResetPasswordInfo(User user);

    /**
     * 根据code查询用户
     * @param code 验证码
     * @return 用户对象
     */
    User selectByCode(@Param("code") Integer code);

    /**
     * 更新密码
     * @param userId 用户ID
     * @param password 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> selectAll();

    /**
     * 根据条件查询用户列表
     * @param keyword 关键词
     * @return 用户列表
     */
    List<User> selectByKeyword(@Param("keyword") String keyword);

}
