package com.lazy.longtengzt.model.dto.mockinterview;

import com.lazy.longtengzt.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: longtengzhitu-backend
 * @description: 模拟面试事件请求
 * @author: Lazy
 * @create: 2025-07-03 16:37
 **/
@Data
public class MockInterviewEventRequest implements Serializable {
    /**
     * 事件类型
     */
    private String event;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 房间 ID
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
