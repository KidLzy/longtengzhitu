package com.lazy.longtengzt.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lazy.longtengzt.model.entity.Post;
import com.lazy.longtengzt.model.entity.Question;
import com.lazy.longtengzt.model.entity.QuestionFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 题目收藏数据库操作
 * @author Lzy
 * @description 针对表【question_favour(题目收藏)】的数据库操作Mapper
 * @createDate 2024-10-04 23:07:05
 * @Entity com.lazy.longtengzt.model.entity.QuestionFavour
 */
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {

    /**
     * 分页查询收藏题目列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper, long favourUserId);
}




