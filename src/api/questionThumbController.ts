// @ts-ignore
/* eslint-disable */
import request from "@/libs/request";

/** doThumb POST /api/question_thumb/ */
export async function doThumbUsingPost1(
  body: API.QuestionThumbAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>("/api/question_thumb/", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
