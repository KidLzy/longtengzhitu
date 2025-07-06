package com.lazy.longtengzt.model.dto.mockinterview;

import java.io.Serializable;
import lombok.Data;

/**
 * @program: longtengzhitu-backend
 * @description: 模拟面试消息记录
 * @author: Lazy
 * @create: 2025-07-03 16:37
 **/
@Data
public class MockInterviewChatMessage implements Serializable {
    private static final long serialVersionUID = -2056799733159215147L;

    /**
     * 角色
     */
    private String role;

    /**
     * 消息内容
     */
    private String message;
}
