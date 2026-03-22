package com.idouban.mapper;

import com.idouban.model.Article;
import com.idouban.model.ArticleList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 文章Mapper接口
 */
@Mapper
public interface ArticleMapper {

    /**
     * 根据文章ID查询文章详情
     */
    Article selectByArticleId(@Param("articleId") Integer articleId);

    /**
     * 新增文章
     */
    int insert(Article article);

    /**
     * 更新文章
     */
    int update(Article article);

    /**
     * 删除文章
     */
    int delete(@Param("articleId") Integer articleId);

    /**
     * 分页查询文章列表
     */
    List<ArticleList> selectArticleListByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 查询文章总数
     */
    int selectArticleCount();

    /**
     * 根据作者ID查询文章列表
     */
    List<ArticleList> selectByAuthorId(@Param("authorId") Integer authorId, 
                                        @Param("offset") int offset, 
                                        @Param("pageSize") int pageSize);

    /**
     * 根据作者ID查询文章总数
     */
    int selectCountByAuthorId(@Param("authorId") Integer authorId);

    /**
     * 模糊搜索文章列表
     */
    List<ArticleList> selectSearchByPage(@Param("searchContent") String searchContent,
                                          @Param("offset") int offset,
                                          @Param("pageSize") int pageSize);

    /**
     * 模糊搜索文章总数
     */
    int selectSearchCount(@Param("searchContent") String searchContent);

    /**
     * 更新文章点赞数
     */
    int updateStarNum(@Param("articleId") Integer articleId, @Param("num") int num);

    /**
     * 更新文章收藏数
     */
    int updateCollectionNum(@Param("articleId") Integer articleId, @Param("num") int num);

    /**
     * 更新文章评论数
     */
    int updateCommentNum(@Param("articleId") Integer articleId, @Param("num") int num);

    /**
     * 更新文章转发数
     */
    int updateShareNum(@Param("articleId") Integer articleId, @Param("num") int num);

    /**
     * 更新文章浏览量
     */
    int updatePageView(@Param("articleId") Integer articleId);

    /**
     * 查询用户收藏的文章列表
     */
    List<ArticleList> selectCollectionByPage(@Param("userId") Integer userId,
                                              @Param("offset") int offset,
                                              @Param("pageSize") int pageSize);

    /**
     * 查询用户收藏文章总数
     */
    int selectCollectionCount(@Param("userId") Integer userId);
}
