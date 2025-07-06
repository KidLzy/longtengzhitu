"use server";
import Title from "antd/es/typography/Title";
import {Divider, Flex, Button} from "antd";
import "./index.css";
import { listQuestionBankVoByPageUsingPost } from "@/api/questionBankController";
import { listQuestionVoByPageUsingPost } from "@/api/questionController";
import Link from "next/link";
import React from "react";
import QuestionBankList from "@/components/QuestionBankList";
import QuestionList from "@/components/QuestionList";
import Image from "next/image";
import { RocketOutlined, BookOutlined } from "@ant-design/icons";

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
    console.error("获取题库列表失败", e);
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
    console.error("获取题目列表失败", e);
  }

  return (
    <div id="homePage" className="max-width-content">
      {/* 英雄区域 */}
      <div className="home-hero">
        <div className="dragon-element"></div>
        <h1>龙腾智途 - 编程能力提升平台</h1>
        <p>
          专注于帮助开发者提升编程技能、准备技术面试的综合学习平台。
          海量题库、智能模拟面试、个性化学习路径，助你在技术之路上乘风破浪！
        </p>
        <div className="home-hero-buttons">
          <Link href="/banks" className="home-hero-button home-hero-button-primary">
            <BookOutlined /> 浏览题库
          </Link>
          <Link href="/mockInterview/create" className="home-hero-button home-hero-button-primary">
            <RocketOutlined /> 模拟面试
          </Link>
        </div>
      </div>

      {/* 题库部分 */}
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题库</Title>
        <Link href={"/banks"}>查看更多</Link>
      </Flex>
      <QuestionBankList questionBankList={questionBankList} />
      <Divider />
      
      {/* 题目部分 */}
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题目</Title>
        <Link href={"/questions"}>查看更多</Link>
      </Flex>
      <QuestionList questionList={questionList} />
    </div>
  );
}
