package com.lazy.longtengzt.model.dto.questionFavour;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 * @author Lazy
 * @create 2024-10-04 23:15
 */
@Data
public class QuestionFavourAddRequest  implements Serializable {

    /**
     * 问题 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
