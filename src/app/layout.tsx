"use client";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import "./globals.css";
import BasicLayout from "@/layouts/BasicLayout";
import React, { useCallback, useEffect, useState } from "react";
import store, { AppDispatch } from "@/stores";
import { Provider, useDispatch } from "react-redux";
import { getLoginUserUsingGet } from "@/api/userController";
import { setLoginUser } from "@/stores/loginUser";
import AccessLayout from "@/access/AccessLayout";
import LoginUserVO = API.LoginUserVO;
import { App } from "antd";

/**
 * 客户端专用组件，确保在客户端渲染
 */
const ClientOnly: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [isClient, setIsClient] = useState(false);
  
  useEffect(() => {
    setIsClient(true);
  }, []);
  
  return isClient ? <>{children}</> : null;
};

/**
 * 执行初始化逻辑的布局（多封装一层）
 * @param children
 * @constructor
 */
const InitLayout: React.FC<
  Readonly<{
    children: React.ReactNode;
  }>
> = ({ children }) => {
  const dispatch = useDispatch<AppDispatch>();
  // 初始化全局用户状态
  const doInitLoginUser = useCallback(async () => {
    const res = await getLoginUserUsingGet();
    if (res.data) {
      // 更新全局用户状态
      // 使用类型断言来告诉TypeScriptres.data的类型是LoginUserVO。
      dispatch(setLoginUser(res.data as LoginUserVO));
    } else {
      // 仅用于测试
      // setTimeout(() => {
      //   const testUser = {
      //     userName: "test",
      //     id: 1,
      //     userAvatar: "https://www.code-nav.cn/logo.png",
      //     userRole: ACCESS_ENUM.ADMIN,
      //   };
      //   dispatch(setLoginUser(testUser));
      // }, 3000);
    }
  }, []);

  // 只执行一次
  useEffect(() => {
    doInitLoginUser();
  }, []);
  return children;
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="zh" suppressHydrationWarning>
      <body suppressHydrationWarning>
        <AntdRegistry>
          <Provider store={store}>
            <InitLayout>
              <ClientOnly>
                <App>
                  <BasicLayout>
                    <AccessLayout>{children}</AccessLayout>
                  </BasicLayout>
                </App>
              </ClientOnly>
            </InitLayout>
          </Provider>
        </AntdRegistry>
      </body>
    </html>
  );
}
