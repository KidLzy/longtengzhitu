package com.lazy.longtengzt.model.dto.mockinterview;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: longtengzhitu-backend
 * @description: 创建模拟面试请求
 * @author: Lazy
 * @create: 2025-07-03 16:37
 **/
@Data
public class MockInterviewAddRequest implements Serializable {
    /**
     * 工作年限
     */
    private String workExperience;

    /**
     * 工作岗位
     */
    private String jobPosition;

    /**
     * 面试难度
     */
    private String difficulty;

    private static final long serialVersionUID = 1L;
}
