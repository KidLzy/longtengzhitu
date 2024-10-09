package com.lazy.longtengzt.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lazy.longtengzt.model.entity.Post;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.QuestionFavour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lazy.longtengzt.model.entity.User;

/**
* 问题收藏服务
 * @author Lzy
* @description 针对表【question_favour(题目收藏)】的数据库操作Service
* @createDate 2024-10-04 23:07:05
*/
public interface QuestionFavourService extends IService<QuestionFavour> {

    /**
     * 问题收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionFavour(long questionId, User loginUser);

    /**
     * 分页获取用户收藏的问题列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
                                        long favourUserId);

    /**
     * 问题收藏（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionFavourInner(long userId, long questionId);
}
