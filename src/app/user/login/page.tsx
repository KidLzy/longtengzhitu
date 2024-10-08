"use client";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { ProFormText } from "@ant-design/pro-components";
import React from "react";
import Image from "next/image";
import Link from "next/link";
import { userLoginUsingPost } from "@/api/userController";
import { message } from "antd";
import { useDispatch } from "react-redux";
import { AppDispatch } from "@/stores";
import { setLoginUser } from "@/stores/loginUser";
import {LoginFormPage, ProForm} from "@ant-design/pro-form/lib";
import { useRouter } from "next/navigation";
import './index.css';
import LoginUserVO = API.LoginUserVO;
/**
 * 用户登录页面
 * @constructor
 */
const UserLoginPage: React.FC = () => {
  const [form] = ProForm.useForm();
  const dispatch = useDispatch<AppDispatch>();
  const router = useRouter();

  const doSubmit = async (values: API.UserLoginRequest) => {
    try {
      const res = await userLoginUsingPost(values);
      if (res.data) {
        message.success("登录成功");
        // 保存用户登录状态
        dispatch(setLoginUser(res.data as LoginUserVO));
        router.replace("/");
        form.resetFields();
      }
    } catch (e) {
      message.error("登录失败，" + (e as any).message);
    }
  };

  return (
    <div
      id="userLoginPage"
      style={{
        backgroundColor: "white",
        height: "100vh",
      }}
    >
      <LoginFormPage
        backgroundImageUrl="https://mdn.alipayobjects.com/huamei_gcee1x/afts/img/A*y0ZTS6WLwvgAAAAAAAAAAAAADml6AQ/fmt.webp"
        backgroundVideoUrl="https://gw.alipayobjects.com/v/huamei_gcee1x/afts/video/jXRBRK_VAwoAAAAAAAAAAAAAK4eUAQBr"
        logo={
          <Image src="/assets/logo.png" alt="龙腾智途" height={44} width={44} />
        }
        title="龙腾智途 - 用户登录"
        containerStyle={{
          backgroundColor: "rgba(255, 255, 255,0.65)",
          backdropFilter: "blur(4px)",
        }}
        subTitle="程序员面试刷题网站"
        form={form}
        onFinish={doSubmit}
      >
        <ProFormText
          name="userAccount"
          fieldProps={{
            size: "large",
            prefix: <UserOutlined className={"prefixIcon"} />,
          }}
          placeholder={"请输入用户账号"}
          rules={[
            {
              required: true,
              message: "请输入用户账号!",
            },
          ]}
        />
        <ProFormText.Password
          name="userPassword"
          fieldProps={{
            size: "large",
            prefix: <LockOutlined className={"prefixIcon"} />,
          }}
          placeholder={"密码: 请输入密码"}
          rules={[
            {
              required: true,
              message: "请输入密码！",
            },
          ]}
        />
        <div
          style={{
            marginBlockEnd: 24,
            textAlign: "end",
          }}
        >
          还没有账号？
          <Link href={"/user/register"}>去注册</Link>
        </div>
      </LoginFormPage>
    </div>
  );
};

// export default () => {
//   return (
//     <ProConfigProvider dark>
//       <Page />
//     </ProConfigProvider>
//   );
// };
export default UserLoginPage;
