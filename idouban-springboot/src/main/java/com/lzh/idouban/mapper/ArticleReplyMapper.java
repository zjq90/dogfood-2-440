package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.ArticleReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论回复数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface ArticleReplyMapper {

    /**
     * 根据ID查询回复
     * @param replyId 回复ID
     * @return 回复对象
     */
    ArticleReply selectById(@Param("replyId") Integer replyId);

    /**
     * 查询评论的所有回复（带用户信息）
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<ArticleReply> selectByCommentId(@Param("commentId") Integer commentId);

    /**
     * 插入新回复
     * @param reply 回复对象
     * @return 影响行数
     */
    int insert(ArticleReply reply);

    /**
     * 删除回复
     * @param replyId 回复ID
     * @return 影响行数
     */
    int deleteById(@Param("replyId") Integer replyId);

    /**
     * 增加点赞数
     * @param replyId 回复ID
     * @return 影响行数
     */
    int incrementStar(@Param("replyId") Integer replyId);

    /**
     * 减少点赞数
     * @param replyId 回复ID
     * @return 影响行数
     */
    int decrementStar(@Param("replyId") Integer replyId);

}
