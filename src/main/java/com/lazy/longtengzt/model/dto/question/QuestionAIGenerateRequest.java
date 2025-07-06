package com.lazy.longtengzt.model.dto.question;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: longtengzhitu-backend
 * @description: AI 生成题目请求
 * @author: Lazy
 * @create: 2025-07-01 18:06
 **/
@Data
public class QuestionAIGenerateRequest implements Serializable {
    /**
     * 题目类型，比如 Java
     */
    private String questionType;

    /**
     * 题目数量，比如 10
     */
    private int number = 10;

    private static final long serialVersionUID = 1L;
}
