package com.lzh.idouban.service;

import com.lzh.idouban.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * @author 林泽鸿
 */
public interface UserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，登录失败返回null
     */
    User login(String username, String password);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 注册成功的用户对象
     */
    User register(String username, String password);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return true-存在，false-不存在
     */
    boolean checkUsernameExists(String username);

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUserById(Integer userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param portrait 头像URL
     * @return 是否成功
     */
    boolean updatePortrait(Integer userId, String portrait);

    /**
     * 生成找回密码验证码
     * @param username 用户名
     * @return 验证码
     */
    Integer generateResetCode(String username);

    /**
     * 验证找回密码验证码
     * @param code 验证码
     * @return 用户对象，验证失败返回null
     */
    User verifyResetCode(Integer code);

    /**
     * 重置密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Integer userId, String newPassword);

    /**
     * 搜索用户
     * @param keyword 关键词
     * @return 用户列表
     */
    List<User> searchUsers(String keyword);

}
