package com.lzh.idouban.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章标签实体类 - 对应数据库 a_tag 表
 * @author 林泽鸿
 */
@Data
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Integer tagId;

    /** 标签名称 */
    private String tagName;

}
