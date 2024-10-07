// @ts-ignore
/* eslint-disable */
import request from "@/libs/request";

/** doQuestionFavour POST /api/question_favour/ */
export async function doQuestionFavourUsingPost(
  body: API.QuestionFavourAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>("/api/question_favour/", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** listFavourQuestionPage POST /api/question_favour/list/page */
export async function listFavourQuestionPageUsingPost(
  body: API.QuestionFavourQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageQuestionVO_>(
    "/api/question_favour/list/page",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** listMyFavourQuestionPage POST /api/question_favour/my/list/page */
export async function listMyFavourQuestionPageUsingPost(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageQuestionVO_>(
    "/api/question_favour/my/list/page",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}
