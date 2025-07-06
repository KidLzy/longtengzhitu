package com.lazy.longtengzt.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lazy.longtengzt.common.ErrorCode;
import com.lazy.longtengzt.constant.CommonConstant;
import com.lazy.longtengzt.exception.BusinessException;
import com.lazy.longtengzt.exception.ThrowUtils;
import com.lazy.longtengzt.manager.AiManager;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewAddRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewChatMessage;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewEventRequest;
import com.lazy.longtengzt.model.dto.mockinterview.MockInterviewQueryRequest;
import com.lazy.longtengzt.model.entity.MockInterview;
import com.lazy.longtengzt.model.entity.User;
import com.lazy.longtengzt.model.enums.MockInterviewEventEnum;
import com.lazy.longtengzt.model.enums.MockInterviewStatusEnum;
import com.lazy.longtengzt.service.MockInterviewService;
import com.lazy.longtengzt.mapper.MockInterviewMapper;
import com.lazy.longtengzt.utils.SqlUtils;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lzy
 * @description 针对表【mock_interview(模拟面试)】的数据库操作Service实现
 * @createDate 2025-07-02 20:03:12
 */
@Service
public class MockInterviewServiceImpl extends ServiceImpl<MockInterviewMapper, MockInterview>
    implements MockInterviewService {

    @Resource
    private AiManager aiManager;

    /**
     * 创建模拟面试
     *
     * @param mockInterviewAddRequest
     * @param loginUser
     * @Date 2025/07/03 17:24
     * @Param mockInterviewAddRequest
     * @Param loginUser
     * @Return java.lang.Long
     */
    @Override
    public Long createMockInterview(MockInterviewAddRequest mockInterviewAddRequest,
                                    User loginUser) {

        // 1. 参数校验
        if (mockInterviewAddRequest == null || loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String workExperience = mockInterviewAddRequest.getWorkExperience();
        String jobPosition = mockInterviewAddRequest.getJobPosition();
        String difficulty = mockInterviewAddRequest.getDifficulty();
        ThrowUtils.throwIf(StrUtil.hasBlank(workExperience, jobPosition, difficulty),
            ErrorCode.PARAMS_ERROR, "参数错误");
        // 2. 封装插入到数据库中的对象
        MockInterview mockInterview = new MockInterview();
        mockInterview.setWorkExperience(workExperience);
        mockInterview.setJobPosition(jobPosition);
        mockInterview.setDifficulty(difficulty);
        mockInterview.setUserId(loginUser.getId());
        mockInterview.setStatus(MockInterviewStatusEnum.TO_START.getValue());

        // 3. 插入数据库
        boolean result = this.save(mockInterview);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建失败");

        // 4. 返回 id
        return mockInterview.getId();
    }

    /**
     * 构造查询条件
     *
     * @param mockInterviewQueryRequest
     * @Date 2025/07/03 17:25
     * @Param mockInterviewQueryRequest
     * @Return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.lazy.longtengzt.model.entity.MockInterview>
     */
    @Override
    public QueryWrapper<MockInterview> getQueryWrapper(
        MockInterviewQueryRequest mockInterviewQueryRequest) {
        // 1. 构造查询条件
        QueryWrapper<MockInterview> queryWrapper = new QueryWrapper<>();
        // 2. 判断是否为空
        if (mockInterviewQueryRequest == null) {
            return queryWrapper;
        }
        // 3. 从对象中取值
        Long id = mockInterviewQueryRequest.getId();
        String workExperience = mockInterviewQueryRequest.getWorkExperience();
        String jobPosition = mockInterviewQueryRequest.getJobPosition();
        String difficulty = mockInterviewQueryRequest.getDifficulty();
        Integer status = mockInterviewQueryRequest.getStatus();
        Long userId = mockInterviewQueryRequest.getUserId();
        String sortField = mockInterviewQueryRequest.getSortField();
        String sortOrder = mockInterviewQueryRequest.getSortOrder();

        // 4. 补充添加查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(workExperience), "workExperience", workExperience);
        queryWrapper.like(StringUtils.isNotBlank(jobPosition), "jobPosition", jobPosition);
        queryWrapper.like(StringUtils.isNotBlank(difficulty), "difficulty", difficulty);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
            sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
            sortField);
        return queryWrapper;
    }

    /**
     * 处理模拟面试事件
     *
     * @param mockInterviewEventRequest
     * @param loginUser
     * @Date 2025/07/03 17:26
     * @Param mockInterviewEventRequest
     * @Param loginUser
     * @Return java.lang.String
     */
    @Override
    public String handleMockInterviewEvent(MockInterviewEventRequest mockInterviewEventRequest,
                                           User loginUser) {
        // 区分事件
        Long id = mockInterviewEventRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        MockInterview mockInterview = this.getById(id);
        ThrowUtils.throwIf(mockInterview == null, ErrorCode.NOT_FOUND_ERROR, "模拟面试未创建");
        // 如果不是本人创建的模拟面试，报错
        if (!mockInterview.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取事件枚举值
        String event = mockInterviewEventRequest.getEvent();
        MockInterviewEventEnum eventEnum = MockInterviewEventEnum.getEnumByValue(event);

        // 根据事件类型处理
        switch (eventEnum) {
            case START:
                // -- 处理 开始面试 事件
                // 用户进入模拟面试，发送“开始”事件，修改模拟面试的状态为“已开始”，AI要给出对应的回复
                return handleChatStartEvent(mockInterview);
            case CHAT:
                // -- 处理 AI 对话事件
                // 用户可以和AI面试官 发送消息，发送消息事件，系统上要发送的消息内容，AI要给出对应的回复
                return handleChatMessageEvent(mockInterviewEventRequest, mockInterview);
            case END:
                // -- 处理结束事件
                // 退出模拟面试，发送“退出”事件，AI 给出面试的复盘总结，修改状态为“已结束”
                return handleChatEndEvent(mockInterview);
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
    }

    /**
     * 处理 AI 对话开始事件
     *
     * @Date 2025/07/03 19:13
     * @Param mockInterview
     * @Return java.lang.String
     */
    private String handleChatStartEvent(MockInterview mockInterview) {
        // 构造消息列表
        // 定义 AI 的 Prompt
        String systemPrompt = String.format(
            "角色设定：你是一名严厉的资深程序员面试官，正在面试应聘【%s岗位】的候选人（工作经验：%s）。面试难度：%s。\n\n" +
                "核心流程与规则：\n" +
                "1. 启动条件\n" +
                "   - 当候选人回复「开始」时，立即启动面试流程\n" +
                "   - 每次仅提1个技术问题（总问题数≤20）\n\n" +
                "2. 终止条件（满足任意一条立即结束）\n" +
                "   - 候选人明确表示「结束面试」\n" +
                "   - 候选人出现以下情况：\n" +
                "     • 技术能力明显不符合岗位要求\n" +
                "     • 工作年限不满足招聘需求\n" +
                "     • 出现不礼貌/不专业言行\n" +
                "   - 回复中必须包含标识符【面试结束】\n\n" +
                "3. 交互要求\n" +
                "   - 全程保持真人面试官口吻，可适当：\n" +
                "     • 对回答进行追问/引导（如：\"能详细解释这个设计决策吗？\"）\n" +
                "     • 表达态度（如：\"这个理解有偏差\"、\"思路不错\"）\n\n" +
                "4. 结束动作\n" +
                "   - 终止后立即生成评估报告，包含：\n" +
                "     • 技术能力分析\n" +
                "     • 沟通表现评价\n" +
                "     • 岗位匹配度总结",
            mockInterview.getJobPosition(),
            mockInterview.getWorkExperience(),
            mockInterview.getDifficulty()
        );
        String userPrompt = "开始";
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage =
            ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage =
            ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);
        // 调用AI获取结果
        String answer = aiManager.doChat(messages);
        ChatMessage assistantMessage =
            ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
        messages.add(assistantMessage);
        // 保存消息记录，并且更新状态
        List<MockInterviewChatMessage> chatMessageList = transformFromChatMessage(messages);
        String jsonStr = JSONUtil.toJsonStr(chatMessageList);
        // 操作数据库进行更新
        MockInterview updateMockInterview = new MockInterview();
        updateMockInterview.setStatus(MockInterviewStatusEnum.IN_PROGRESS.getValue());
        updateMockInterview.setId(mockInterview.getId());
        updateMockInterview.setMessages(jsonStr);
        boolean result = this.updateById(updateMockInterview);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "更新失败");
        return answer;
    }

    /**
     * 处理 AI 对话消息事件
     *
     * @Date 2025/07/03 20:16
     * @Param mockInterviewEventRequest
     * @Param mockInterview
     * @Return java.lang.String
     */
    private String handleChatMessageEvent(MockInterviewEventRequest mockInterviewEventRequest,
                                          MockInterview mockInterview) {
        String message = mockInterviewEventRequest.getMessage();
        // 构造消息列表，注意需要先获取之前的消息记录
        String historyMessage = mockInterview.getMessages();
        // 转换成消息记录对象
        List<MockInterviewChatMessage> historyMessageList =
            JSONUtil.parseArray(historyMessage).toList(MockInterviewChatMessage.class);
        // 构造消息列表
        final List<ChatMessage> chatMessages = transformToChatMessage(historyMessageList);
        final ChatMessage chatUserMessage =
            ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
        chatMessages.add(chatUserMessage);

        // 调用AI获取结果
        String chatAnswer = aiManager.doChat(chatMessages);
        ChatMessage chatAssistantMessage =
            ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(chatAnswer).build();
        chatMessages.add(chatAssistantMessage);
        // 保存消息记录，并且更新状态
        List<MockInterviewChatMessage> mockInterviewChatMessages =
            transformFromChatMessage(chatMessages);
        String newJsonStr = JSONUtil.toJsonStr(mockInterviewChatMessages);
        // 创建新模拟面试对象进行更新
        MockInterview newUpdateMockInterview = new MockInterview();
        newUpdateMockInterview.setId(mockInterview.getId());
        newUpdateMockInterview.setMessages(newJsonStr);
        // 如果 AI 主动结束了面试，更改状态
        if (chatAnswer.contains("【面试结束】")) {
            newUpdateMockInterview.setStatus(MockInterviewStatusEnum.ENDED.getValue());
        }
        // 操作数据库进行
        boolean newResult = this.updateById(newUpdateMockInterview);
        ThrowUtils.throwIf(!newResult, ErrorCode.SYSTEM_ERROR, "更新失败");
        return chatAnswer;
    }

    /**
     * 处理 AI 对话结束事件
     *
     * @Date 2025/07/03 20:24
     * @Param mockInterview
     * @Return java.lang.String
     */
    private String handleChatEndEvent(MockInterview mockInterview) {
        // 构建消息列表，注意需要先获取之前的消息记录
        String historyMessage = mockInterview.getMessages();
        List<MockInterviewChatMessage> historyMessageList =
            JSONUtil.parseArray(historyMessage).toList(MockInterviewChatMessage.class);

        final List<ChatMessage> chatMessages = transformToChatMessage(historyMessageList);
        // 构造用户结束消息
        String endUserPrompt = "结束";
        final ChatMessage endUserMessage =
            ChatMessage.builder().role(ChatMessageRole.USER).content(endUserPrompt).build();
        chatMessages.add(endUserMessage);

        // 调用AI获取结果
        String endAnswer = aiManager.doChat(chatMessages);
        ChatMessage endAssistantMessage =
            ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(endAnswer).build();
        chatMessages.add(endAssistantMessage);
        // 保存消息记录，并且更新状态
        List<MockInterviewChatMessage> mockInterviewChatMessages =
            transformFromChatMessage(chatMessages);
        String newJsonStr = JSONUtil.toJsonStr(mockInterviewChatMessages);

        MockInterview newUpdateMockInterview = new MockInterview();
        newUpdateMockInterview.setStatus(MockInterviewStatusEnum.ENDED.getValue());
        newUpdateMockInterview.setId(mockInterview.getId());
        newUpdateMockInterview.setMessages(newJsonStr);
        boolean newResult = this.updateById(newUpdateMockInterview);
        ThrowUtils.throwIf(!newResult, ErrorCode.SYSTEM_ERROR, "更新失败");
        return endAnswer;
    }


    /**
     * 消息记录对象转换
     *
     * @Date 2025/07/03 18:51
     * @Param chatMessageList
     * @Return java.util.List<com.lazy.longtengzt.model.dto.mockinterview.MockInterviewChatMessage>
     */
    List<MockInterviewChatMessage> transformFromChatMessage(List<ChatMessage> chatMessageList) {
        return chatMessageList.stream().map(chatMessage -> {
            MockInterviewChatMessage mockInterviewChatMessage = new MockInterviewChatMessage();
            mockInterviewChatMessage.setRole(chatMessage.getRole().value());
            mockInterviewChatMessage.setMessage(chatMessage.getContent().toString());
            return mockInterviewChatMessage;
        }).collect(Collectors.toList());
    }

    /**
     * 消息记录对象转换
     *
     * @Date 2025/07/03 18:36
     * @Param chatMessageList
     * @Return java.util.List<com.volcengine.ark.runtime.model.completion.chat.ChatMessage>
     */
    List<ChatMessage> transformToChatMessage(List<MockInterviewChatMessage> chatMessageList) {
        return chatMessageList.stream().map(chatMessage -> {
            ChatMessage tempChatMessage = ChatMessage.builder()
                .role(ChatMessageRole.valueOf(StringUtils.upperCase(chatMessage.getRole())))
                .content(chatMessage.getMessage()).build();
            return tempChatMessage;
        }).collect(Collectors.toList());
    }
}




