package com.lazy.longtengzt.model.enums;

import cn.hutool.core.util.ObjectUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;

/**
 * @Description: 模拟面试状态枚举
 * @Author Lazy
 * @Date 2025/07/03 16:49
 * @Version 1.0
 */
public enum MockInterviewStatusEnum {

    TO_START("待开始", 0),
    IN_PROGRESS("进行中", 1),
    ENDED("已结束", 2);

    private final String text;
    private final int value;


    /**
     * @Description: 该枚举类构造器
     * @Author Lazy
     * @Date 2025/07/03 16:52
     * @Version 1.0
     */

    MockInterviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @Date 2025/07/03 16:54
     * @Return java.util.List<java.lang.Integer>
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据值（value）获取枚举
     *
     * @Date 2025/07/03 16:57
     * @Param value
     * @Return com.lazy.longtengzt.model.enums.MockInterviewStatusEnum
     */
    public static MockInterviewStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MockInterviewStatusEnum anEnum : MockInterviewStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
