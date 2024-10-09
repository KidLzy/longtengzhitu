package com.lazy.longtengzt.model.dto.questionThumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 问题收藏 / 取消收藏请求
 * @author Lazy
 * @create 2024-10-04 23:18
 */
@Data
public class QuestionThumbAddRequest implements Serializable {

    /**
     * 问题 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
