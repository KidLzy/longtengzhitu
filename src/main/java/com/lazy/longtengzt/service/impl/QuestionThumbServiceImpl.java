package com.lazy.longtengzt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.QuestionThumb;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.service.QuestionService;
import com.lazy.longtengzt.service.QuestionThumbService;
import com.lazy.longtengzt.mapper.QuestionThumbMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 题目点赞服务实现
 *
 * @author Lzy
 * @description 针对表【question_thumb(题目点赞)】的数据库操作Service实现
 * @createDate 2024-10-04 23:07:05
 */
@Service
public class QuestionThumbServiceImpl extends ServiceImpl<QuestionThumbMapper, QuestionThumb>
        implements QuestionThumbService {

    @Resource
    private QuestionService questionService;

    /**
     * 点赞
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    @Override
    public int doQuestionThumb(long questionId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionThumbService questionThumbService = (QuestionThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionThumbService.doQuestionThumbInner(userId, questionId);
        }
    }

    /**
     * 封装了事务的方法
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    public int doQuestionThumbInner(long userId, long questionId) {
        QuestionThumb questionThumb = new QuestionThumb();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        QueryWrapper<QuestionThumb> thumbQueryWrapper = new QueryWrapper<>(questionThumb);
        QuestionThumb oldQuestionThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0; // -1 表示取消点赞
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionThumb);
            if (result) {
                // 点赞数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0; // 1 表示点赞
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




