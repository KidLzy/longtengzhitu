package com.lazy.longtengzt.service.impl;

import com.lazy.longtengzt.annotation.AuthCheck;
import com.lazy.longtengzt.common.BaseResponse;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.common.ResultUtils;
import com.lazy.longtengzt.common.ReviewRequest;
import com.lazy.longtengzt.constant.UserConstant;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.exception.ThrowUtils;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.QuestionBank;
import com.lazy.longtengzt.model.enums.ReviewStatusEnum;
import com.lazy.longtengzt.service.QuestionBankService;
import com.lazy.longtengzt.service.QuestionService;
import com.lazy.longtengzt.service.ReviewService;
import com.lazy.longtengzt.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 审核服务实现类
 * @author Lazy
 * @create 2024-10-07 23:13
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private UserService userService;

    @Override
    public boolean doReview(Object reviewObject, ReviewRequest reviewRequest, HttpServletRequest request) {
        // 审核
        if (reviewObject instanceof Question) {
            return reviewQuestion((Question) reviewObject, reviewRequest, request);
        } else if (reviewObject instanceof QuestionBank) {
            return reviewQuestionBank((QuestionBank) reviewObject, reviewRequest, request);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    private boolean reviewQuestion(Question oldQuestion, ReviewRequest reviewRequest, HttpServletRequest request) {
        Long id = reviewRequest.getId();
        Integer reviewStatus = reviewRequest.getReviewStatus();

        // 已是该状态
        if (oldQuestion.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        Question question = new Question();
        question.setId(id);
        question.setReviewStatus(reviewStatus);
        question.setReviewMessage(reviewRequest.getReviewMessage());
        question.setReviewerId(userService.getLoginUser(request).getId());
        question.setReviewTime(new Date());
        boolean result = questionService.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    private boolean reviewQuestionBank(QuestionBank oldQuestionBank, ReviewRequest reviewRequest, HttpServletRequest request) {
        Long id = reviewRequest.getId();
        Integer reviewStatus = reviewRequest.getReviewStatus();

        // 已是该状态
        if (oldQuestionBank.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        QuestionBank questionBank = new QuestionBank();
        questionBank.setId(id);
        questionBank.setReviewStatus(reviewStatus);
        questionBank.setReviewMessage(reviewRequest.getReviewMessage());
        questionBank.setReviewerId(userService.getLoginUser(request).getId());
        questionBank.setReviewTime(new Date());

        boolean result = questionBankService.updateById(questionBank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }


}
