package com.lzh.idouban.service.impl;

import com.lzh.idouban.entity.User;
import com.lzh.idouban.mapper.UserMapper;
import com.lzh.idouban.service.UserService;
import com.lzh.idouban.util.MD5Util;
import com.lzh.idouban.util.ValidationUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 用户服务实现类
 * @author 林泽鸿
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        // 参数校验
        if (!ValidationUtil.isValidUsername(username)) {
            log.warn("登录失败：用户名为空");
            return null;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            log.warn("登录失败：密码为空");
            return null;
        }

        // 密码MD5加密
        String encryptedPassword = MD5Util.encrypt(password);

        // 查询用户
        User user = userMapper.login(username, encryptedPassword);
        if (user == null) {
            log.warn("登录失败：用户名或密码错误，username={}", username);
            return null;
        }

        log.info("用户登录成功：username={}", username);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(String username, String password) {
        // 参数校验
        if (!ValidationUtil.isValidUsername(username)) {
            log.warn("注册失败：用户名为空");
            return null;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            log.warn("注册失败：密码为空");
            return null;
        }

        // 检查用户名是否已存在
        if (userMapper.checkUsernameExists(username) > 0) {
            log.warn("注册失败：用户名已存在，username={}", username);
            return null;
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.encrypt(password));
        user.setStatus(0); // 普通用户
        user.setReported(0);
        user.setNickname("用户" + System.currentTimeMillis() % 10000);
        user.setPortrait("/images/default.png");

        int result = userMapper.insert(user);
        if (result > 0) {
            log.info("用户注册成功：username={}", username);
            return user;
        }

        log.error("用户注册失败：username={}", username);
        return null;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.checkUsernameExists(username) > 0;
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectByUserId(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }
        int result = userMapper.update(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePortrait(Integer userId, String portrait) {
        int result = userMapper.updatePortrait(userId, portrait);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer generateResetCode(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("生成找回密码验证码失败：用户不存在，username={}", username);
            return null;
        }

        // 生成6位随机验证码
        Integer code = new Random().nextInt(900000) + 100000;

        // 设置验证码和过期时间（24小时后过期）
        user.setCode(code);
        user.setOutTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        userMapper.updateResetPasswordInfo(user);

        log.info("生成找回密码验证码成功：username={}, code={}", username, code);
        return code;
    }

    @Override
    public User verifyResetCode(Integer code) {
        if (code == null) {
            return null;
        }

        User user = userMapper.selectByCode(code);
        if (user == null) {
            log.warn("验证找回密码验证码失败：验证码不存在，code={}", code);
            return null;
        }

        // 检查验证码是否过期
        if (user.getOutTime() == null || user.getOutTime().before(new Timestamp(System.currentTimeMillis()))) {
            log.warn("验证找回密码验证码失败：验证码已过期，code={}", code);
            return null;
        }

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Integer userId, String newPassword) {
        if (!ValidationUtil.isValidPassword(newPassword)) {
            return false;
        }

        String encryptedPassword = MD5Util.encrypt(newPassword);
        int result = userMapper.updatePassword(userId, encryptedPassword);
        return result > 0;
    }

    @Override
    public List<User> searchUsers(String keyword) {
        if (!ValidationUtil.isValidKeyword(keyword)) {
            return null;
        }
        return userMapper.selectByKeyword(keyword);
    }

}
