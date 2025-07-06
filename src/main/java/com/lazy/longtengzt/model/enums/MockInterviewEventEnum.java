package com.lazy.longtengzt.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;

/**
 * @Description: 模拟面试事件类型枚举
 * @Author Lazy
 * @Date 2025/07/03 16:49
 * @Version 1.0
 */
public enum MockInterviewEventEnum {

    START("开始", "start"),
    CHAT("聊天", "chat"),
    END("结束", "end");


    // 事件描述
    private final String text;
    // 事件值
    private final String value;


    /**
     * @Description: 该枚举类构造器
     * @Author Lazy
     * @Date 2025/07/03 16:52
     * @Version 1.0
     */

    MockInterviewEventEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有事件的值列表
     *
     * @Date 2025/07/03 16:54
     * @Return java.util.List<java.lang.Integer>
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据值（value）获取枚举
     *
     * @Date 2025/07/03 16:57
     * @Param value
     * @Return com.lazy.longtengzt.model.enums.MockInterviewStatusEnum
     */
    public static MockInterviewEventEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MockInterviewEventEnum anEnum : MockInterviewEventEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
