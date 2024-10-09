package com.lazy.longtengzt.service;

import com.lazy.longtengzt.common.ReviewRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 审核服务接口
 * @author Lazy
 * @create 2024-10-07 23:09
 */
public interface ReviewService {
    boolean doReview(Object reviewObject, ReviewRequest reviewRequest, HttpServletRequest request);
}
