"use client";
import {Card, Select} from "antd";
import "./index.css";
import MdViewer from "@/components/MdViewer";
import TagList from "@/components/TagList";
import Title from "antd/es/typography/Title";
import {useState} from "react";
import {themeList} from "bytemd-plugin-theme";
import useAddUserSignInRecord from "@/hooks/useAddUserSignInRecord";

interface Props {
  question: API.QuestionVO;
}

/**
 * 题目卡片
 * @param props
 * @constructor
 */
const QuestionCard = (props: Props) => {
  const { question } = props;
    const [themeValue, setThemeValue] = useState<string>("channing-cyan");

    const handleChange = (value: string) => {
        setThemeValue(value);
    };

  // 签到
  useAddUserSignInRecord();

  return (
    <div className="question-card">
      <Card>
        <Title level={1} style={{ fontSize: 24 }}>
          {question.title}
        </Title>
        <TagList tagList={question.tagList} />
        <div style={{ marginBottom: 16 }} />
        <MdViewer value={question.content} />
      </Card>
      <div style={{ marginBottom: 16 }} />
      <Card title="推荐答案">
          <Select
              defaultValue={themeValue}
              style={{width: 205}}
              onChange={handleChange}
              options={themeList.map((item) => {
                  return {
                      key: item.theme,
                      label: item.title,
                      value: item.theme,
                  };
              })}
          />
        <MdViewer value={question.answer} theme={themeValue}/>
      </Card>
    </div>
  );
};

export default QuestionCard;
