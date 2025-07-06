package com.lazy.longtengzt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewAddRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewEventRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewQueryRequest;
import com.lazy.longtengzt.model.entity.MockInterview;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lazy.longtengzt.model.entity.User;

/**
* @author Lzy
* @description 针对表【mock_interview(模拟面试)】的数据库操作Service
* @createDate 2025-07-02 20:03:12
*/
public interface MockInterviewService extends IService<MockInterview> {

    /**
     * 创建模拟面试
     * @Date 2025/07/03 17:24
     * @Param mockInterviewAddRequest
     * @Param loginUser
     * @Return java.lang.Long
     */
    Long createMockInterview(MockInterviewAddRequest mockInterviewAddRequest, User loginUser);

    /**
     * 构造查询条件
     * @Date 2025/07/03 17:25
     * @Param mockInterviewQueryRequest
     * @Return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.lazy.longtengzt.model.entity.MockInterview>
     */
    QueryWrapper<MockInterview> getQueryWrapper(MockInterviewQueryRequest mockInterviewQueryRequest);

    /**
     * 处理模拟面试事件
     * @Date 2025/07/03 17:26
     * @Param mockInterviewEventRequest
     * @Param loginUser
     * @Return java.lang.String
     */
    String handleMockInterviewEvent(MockInterviewEventRequest mockInterviewEventRequest, User loginUser);

}
