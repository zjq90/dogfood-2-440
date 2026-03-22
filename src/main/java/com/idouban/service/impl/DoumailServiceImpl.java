package com.idouban.service.impl;

import com.idouban.dto.PageResult;
import com.idouban.dto.Result;
import com.idouban.entity.Doumail;
import com.idouban.mapper.DoumailMapper;
import com.idouban.service.DoumailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 豆邮服务实现类
 * 
 * @author iDouban Team
 */
@Service
public class DoumailServiceImpl implements DoumailService {

    private static final Logger logger = LoggerFactory.getLogger(DoumailServiceImpl.class);

    @Autowired
    private DoumailMapper doumailMapper;

    @Override
    public Result<PageResult<Doumail>> getConversationList(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("获取会话列表，用户ID：{}，页码：{}", userId, pageNum);

        if (userId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Doumail> conversations = doumailMapper.selectConversationList(userId, offset, pageSize);
        Long total = doumailMapper.selectConversationCount(userId);

        PageResult<Doumail> pageResult = new PageResult<>(total, pageNum, pageSize, conversations);
        return Result.success(pageResult);
    }

    @Override
    public Result<PageResult<Doumail>> getDoumailDetail(Integer userId, Integer targetUserId, Integer pageNum, Integer pageSize) {
        logger.info("获取豆邮详情，用户ID：{}，对方用户ID：{}，页码：{}", userId, targetUserId, pageNum);

        if (userId == null || targetUserId == null) {
            return Result.fail("用户ID不能为空");
        }

        // 参数校验
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        int offset = (pageNum - 1) * pageSize;
        List<Doumail> doumails = doumailMapper.selectDoumailDetail(userId, targetUserId, offset, pageSize);
        Long total = doumailMapper.selectDoumailCount(userId, targetUserId);

        PageResult<Doumail> pageResult = new PageResult<>(total, pageNum, pageSize, doumails);
        return Result.success(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Doumail> sendDoumail(Doumail doumail) {
        logger.info("发送豆邮，发送者ID：{}，接收者ID：{}", doumail.getFromUserId(), doumail.getToUserId());

        if (doumail.getFromUserId() == null || doumail.getToUserId() == null) {
            return Result.fail("用户ID不能为空");
        }
        if (doumail.getFromUserId().equals(doumail.getToUserId())) {
            return Result.fail("不能给自己发送豆邮");
        }
        if (doumail.getChatMsg() == null || doumail.getChatMsg().trim().isEmpty()) {
            return Result.fail("豆邮内容不能为空");
        }

        int result = doumailMapper.insertDoumail(doumail);
        if (result > 0) {
            logger.info("豆邮发送成功，豆邮ID：{}", doumail.getDoumailId());
            return Result.success("发送成功", doumail);
        } else {
            return Result.fail("发送失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> markAsRead(Integer fromUserId, Integer toUserId) {
        logger.info("标记豆邮为已读，发送者ID：{}，接收者ID：{}", fromUserId, toUserId);

        if (fromUserId == null || toUserId == null) {
            return Result.fail("用户ID不能为空");
        }

        int result = doumailMapper.markAsRead(fromUserId, toUserId);
        return Result.success(result >= 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteDoumail(Integer doumailId, Integer userId) {
        logger.info("删除豆邮，豆邮ID：{}，操作人ID：{}", doumailId, userId);

        if (doumailId == null || userId == null) {
            return Result.fail("参数不能为空");
        }

        // 逻辑删除：状态1-发送者删除，状态2-接收者删除
        int result = doumailMapper.deleteDoumail(doumailId, userId, 1);
        if (result > 0) {
            logger.info("豆邮删除成功，豆邮ID：{}", doumailId);
            return Result.success("删除成功", true);
        } else {
            return Result.fail("删除失败");
        }
    }

    @Override
    public Result<Integer> getUnreadCount(Integer userId) {
        if (userId == null) {
            return Result.success(0);
        }
        Integer count = doumailMapper.selectUnreadCount(userId);
        return Result.success(count != null ? count : 0);
    }
}
