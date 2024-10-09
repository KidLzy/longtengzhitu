package com.lazy.longtengzt.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 审核请求类
 * @author Lazy
 * @create 2024-10-07 22:35
 */
@Data
public class ReviewRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;


    private static final long serialVersionUID = 1L;
}