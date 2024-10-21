package com.lazy.longtengzt.sentinel;

/**
 * Sentinel 限流熔断常量
 * @author Lazy
 * @create 2024-10-21 16:29
 */
public interface SentinelConstant {
    /**
     * 分页获取题库列表接口限流
     */
    String listQuestionBankVOByPage = "listQuestionBankVOByPage";

    /**
     * 分页获取题目列表接口限流
     */
    String listQuestionVOByPage = "listQuestionVOByPage";
}
