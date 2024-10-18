import Title from "antd/es/typography/Title";
import QuestionTable from "@/components/QuestionTable";
import {listQuestionVoByPageUsingPost, searchQuestionVoByPageUsingPost} from "@/api/questionController";

export default async function QuestionPage({ searchParams }) {
  // 获取url的查询参数
  const { q: searchText } = searchParams;
  //
  let questionList = [];
  let total = 0;

  try {
    const questionRes = await searchQuestionVoByPageUsingPost({
      title: searchText,
      pageSize: 12,
      sortField: "_score",
      sortOrder: "descend",
    }) as any;
    questionList = questionRes.data.records ?? [];
    total = questionRes.data.total ?? 0;
  } catch (e) {
    console.error("获取题目列表失败，" + (e as any).message);
  }

  return (
    <div id="questionPage">
      <Title level={3}>题库大全</Title>
      <QuestionTable defaultQuestionList={questionList} defaultTotal={total} defaultSearchParams={{
        title:searchText,
      }}/>
    </div>
  );
}
