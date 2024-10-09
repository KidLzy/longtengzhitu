package com.lazy.longtengzt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lazy.longtengzt.common.BaseResponse;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.common.ResultUtils;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.exception.ThrowUtils;
import com.lazy.longtengzt.model.dto.question.QuestionQueryRequest;
import com.lazy.longtengzt.model.dto.questionFavour.QuestionFavourAddRequest;
import com.lazy.longtengzt.model.dto.questionFavour.QuestionFavourQueryRequest;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.model.vo.QuestionVO;
import com.lazy.longtengzt.service.QuestionFavourService;
import com.lazy.longtengzt.service.QuestionService;
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
 * @create 2024-10-05 0:01
 */
@RestController
@RequestMapping("/question_favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞题目
     *
     * @param questionFavourAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest questionFavourAddRequest,
                                                  HttpServletRequest request) {
        if (questionFavourAddRequest == null || questionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionFavourAddRequest.getQuestionId();
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 查询当前登录用户点赞的题目列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                   HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest), loginUser.getId());
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 查询用户点赞的题目列表
     *
     * @param questionFavourQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionVO>> listFavourQuestionPage(@RequestBody QuestionFavourQueryRequest questionFavourQueryRequest,
                                                                 HttpServletRequest request) {
        if (questionFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionFavourQueryRequest.getCurrent();
        long size = questionFavourQueryRequest.getPageSize();
        Long userId = questionFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionFavourQueryRequest.getQuestionQueryRequest()), userId);
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

}
