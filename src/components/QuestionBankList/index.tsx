"use client";
import {Avatar, List} from "antd";
import "./index.css";
import Link from "next/link";
import { 
  CodeOutlined, 
  DatabaseOutlined, 
  LaptopOutlined, 
  HtmlOutlined,
  CoffeeOutlined,
  BookOutlined
} from "@ant-design/icons";

interface Props {
    questionBankList: API.QuestionBankVO[];
}

/**
 * 题库列表组件
 * @param props
 * @constructor
 */
const QuestionBankList = (props: Props) => {
    const {questionBankList = []} = props;
    
    // 根据题库标题选择合适的图标
    const getIconForBank = (title: string) => {
      const lowerTitle = title.toLowerCase();
      if (lowerTitle.includes('java')) {
        return <CoffeeOutlined className="bank-icon java" />;
      } else if (lowerTitle.includes('python')) {
        return <CodeOutlined className="bank-icon python" />;
      } else if (lowerTitle.includes('mysql') || lowerTitle.includes('sql')) {
        return <DatabaseOutlined className="bank-icon database" />;
      } else if (lowerTitle.includes('html')) {
        return <HtmlOutlined className="bank-icon html" />;
      } else if (lowerTitle.includes('虚拟机') || lowerTitle.includes('jvm')) {
        return <LaptopOutlined className="bank-icon vm" />;
      } else {
        return <BookOutlined className="bank-icon default" />;
      }
    };
    
    const questionBankView = (questionBank: API.QuestionBankVO) => {
        return (
            <div className="question-bank-card">
                <Link href={`/bank/${questionBank.id}`}>
                    <div className="bank-icon-container">
                        {questionBank.picture ? 
                            <Avatar src={questionBank.picture} size={48} className="bank-avatar" /> : 
                            getIconForBank(questionBank.title)
                        }
                    </div>
                    <div className="bank-content">
                        <h3 className="bank-title">{questionBank.title}</h3>
                        <p className="bank-description">{questionBank.description || "暂无描述"}</p>
                    </div>
                </Link>
                <div className="bank-highlight"></div>
            </div>
        );
    };

    return (
        <div className="question-bank-list">
            <List
                grid={{
                    gutter: 24,
                    column: 4,
                    xs: 1,
                    sm: 2,
                    md: 3,
                    lg: 4,
                }}
                dataSource={questionBankList}
                renderItem={(item) => <List.Item>{questionBankView(item)}</List.Item>}
                locale={{ emptyText: "暂无题库，敬请期待" }}
            />
        </div>
    );
};

export default QuestionBankList;
