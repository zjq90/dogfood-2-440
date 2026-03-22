package com.idouban.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 好友关系实体类
 * 对应数据库表 friend
 * 
 * @author iDouban Team
 */
@Data
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 好友关系ID
     */
    private Integer friendId;

    /**
     * 发起用户ID
     */
    private Integer fromUserId;

    /**
     * 接收用户ID
     */
    private Integer toUserId;

    /**
     * 关系状态：1->A关注B；2->AB为好友（双向关注）；3->B被A拉黑
     */
    private Integer status;

    /**
     * 好友分组ID
     */
    private Integer groupId;

    /**
     * 对方用户信息（非数据库字段）
     */
    private User friendUser;
}
