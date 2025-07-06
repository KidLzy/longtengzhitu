"use client";
import { Card, Col, Row, message, Modal, Form, Spin } from "antd";
import { useSelector } from "react-redux";
import { RootState } from "@/stores";
import React, { useState, useEffect } from "react";
import CalendarChart from "@/app/user/center/components/CalendarChart";
import UserInfo from "@/app/user/center/components/UserInfo";
import UserEditForm from "@/app/user/center/components/UserEditForm";
import styles from "./index.module.css";
import { 
  getUserByIdUsingGet, 
  getLoginUserUsingGet,
  editUserUsingPost
} from "@/api/userController";
import useAddUserSignInRecord from "@/hooks/useAddUserSignInRecord";

export default function UserCenterPage() {
    // 从 Redux 获取当前登录用户的基本信息
    const loginUser = useSelector((state: RootState) => state.loginUser);
    const [userDetail, setUserDetail] = useState<any>(null);
    const [activeTabKey, setActiveTabKey] = useState<string>("record");
    const [loading, setLoading] = useState<boolean>(true);
    const [pageLoaded, setPageLoaded] = useState<boolean>(false);
    
    // 编辑用户信息相关状态
    const [isEditModalVisible, setIsEditModalVisible] = useState<boolean>(false);
    const [submitting, setSubmitting] = useState<boolean>(false);
    
    // 使用Form.useForm创建表单实例，确保表单状态与组件状态关联
    const [form] = Form.useForm();
    
    // 自动签到
    const { loading: signInLoading } = useAddUserSignInRecord();

    // 组件挂载完成后的效果
    useEffect(() => {
        // 添加页面加载完成的动画
        const timer = setTimeout(() => {
            setPageLoaded(true);
        }, 300);
        
        return () => clearTimeout(timer);
    }, []);

    // 在组件挂载时获取登录用户信息
    useEffect(() => {
        const fetchLoginUser = async () => {
            // 检查Redux中是否已有用户ID
            if (loginUser && loginUser.id) {
                // 如果有登录用户ID，直接获取详细信息
                fetchUserDetail(loginUser.id);
            } else {
                try {
                    // 尝试获取登录用户信息
                    await getLoginUserUsingGet();
                    // 此时应已更新Redux中的用户状态
                    if (loginUser && loginUser.id) {
                        fetchUserDetail(loginUser.id);
                    } else {
                        setLoading(false);
                        message.error("未获取到用户ID");
                    }
                } catch (err) {
                    console.error("获取登录用户信息失败:", err);
                    setLoading(false);
                    message.error("获取登录信息失败，请重新登录");
                }
            }
        };

        fetchLoginUser();
    }, [loginUser]);

    // 获取用户详细信息
    const fetchUserDetail = async (userId: number) => {
        if (!userId) {
            setLoading(false);
            return;
        }

        try {
            // 使用封装好的 API 调用
            const res = await getUserByIdUsingGet({ id: userId });
            
            if (res && res.data) {
                // 更新用户详细信息
                setUserDetail(res.data);
            } else {
                message.warning("获取用户详情不完整");
            }
        } catch (err) {
            console.error("获取用户详细信息失败:", err);
            message.error("获取用户信息失败");
        } finally {
            setLoading(false);
        }
    };

    // 合并基本信息和详细信息，确保使用完整的User对象信息
    const userInfo = userDetail 
        ? { 
            ...loginUser,  // 基础信息
            ...userDetail  // 详细信息会覆盖同名字段
          } 
        : loginUser;  // 如果没有详细信息，就只使用基础信息

    // 处理编辑按钮点击
    const handleEdit = () => {
        // 预填充表单数据
        form.setFieldsValue({
            userName: userInfo.userName,
            userProfile: userInfo.userProfile,
            phoneNumber: userInfo.phoneNumber,
            email: userInfo.email,
            expertiseDirection: userInfo.expertiseDirection,
            workExperience: userInfo.workExperience,
            grade: userInfo.grade
        });
        setIsEditModalVisible(true);
    };
    
    // 处理关闭模态框
    const handleCancel = () => {
        form.resetFields();
        setIsEditModalVisible(false);
    };

    // 处理表单提交
    const handleSubmit = async (values: any) => {
        if (!userInfo || !userInfo.id) {
            message.error("用户信息不完整，无法更新");
            return;
        }

        setSubmitting(true);
        try {
            // 使用 editUserUsingPost 更新用户信息
            const res = await editUserUsingPost({
                ...values,
                id: userInfo.id, // 确保传递用户ID
            });

            // 检查API响应
            if (res && res.data) {
                message.success("个人信息更新成功");
                // 关闭模态框
                setIsEditModalVisible(false);
                form.resetFields();
                
                // 重新获取用户信息
                fetchUserDetail(userInfo.id);
            } else {
                message.error("更新失败，请稍后再试");
            }
        } catch (err: any) {
            message.error(err.message || "更新用户信息失败");
            console.error("更新用户信息失败:", err);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className={`${styles.maxWidthContent} ${pageLoaded ? styles.fadeIn : ''}`}>
            {loading ? (
                <div className={styles.loadingContainer}>
                    <Spin size="large" tip="加载用户信息中..." />
                </div>
            ) : (
            <Row gutter={[24, 24]}>
                <Col xs={24} md={8} lg={6}>
                    <UserInfo
                            user={userInfo}
                        showEditButton={true}
                        onEdit={handleEdit}
                            loading={false}
                    />
                </Col>
                    <Col xs={24} md={16} lg={18} className={styles.rightColumn}>
                    <Card
                            className={styles.tabCard}
                        tabList={[
                            { key: "record", label: "刷题记录" },
                            { key: "analysis", label: "学习分析" },
                            { key: "achievement", label: "成就徽章" },
                        ]}
                        activeTabKey={activeTabKey}
                        onTabChange={(key: string) => setActiveTabKey(key)}
                            styles={{ 
                                body: { 
                                    padding: activeTabKey === "record" ? "16px 0 0" : "16px",
                                    backgroundColor: activeTabKey === "record" ? "#f9f9f9" : "#fff"
                                } 
                            }}
                    >
                        {activeTabKey === "record" && <CalendarChart />}
                        {activeTabKey === "analysis" && (
                                <div className={styles.contentSection}>
                                <h3>学习数据分析</h3>
                                <p>这里将展示用户的学习统计图表...</p>
                            </div>
                        )}
                        {activeTabKey === "achievement" && (
                                <div className={styles.contentSection}>
                                <h3>成就徽章</h3>
                                <p>这里将展示用户获得的成就徽章...</p>
                            </div>
                        )}
                    </Card>
                </Col>
            </Row>
            )}

            {/* 编辑用户信息模态框 */}
            <Modal
                title="编辑个人信息"
                open={isEditModalVisible}
                onCancel={handleCancel}
                footer={null}
                destroyOnClose={false}
                maskClosable={false}
                width={520}
                centered
                bodyStyle={{ 
                    padding: '24px', 
                    borderRadius: '12px',
                    maxHeight: '80vh',
                    overflow: 'auto'
                }}
                className={styles.editModal}
            >
                <UserEditForm 
                    user={userInfo}
                    form={form}
                    submitting={submitting}
                    onFinish={handleSubmit}
                    onCancel={handleCancel}
                />
            </Modal>
        </div>
    );
}