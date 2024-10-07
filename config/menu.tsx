import { MenuDataItem } from "@ant-design/pro-layout";
import { CrownOutlined } from "@ant-design/icons";

// 菜单列表
export const menus = [
    {
        path: "/",
        name: "主页",
    },
    {
        path: "/banks",
        name: "题库",
    },
    {
        path: "/questions",
        name: "题目",
    },
    {
        name: "面试鸭",
        path: "https://mianshiya.com",
        target: "_blank",
    },
    {
        path: "/admin",
        name: "管理",
        icon: <CrownOutlined />,
        // access: ACCESS_ENUM.ADMIN,
        children: [
            {
                path: "/admin/user",
                name: "用户管理",
                // access: ACCESS_ENUM.ADMIN,
            },
            {
                path: "/admin/bank",
                name: "题库管理",
                // access: ACCESS_ENUM.ADMIN,
            },
            {
                path: "/admin/question",
                name: "题目管理",
                // access: ACCESS_ENUM.ADMIN,
            },
        ],
    },
] as MenuDataItem[];