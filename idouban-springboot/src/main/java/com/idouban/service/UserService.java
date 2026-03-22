package com.idouban.service;

import com.idouban.common.BusinessException;
import com.idouban.mapper.UserMapper;
import com.idouban.model.Page;
import com.idouban.model.User;
import com.idouban.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户服务类
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        log.info("用户登录请求, username: {}", username);
        String encryptedPassword = MD5Util.encrypt(password);
        User user = userMapper.login(username, encryptedPassword);
        if (user == null) {
            log.warn("登录失败, 用户名或密码错误, username: {}", username);
            throw new BusinessException("用户名或密码错误");
        }
        log.info("用户登录成功, userId: {}", user.getUserId());
        return user;
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        log.info("用户注册请求, username: {}", user.getUsername());
        
        int count = userMapper.checkUsername(user.getUsername());
        if (count > 0) {
            log.warn("注册失败, 用户名已存在, username: {}", user.getUsername());
            throw new BusinessException("用户名已存在");
        }
        
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setStatus(0);
        user.setReported(0);
        user.setNickname(user.getUsername().split("@")[0]);
        
        userMapper.insert(user);
        log.info("用户注册成功, userId: {}", user.getUserId());
        return user;
    }

    /**
     * 根据用户ID查询用户信息
     */
    public User getUserById(Integer userId) {
        log.debug("查询用户信息, userId: {}", userId);
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    /**
     * 更新用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        log.info("更新用户信息, userId: {}", user.getUserId());
        int result = userMapper.update(user);
        return result > 0;
    }

    /**
     * 更新用户头像
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePortrait(Integer userId, String portrait) {
        log.info("更新用户头像, userId: {}", userId);
        int result = userMapper.updatePortrait(userId, portrait);
        return result > 0;
    }

    /**
     * 更新用户密码
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword) {
        log.info("更新用户密码, userId: {}", userId);
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!user.getPassword().equals(MD5Util.encrypt(oldPassword))) {
            throw new BusinessException("原密码错误");
        }
        int result = userMapper.updatePassword(userId, MD5Util.encrypt(newPassword));
        return result > 0;
    }

    /**
     * 分页查询用户列表
     */
    public Page<User> getUserList(int currentPage, int pageSize) {
        log.debug("分页查询用户列表, currentPage: {}, pageSize: {}", currentPage, pageSize);
        int offset = (currentPage - 1) * pageSize;
        List<User> users = userMapper.selectByPage(offset, pageSize);
        int totalCount = userMapper.selectCount();
        
        Page<User> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(users);
        return page;
    }

    /**
     * 搜索用户
     */
    public Page<User> searchUsers(String searchContent, int currentPage, int pageSize) {
        log.debug("搜索用户, searchContent: {}, currentPage: {}", searchContent, currentPage);
        int offset = (currentPage - 1) * pageSize;
        List<User> users = userMapper.selectSearchByPage(searchContent, offset, pageSize);
        int totalCount = userMapper.selectSearchCount(searchContent);
        
        Page<User> page = new Page<>();
        page.setPageSize(pageSize);
        page.setTotalCount(totalCount);
        page.setCurrentPage(currentPage);
        page.setObjects(users);
        return page;
    }

    /**
     * 检查用户名是否存在
     */
    public boolean checkUsernameExist(String username) {
        int count = userMapper.checkUsername(username);
        return count > 0;
    }
}
