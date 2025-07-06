"use client";
import { List } from "antd";
import "./index.css";
import TagList from "@/components/TagList";
import Link from "next/link";
import { FileTextOutlined } from "@ant-design/icons";

interface Props {
  questionBankId?: number;
  questionList: API.QuestionVO[];
  cardTitle?: string;
}

/**
 * 题目列表组件
 * @param props
 * @constructor
 */
const QuestionList = (props: Props) => {
  const { questionList = [], cardTitle, questionBankId } = props;

  return (
    <div className="question-list-container">
      {cardTitle && <div className="question-list-title">{cardTitle}</div>}
      <List
        className="question-list"
        itemLayout="horizontal"
        dataSource={questionList}
        renderItem={(item: API.QuestionVO) => (
          <List.Item className="question-item">
            <Link
              href={
                questionBankId
                  ? `/bank/${questionBankId}/question/${item.id}`
                  : `/question/${item.id}`
              }
              className="question-link"
            >
              <div className="question-content">
                <div className="question-header">
                  <div className="question-title">
                    <FileTextOutlined className="question-icon" />
                    {item.title}
                  </div>
                </div>
                <div className="question-tags">
                  <TagList tagList={item.tagList} />
                </div>
              </div>
            </Link>
          </List.Item>
        )}
        locale={{ emptyText: "暂无题目，敬请期待" }}
      />
    </div>
  );
};

export default QuestionList;
