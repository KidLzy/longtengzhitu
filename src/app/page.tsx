"use server";
import Title from "antd/es/typography/Title";
import {Divider, Flex, message} from "antd";
import "./index.css";
import { listQuestionBankVoByPageUsingPost } from "@/api/questionBankController";
import { listQuestionVoByPageUsingPost } from "@/api/questionController";
import Link from "next/link";
import React from "react";
import QuestionBankList from "@/components/QuestionBankList";
import QuestionList from "@/components/QuestionList";

// 本页面使用服务端渲染，禁用静态生成
// export const dynamic = 'force-dynamic';

/**
 * 主页
 * @constructor
 */
export default async function HomePage() {
  let questionBankList = [];
  let questionList = [];

  // 获取题库列表
  try {
    const questionBankRes = (await listQuestionBankVoByPageUsingPost({
      pageSize: 12,
      sortField: "createTime",
      sortOrder: "descend",
    })) as any;
    questionBankList = questionBankRes.data.records ?? [];
  } catch (e) {
    message.error("获取题库列表失败，" + (e as any).message);
  }

  // 获取题目列表
  try {
    const questionListRes = (await listQuestionVoByPageUsingPost({
      pageSize: 12,
      sortField: "createTime",
      sortOrder: "descend",
    })) as any;
    questionList = questionListRes.data.records ?? [];
  } catch (e) {
    message.error("获取题目列表失败，" + (e as any).message);
  }

  return (
    <div id="homePage" className="max-width-content">
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题库</Title>
        <Link href={"/banks"}>查看更多</Link>
      </Flex>
      <QuestionBankList questionBankList={questionBankList} />
      <Divider />
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题目</Title>
        <Link href={"/questions"}>查看更多</Link>
      </Flex>
      <QuestionList questionList={questionList} />
    </div>
  );
}
