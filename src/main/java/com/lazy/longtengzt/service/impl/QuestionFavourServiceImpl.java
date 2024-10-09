package com.lazy.longtengzt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.QuestionFavour;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.service.QuestionFavourService;
import com.lazy.longtengzt.mapper.QuestionFavourMapper;
import com.lazy.longtengzt.service.QuestionService;
import com.lazy.longtengzt.service.QuestionThumbService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author Lzy
* @description 针对表【question_favour(题目收藏)】的数据库操作Service实现
* @createDate 2024-10-04 23:07:05
*/
@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour>
    implements QuestionFavourService{

    @Resource
    private QuestionService questionService;

    /**
     * 问题收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    @Override
    public int doQuestionFavour(long questionId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionFavourService questionFavourService = (QuestionFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionFavourService.doQuestionFavourInner(userId, questionId);
        }
    }

    /**
     * 分页获取用户收藏的问题列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourQuestionByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 问题收藏（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(long userId, long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);

        QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
        QuestionFavour oldQuestionFavour = this.getOne(questionFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldQuestionFavour != null) {
            result = this.remove(questionFavourQueryWrapper);
            if(result){
                result = questionService.update()
                        .eq("id",questionId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(questionFavour);
            if(result){
                // 帖子收藏数 + 1
                result = questionService.update()
                        .eq("id",questionId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




