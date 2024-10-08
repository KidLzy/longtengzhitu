"use client";

import {useRef} from "react";
import {ActionType, ProColumns, ProTable} from "@ant-design/pro-components";
import Link from "next/link";
import TagList from "@/components/TagList";
import {listQuestionVoByPageUsingPost} from "@/api/questionController";
import {TablePaginationConfig} from "antd";

interface Props {
  defaultQuestionList?: API.QuestionVO[]
}

/**
 * 题目表格组件
 * @constructor
 */
export default function QuestionTable(props: Props) {
  const actionRef = useRef<ActionType>();

  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionVO>[] = [
    {
      title: "题目",
      dataIndex: "title",
      render(_, record) {
        return <Link href={`/question/${record.id}`}>{record.title}</Link>;
      },
    },
    {
      title: "标签",
      dataIndex: "tagList",
      valueType: "select",
      fieldProps: {
        mode: "tags"
      },
      render: (_, record) => <TagList tagList={record.tagList} />,
    },
  ];

  return (
      <div className="question-table">
        <ProTable
            actionRef={actionRef}
            columns={columns}
            size="large"
            search={{
              labelWidth: "auto",
            }}
            pagination={
              {
                pageSize: 12,
                showTotal: (total) => `总共 ${total} 条`,
                showSizeChanger: false,
              } as TablePaginationConfig
            }
            request={async (params, sort, filter) => {
              const sortField = Object.keys(sort)?.[0];
              const sortOrder = sort?.[sortField];
              // 请求
              const { data, code } = await listQuestionVoByPageUsingPost({
                ...params,
                sortField,
                sortOrder,
                ...filter,
              } as API.UserQueryRequest) as any;
              // 更新结果
              const newTotal = Number(data.total) || 0;
              const newData = data.records || [];
              return {
                success: code === 0,
                data: newData,
                total: newTotal,
              };
            }}
        />
      </div>
  );
}
