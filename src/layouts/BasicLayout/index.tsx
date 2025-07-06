"use client";
import {
  GithubFilled,
  LogoutOutlined,
  SearchOutlined,
  UserOutlined,
  HomeOutlined,
  BellOutlined,
} from "@ant-design/icons";
import { ProLayout } from "@ant-design/pro-components";
import { Dropdown, Input, message, App, Badge, Tooltip, Avatar } from "antd";
import React, { useEffect, useState } from "react";
import Image from "next/image";
import { usePathname, useRouter } from "next/navigation";
import Link from "next/link";
import GlobalFooter from "@/components/GlobalFooter";
import { menus } from "../../../config/menu";
import { AppDispatch, RootState } from "@/stores";
import { useDispatch, useSelector } from "react-redux";
import getAccessibleMenus from "@/access/meauAccess";
import { userLogoutUsingPost } from "@/api/userController";
import { setLoginUser } from "@/stores/loginUser";
import { DEFAULT_USER } from "@/constants/user";
import SearchInput from "@/layouts/BasicLayout/components/SearchInput";
import "./index.css";

interface Props {
  children: React.ReactNode;
}

export default function BasicLayout({ children }: Props) {
  const pathname = usePathname();
  const loginUser = useSelector((state: RootState) => state.loginUser);
  const dispatch = useDispatch<AppDispatch>();
  const router = useRouter();
  const { message: messageApi } = App.useApp();
  const [headerClass, setHeaderClass] = useState("");

  // 监听滚动事件，添加导航栏滚动效果
  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 20) {
        setHeaderClass("scrolled");
      } else {
        setHeaderClass("");
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  /**
   * 用户注销
   */
  const userLogout = async () => {
    try {
      await userLogoutUsingPost();
      messageApi.success("已退出登录");
      dispatch(setLoginUser(DEFAULT_USER));
      router.push("/user/login");
    } catch (e){
      messageApi.error("退出登录失败"+ (e as any).message);
    }
  };

  // 自定义头像组件
  const AvatarDropdown = () => {
    if (!loginUser.id) {
      return (
        <Tooltip title="点击登录" placement="bottom">
          <Avatar
            src="/assets/notLoginUser.png"
            size="small"
            className="header-avatar not-login"
            onClick={() => router.push("/user/login")}
          />
        </Tooltip>
      );
    }

    return (
      <Dropdown
        menu={{
          items: [
            {
              key: "userCenter",
              icon: <UserOutlined />,
              label: "个人中心",
            },
            {
              key: "logout",
              icon: <LogoutOutlined />,
              label: "退出登录",
            },
          ],
          onClick: async (event: { key: React.Key }) => {
            const { key } = event;
            if (key === "logout") {
              userLogout();
            } else if (key === "userCenter") {
              router.push("/user/center");
            }
          },
        }}
      >
        <div className="avatar-container">
          <Avatar
            src={loginUser.userAvatar || "/assets/notLoginUser.png"}
            size="small"
            className="header-avatar"
          />
          <span className="avatar-name">{loginUser.userName || "用户"}</span>
        </div>
      </Dropdown>
    );
  };

  return (
    <div
      id="basicLayout"
      style={{
        height: "100vh",
        overflow: "auto",
      }}
    >
      <ProLayout
        title="龙腾智途-刷题平台"
        layout="top"
        logo={
          <Image
            src="/assets/logo.png"
            height={32}
            width={32}
            alt="龙腾智途刷题网站"
            style={{ width: 'auto', height: 'auto' }}
          />
        }
        location={{
          pathname,
        }}
        headerClassName={headerClass}
        avatarProps={false}
        actionsRender={(props) => {
          if (props.isMobile) return [];
          return [
            <SearchInput key="search" />,
            <Tooltip title="通知" key="notification">
              <Badge count={0} dot>
                <BellOutlined className="action-icon" />
              </Badge>
            </Tooltip>,
            <Tooltip title="GitHub" key="github">
              <a
                href="https://github.com/KidLzy/longtengzhitu"
                target="_blank"
                className="github-link"
              >
                <GithubFilled className="action-icon" />
              </a>
            </Tooltip>,
            <AvatarDropdown key="avatar" />,
          ];
        }}
        headerTitleRender={(logo, title, _) => {
          const defaultDom = (
            <Link href="/" className="header-logo-link">
              {logo}
              <span className="header-title">{title}</span>
            </Link>
          );
          if (typeof window !== "undefined") {
            if (document.body.clientWidth < 1400 || _.isMobile) {
              return defaultDom;
            }
          }
          return <>{defaultDom}</>;
        }}
        // 渲染底部栏
        footerRender={() => {
          return <GlobalFooter />;
        }}
        onMenuHeaderClick={(e) => router.push("/")}
        // 定义有哪些菜单
        menuDataRender={() => {
          return getAccessibleMenus(loginUser, menus);
        }}
        // 定义菜单项如何渲染
        menuItemRender={(item, dom) => (
          <Link href={item.path || "/"} target={item.target} className="menu-item-link">
            {dom}
          </Link>
        )}
      >
        {children}
      </ProLayout>
    </div>
  );
}
