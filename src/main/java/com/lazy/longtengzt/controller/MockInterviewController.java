package com.lazy.longtengzt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lazy.longtengzt.common.BaseResponse;
import com.lazy.longtengzt.common.DeleteRequest;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.common.ResultUtils;
import com.lazy.longtengzt.constant.UserConstant;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.exception.ThrowUtils;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewAddRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewEventRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewQueryRequest;
import com.lazy.longtengzt.model.entity.MockInterview;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.service.MockInterviewService;
import com.lazy.longtengzt.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: longtengzhitu-backend
 * @description: 模拟面试接口
 * @author: Lazy
 * @create: 2025-07-03 20:32
 **/
@RestController
@RequestMapping("/mockInterview")
@Slf4j
public class MockInterviewController {

    @Resource
    private MockInterviewService mockInterviewService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建模拟面试
     *
     * @Date 2025/07/03 20:42
     * @Param mockInterviewAddRequest
     * @Param request
     * @Return com.lazy.longtengzt.common.BaseResponse<java.lang.Long>
     */
    @PostMapping("/add")
    public BaseResponse<Long> addMockInterview(
        @RequestBody MockInterviewAddRequest mockInterviewAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(mockInterviewAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 调用 Service 创建模拟面试
        Long mockInterviewId =
            mockInterviewService.createMockInterview(mockInterviewAddRequest, loginUser);
        return ResultUtils.success(mockInterviewId);
    }

    /**
     * 删除模拟面试
     *
     * @Date 2025/07/03 20:46
     * @Param deleteRequest
     * @Param request
     * @Return com.lazy.longtengzt.common.BaseResponse<java.lang.Boolean>
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMockInterview(@RequestBody DeleteRequest deleteRequest,
                                                     HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();

        // 判断是否存在
        MockInterview oldMockInterview = mockInterviewService.getById(id);
        ThrowUtils.throwIf(oldMockInterview == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅本人或管理员可删除
        if (!oldMockInterview.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 操作数据库
        boolean result = mockInterviewService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据id获取模拟面试（封装类）
     *
     * @Date 2025/07/03 20:48
     * @Param id
     * @Param request
     * @Return com.lazy.longtengzt.common.BaseResponse<com.lazy.longtengzt.model.entity.MockInterview>
     */
    @GetMapping("/get")
    public BaseResponse<MockInterview> getMockInterviewById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        MockInterview mockInterview = mockInterviewService.getById(id);
        ThrowUtils.throwIf(mockInterview == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(mockInterview);
    }

    /**
     * 分页获取当前登录用户创建的模拟面试列表
     *
     * @Date 2025/07/03 20:53
     * @Param mockInterviewQueryRequest
     * @Param request
     * @Return com.lazy.longtengzt.common.BaseResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.lazy.longtengzt.model.entity.MockInterview>>
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<MockInterview>> listMyMockInterviewVOByPage(
        @RequestBody MockInterviewQueryRequest mockInterviewQueryRequest,
        HttpServletRequest request) {
        ThrowUtils.throwIf(mockInterviewQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long size = mockInterviewQueryRequest.getPageSize();
        long current = mockInterviewQueryRequest.getCurrent();
        long pageSize = mockInterviewQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 限制只能获取本人的
        User loginUser = userService.getLoginUser(request);
        mockInterviewQueryRequest.setUserId(loginUser.getId());

        // 查询数据库
        Page<MockInterview> queryPage = new Page<>(current, pageSize);
        Page<MockInterview> mockInterviewPage = mockInterviewService.page(queryPage,
            mockInterviewService.getQueryWrapper(mockInterviewQueryRequest));
        // 封装类
        return ResultUtils.success(mockInterviewPage);
    }

    /**
     * 分页获取模拟面试列表（仅管理员可用）
     *
     * @param mockInterviewQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<MockInterview>> listMockInterviewByPage(
        @RequestBody MockInterviewQueryRequest mockInterviewQueryRequest) {
        ThrowUtils.throwIf(mockInterviewQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = mockInterviewQueryRequest.getCurrent();
        long pageSize = mockInterviewQueryRequest.getPageSize();
        // 查询数据库
        Page<MockInterview> queryPage = new Page<>(current, pageSize);
        Page<MockInterview> mockInterviewPage = mockInterviewService.page(queryPage,
            mockInterviewService.getQueryWrapper(mockInterviewQueryRequest));
        return ResultUtils.success(mockInterviewPage);
    }

    // endregion

    @PostMapping("/handleEvent")
    public BaseResponse<String> handleMockInterviewEvent(
        @RequestBody MockInterviewEventRequest mockInterviewEventRequest,
        HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 调用 Service 处理模拟面试事件
        String aiResponse =
            mockInterviewService.handleMockInterviewEvent(mockInterviewEventRequest, loginUser);
        // 返回 AI 的回复
        return ResultUtils.success(aiResponse);
    }

}
