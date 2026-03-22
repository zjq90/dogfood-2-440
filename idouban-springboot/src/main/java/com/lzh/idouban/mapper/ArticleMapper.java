package com.lzh.idouban.mapper;

import com.lzh.idouban.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章数据访问层
 * @author 林泽鸿
 */
@Mapper
public interface ArticleMapper {

    /**
     * 根据ID查询文章
     * @param articleId 文章ID
     * @return 文章对象
     */
    Article selectById(@Param("articleId") Integer articleId);

    /**
     * 查询文章列表（带作者信息）
     * @return 文章列表
     */
    List<Article> selectListWithAuthor();

    /**
     * 分页查询文章列表
     * @return 文章列表
     */
    List<Article> selectArticleList();

    /**
     * 查询用户的文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<Article> selectByUserId(@Param("userId") Integer userId);

    /**
     * 插入新文章
     * @param article 文章对象
     * @return 影响行数
     */
    int insert(Article article);

    /**
     * 更新文章
     * @param article 文章对象
     * @return 影响行数
     */
    int update(Article article);

    /**
     * 删除文章
     * @param articleId 文章ID
     * @return 影响行数
     */
    int deleteById(@Param("articleId") Integer articleId);

    /**
     * 增加浏览量
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementPageView(@Param("articleId") Integer articleId);

    /**
     * 增加点赞数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementStar(@Param("articleId") Integer articleId);

    /**
     * 减少点赞数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementStar(@Param("articleId") Integer articleId);

    /**
     * 增加评论数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementComment(@Param("articleId") Integer articleId);

    /**
     * 减少评论数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementComment(@Param("articleId") Integer articleId);

    /**
     * 增加收藏数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementCollection(@Param("articleId") Integer articleId);

    /**
     * 减少收藏数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementCollection(@Param("articleId") Integer articleId);

    /**
     * 增加转发数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementShare(@Param("articleId") Integer articleId);

    /**
     * 查询总记录数
     * @return 记录数
     */
    int selectTotalCount();

    /**
     * 查询用户的文章数
     * @param userId 用户ID
     * @return 记录数
     */
    int selectCountByUserId(@Param("userId") Integer userId);

    /**
     * 搜索文章
     * @param keyword 关键词
     * @return 文章列表
     */
    List<Article> searchByKeyword(@Param("keyword") String keyword);

}
