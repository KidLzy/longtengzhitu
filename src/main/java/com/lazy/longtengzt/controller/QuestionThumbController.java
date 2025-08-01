package com.lazy.longtengzt.controller;

import com.lazy.longtengzt.common.BaseResponse;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.common.ResultUtils;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.model.dto.questionThumb.QuestionThumbAddRequest;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.service.QuestionThumbService;
import com.lazy.longtengzt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目点赞接口
 *
 * @author Lazy
 * @create 2024-10-05 0:02
 */
@RestController
@RequestMapping("/question_thumb")
@Slf4j
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionThumbAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest questionThumbAddRequest,
                                         HttpServletRequest request){
        if(questionThumbAddRequest == null || questionThumbAddRequest.getQuestionId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionThumbAddRequest.getQuestionId();
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return ResultUtils.success(result);
    }
}
