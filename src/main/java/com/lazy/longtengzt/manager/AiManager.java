package com.lazy.longtengzt.manager;

import cn.hutool.core.collection.CollUtil;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.exception.BusinessException;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @program: longtengzhitu-backend
 * @description: 通用的AI调用类
 * @author: Lazy
 * @create: 2025-07-01 14:38
 **/
@Service
public class AiManager {

    @Resource
    private ArkService aiService;

    private final String DEFAULT_MODEL = "deepseek-v3-250324";

    /**
     * 调用 AI 接口，获取响应字符串
     *
     * @param userPrompt
     * @return
     */
    public String doChat(String userPrompt) {
        return doChat("", userPrompt, DEFAULT_MODEL);
    }

    /**
     * 调用 AI 接口，获取响应字符串
     * @Date 2025/07/01 16:16
     * @Param systemPrompt
     * @Param userPrompt
     * @Return java.lang.String
     */
    public String doChat(String systemPrompt, String userPrompt) {
        return doChat(systemPrompt, userPrompt, DEFAULT_MODEL);
    }

    /**
     * 调用 AI 接口，获取响应字符串（允许传入自定义的消息列表，使用默认模型）
     *
     * @Date 2025/07/01 16:13
     * @Param systemPrompt
     * @Param userPrompt
     * @Return java.lang.String
     */
    public String doChat(String systemPrompt, String userPrompt, String model) {
        // 构造消息列表
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage =
            ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage =
            ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();

        messages.add(systemMessage);
        messages.add(userMessage);

        return doChat(messages, model);
    }

    /**
     * 调用 AI 接口，获取响应字符串（允许传入自定义的消息列表，使用默认模型）
     *
     * @Date 2025/07/01 16:13
     * @Param messages
     * @Return java.lang.String
     */
    public String doChat(List<ChatMessage> messages) {
        return doChat(messages, DEFAULT_MODEL);
    }

    /**
     * 调用AI接口，获取响应字符串（允许传入自定义的消息列表）
     *
     * @Date 2025/07/01 16:09
     * @Param messages
     * @Param model
     * @Return java.lang.String
     */
    public String doChat(List<ChatMessage> messages, String model) {
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model(model)
            .messages(messages)
            .build();

        // 调用接口发送请求
        List<ChatCompletionChoice> choices =
            aiService.createChatCompletion(chatCompletionRequest).getChoices();
        if (CollUtil.isNotEmpty(choices)) {
            return (String) choices.get(0).getMessage().getContent();
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用AI失败,没有返回结果");
        // shutdown service after all requests is finished
        // aiService.shutdownExecutor();
    }

}
