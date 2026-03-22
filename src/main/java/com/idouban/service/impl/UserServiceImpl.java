package com.idouban.service.impl;

import com.idouban.dto.Result;
import com.idouban.entity.User;
import com.idouban.mapper.UserMapper;
import com.idouban.service.UserService;
import com.idouban.util.MD5Util;
import com.idouban.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 用户服务实现类
 * 
 * @author iDouban Team
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    @Override
    public Result<User> login(String username, String password) {
        logger.info("用户登录尝试，用户名：{}", username);

        // 验证参数
        if (ValidateUtil.isInvalidUserName(username)) {
            logger.warn("登录失败：用户名为空");
            return Result.fail("用户名不能为空");
        }
        if (ValidateUtil.isInvalidPassword(password)) {
            logger.warn("登录失败：密码为空");
            return Result.fail("密码不能为空");
        }

        // MD5加密密码
        String encryptedPassword = MD5Util.MD5Encode(password, "utf8");

        // 查询用户
        User user = userMapper.selectForLogin(username, encryptedPassword);
        if (user == null) {
            // 检查用户名是否存在
            User existUser = userMapper.selectByUsername(username);
            if (existUser == null) {
                logger.warn("登录失败：用户不存在，用户名：{}", username);
                return Result.fail("该用户不存在");
            } else {
                logger.warn("登录失败：密码错误，用户名：{}", username);
                return Result.fail("密码错误");
            }
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 2) {
            logger.warn("登录失败：用户已被封禁，用户名：{}", username);
            return Result.fail("该账号已被封禁");
        }

        // 清除敏感信息
        user.setPassword(null);
        user.setCode(null);
        user.setOutTime(null);

        logger.info("登录成功，用户名：{}", username);
        return Result.success(user.getStatus() == 1 ? "管理员登录成功" : "用户登录成功", user);
    }

    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<User> register(String username, String password) {
        logger.info("用户注册尝试，用户名：{}", username);

        // 验证参数
        if (ValidateUtil.isInvalidUserName(username)) {
            return Result.fail("用户名不能为空");
        }
        if (ValidateUtil.isInvalidPassword(password)) {
            return Result.fail("密码不能为空");
        }
        if (!ValidateUtil.isEmail(username)) {
            return Result.fail("用户名格式不正确，请使用邮箱注册");
        }

        // 检查用户名是否已存在
        if (isUsernameExists(username)) {
            logger.warn("注册失败：用户名重复，用户名：{}", username);
            return Result.fail("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.MD5Encode(password, "utf8"));
        user.setNickname("用户" + System.currentTimeMillis());

        int result = userMapper.insertUser(user);
        if (result > 0) {
            logger.info("注册成功，用户名：{}", username);
            user.setPassword(null);
            return Result.success("注册成功", user);
        } else {
            logger.error("注册失败，用户名：{}", username);
            return Result.fail("注册失败，请稍后重试");
        }
    }

    /**
     * 检查用户名是否已存在
     */
    @Override
    public boolean isUsernameExists(String username) {
        User user = userMapper.selectByUsername(username);
        return user != null;
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 根据用户ID获取用户信息
     */
    @Override
    public User getUserByUserId(Integer userId) {
        User user = userMapper.selectByUserId(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 更新用户个人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updatePersonage(User user) {
        logger.info("更新用户信息，用户ID：{}", user.getUserId());
        int result = userMapper.updatePersonage(user);
        if (result > 0) {
            logger.info("用户信息更新成功，用户ID：{}", user.getUserId());
            return Result.success("更新成功", true);
        } else {
            logger.warn("用户信息更新失败，用户ID：{}", user.getUserId());
            return Result.fail("更新失败");
        }
    }

    /**
     * 更新用户头像
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updatePortrait(Integer userId, String portrait) {
        logger.info("更新用户头像，用户ID：{}", userId);
        int result = userMapper.updatePortrait(userId, portrait);
        if (result > 0) {
            logger.info("用户头像更新成功，用户ID：{}", userId);
            return Result.success("头像更新成功", true);
        } else {
            logger.warn("用户头像更新失败，用户ID：{}", userId);
            return Result.fail("头像更新失败");
        }
    }

    /**
     * 生成找回密码凭证
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> generateResetCode(String username) {
        logger.info("生成找回密码凭证，用户名：{}", username);

        // 检查用户是否存在
        if (!isUsernameExists(username)) {
            return Result.fail("该邮箱未注册");
        }

        // 生成随机验证码
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);

        // 设置过期时间（30分钟后）
        Timestamp outTime = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);

        User user = new User();
        user.setUsername(username);
        user.setCode(code);
        user.setOutTime(outTime);

        int result = userMapper.updateCodeAndOutTime(user);
        if (result > 0) {
            logger.info("找回密码凭证生成成功，用户名：{}，凭证码：{}", username, code);
            return Result.success("凭证已发送到您的邮箱", code);
        } else {
            return Result.fail("生成凭证失败，请稍后重试");
        }
    }

    /**
     * 验证找回密码凭证
     */
    @Override
    public Result<User> verifyResetCode(Integer code) {
        logger.info("验证找回密码凭证，凭证码：{}", code);
        User user = userMapper.selectByCode(code);
        if (user != null) {
            user.setPassword(null);
            return Result.success("验证成功", user);
        } else {
            return Result.fail("凭证无效或已过期");
        }
    }

    /**
     * 重置密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> resetPassword(Integer code, String newPassword) {
        logger.info("重置密码，凭证码：{}", code);

        // 验证凭证
        User user = userMapper.selectByCode(code);
        if (user == null) {
            return Result.fail("凭证无效或已过期");
        }

        // 更新密码
        String encryptedPassword = MD5Util.MD5Encode(newPassword, "utf8");
        int result = userMapper.updatePassword(user.getUserId(), encryptedPassword);
        if (result > 0) {
            logger.info("密码重置成功，用户ID：{}", user.getUserId());
            return Result.success("密码重置成功", true);
        } else {
            logger.error("密码重置失败，用户ID：{}", user.getUserId());
            return Result.fail("密码重置失败");
        }
    }

    /**
     * 修改密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> changePassword(Integer userId, String oldPassword, String newPassword) {
        logger.info("修改密码，用户ID：{}", userId);

        // 验证旧密码
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }

        String encryptedOldPassword = MD5Util.MD5Encode(oldPassword, "utf8");
        if (!user.getPassword().equals(encryptedOldPassword)) {
            logger.warn("修改密码失败：旧密码错误，用户ID：{}", userId);
            return Result.fail("旧密码错误");
        }

        // 更新新密码
        String encryptedNewPassword = MD5Util.MD5Encode(newPassword, "utf8");
        int result = userMapper.updatePassword(userId, encryptedNewPassword);
        if (result > 0) {
            logger.info("密码修改成功，用户ID：{}", userId);
            return Result.success("密码修改成功", true);
        } else {
            logger.error("密码修改失败，用户ID：{}", userId);
            return Result.fail("密码修改失败");
        }
    }

    /**
     * 搜索用户
     */
    @Override
    public Result<List<User>> searchUsers(String keyword) {
        logger.info("搜索用户，关键词：{}", keyword);
        List<User> users = userMapper.searchUsers(keyword);
        // 清除敏感信息
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    /**
     * 获取所有用户（管理员功能）
     */
    @Override
    public Result<List<User>> getAllUsers() {
        logger.info("获取所有用户列表");
        List<User> users = userMapper.selectAllUsers();
        // 清除敏感信息
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    /**
     * 更新用户状态（封禁/解封）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Integer userId, Integer status) {
        logger.info("更新用户状态，用户ID：{}，状态：{}", userId, status);
        int result = userMapper.updateStatus(userId, status);
        if (result > 0) {
            return Result.success("状态更新成功", true);
        } else {
            return Result.fail("状态更新失败");
        }
    }

    /**
     * 举报用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> reportUser(Integer userId) {
        logger.info("举报用户，用户ID：{}", userId);
        int result = userMapper.updateReported(userId, 1);
        if (result > 0) {
            return Result.success("举报成功", true);
        } else {
            return Result.fail("举报失败");
        }
    }
}
