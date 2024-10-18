import {ProColumns, ProTable} from "@ant-design/pro-components";
import {message, Modal} from "antd";
import {reviewQuestionUsingPost} from "@/api/questionController";
import React from "react";

interface Props{
    oldData?: API.Question;
    visible: boolean;
    columns:ProColumns<API.Question>[];
    onSubmit:(values: API.ReviewRequest)=>void;
    onCancel:()=>void;
}

const thisColumns: ProColumns<API.Question>[] = [
    {
        title: '审核状态',
        dataIndex: 'reviewStatus',
        valueType: 'select',
        valueEnum: {
            1: { text: '通过' },
            2: { text: '拒绝' },
        },
    },
    {
        title: '审核说明',
        dataIndex: 'reviewMessage',
        formItemProps: {
            rules: [{ required: true, message: '请输入问题内容' }],
        },
    },
];

/**
 * 审核弹窗
 * @param fields
 */
const handleReview = async (fields: API.ReviewRequest) =>{
    const hide = message.loading('正在审核');
    try{
        await reviewQuestionUsingPost(fields);
        hide();
        message.success('审核成功');
        return true;
    }catch (error: any) {
        hide();
        message.error('审核失败，' + error.message);
        return false;
    }
}

const ReviewModal: React.FC<Props> = (props) => {
    const {oldData, visible, columns, onSubmit, onCancel } = props;
    if (!oldData) {
        return <></>;
    }
    // 表单转换
    let initValues = { ...oldData };
    return (
        <Modal
            destroyOnClose
            title={'审核'}
            open={visible}
            footer={null}
            onCancel={() => {
                onCancel?.();
            }}>
            <ProTable
                type="form"
                columns={thisColumns}
                form={{
                    initialValues: initValues,
                }}
                onSubmit={async (values: API.ReviewRequest) => {
                    const success = await handleReview({
                        ...values,
                        id: oldData?.id as any});
                    if (success) {
                        onSubmit?.(values);
                    }
                }}
            />
        </Modal>
    );
};
export default ReviewModal;
