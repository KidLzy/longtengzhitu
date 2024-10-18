"use server";
import Title from "antd/es/typography/Title";
import {Flex, Menu, message} from "antd";
import "./index.css";
import { getQuestionBankVoByIdUsingGet } from "@/api/questionBankController";
import React from "react";
import { getQuestionVoByIdUsingGet } from "@/api/questionController";
import Link from "next/link";
import Sider from "antd/es/layout/Sider";
import {Content} from "antd/es/layout/layout";
import QuestionCard from "@/components/QuestionCard";

/**
 * 题库题目详情页
 * @constructor
 */
export default async function BankQuestionPage({ params }) {
  const { questionBankId, questionId } = params;
  // 获取题库详情
  let bank = undefined;

  // 获取题库列表
  try {
    const res = await getQuestionBankVoByIdUsingGet({
      id: questionBankId,
      needQueryQuestionList: true,
      // 可以自行扩展未分页实现
      pageSize: 200,
    });
    bank = res.data as any;
  } catch (e) {
    message.error("获取题库详情失败，" + (e as any).message);
  }
  // 错误处理
  if (!bank) {
    return <div>获取题库详情失败，请刷新重试</div>;
  }

  // 获取题目详情
  let question = undefined;
  try {
    const res = await getQuestionVoByIdUsingGet({
      id: questionId,
    });
    question = res.data as any;
  } catch (e) {
    message.error("获取题目详情失败，" + (e as any).message);
  }
  // 错误处理
  if (!question) {
    return <div>获取题目详情失败，请刷新重试</div>;
  }
  // 题目菜单列表
  const questionMenuItemList = (bank.questionPage?.records || []).map((q) => {
    return {
      label: (
          <Link href={`/bank/${questionBankId}/question/${q.id}`}>{q.title}</Link>
      ),
      key: q.id,
    };
  });

  return (
      <div id="bankQuestionPage">
        <Flex gap={24}>
          <Sider width={240} theme="light" style={{padding: "24px 0"}}>
            <Title level={4} style={{padding: "0 20px"}}>
              {bank.title}
            </Title>
            <Menu items={questionMenuItemList} selectedKeys={[question.id]}/>
          </Sider>
          <Content>
            <QuestionCard question={question}/>
          </Content>
        </Flex>
      </div>
  );
}
