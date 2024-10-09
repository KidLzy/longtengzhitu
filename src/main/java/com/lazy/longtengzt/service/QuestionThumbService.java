package com.lazy.longtengzt.service;

import com.lazy.longtengzt.model.entity.QuestionThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lazy.longtengzt.model.entity.User;

/**
 * (题目点赞) 服务类
* @author Lzy
* @description 针对表【question_thumb(题目点赞)】的数据库操作Service
* @createDate 2024-10-04 23:07:05
*/
public interface QuestionThumbService extends IService<QuestionThumb> {

    /**
     * 点赞
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionThumb(long questionId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionThumbInner(long userId, long questionId);
}
