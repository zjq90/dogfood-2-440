package com.lzh.idouban.common;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * @author 林泽鸿
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 是否有下一页
     */
    private Boolean hasNextPage;

    /**
     * 是否有上一页
     */
    private Boolean hasPreviousPage;

    public PageResult() {
    }

    public PageResult(Integer pageNum, Integer pageSize, Long total, Integer pages, 
                      List<T> list, Boolean hasNextPage, Boolean hasPreviousPage) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
        this.list = list;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
    }

    /**
     * 从 PageInfo 构建分页结果
     */
    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        return new PageResult<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getPages(),
                pageInfo.getList(),
                pageInfo.isHasNextPage(),
                pageInfo.isHasPreviousPage()
        );
    }

    /**
     * 构建空分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(1, 10, 0L, 0, null, false, false);
    }

}
