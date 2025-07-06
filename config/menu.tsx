import { MenuDataItem } from "@ant-design/pro-layout";
import { 
  CrownOutlined, 
  RobotOutlined, 
  HomeOutlined, 
  BookOutlined, 
  FileTextOutlined, 
  GlobalOutlined,
  UserOutlined,
  DatabaseOutlined,
  FileAddOutlined,
  CommentOutlined
} from "@ant-design/icons";
import ACCESS_ENUM from "@/access/accessEnum";

// 菜单列表
export const menus = [
  {
    path: "/",
    name: "主页",
    icon: <HomeOutlined />,
  },
  {
    path: "/banks",
    name: "题库",
    icon: <BookOutlined />,
  },
  {
    path: "/questions",
    name: "题目",
    icon: <FileTextOutlined />,
  },
  {
    path: "/mockInterview",
    name: "模拟面试",
    icon: <RobotOutlined />,
    access: ACCESS_ENUM.USER,
  },
  {
    name: "面试鸭",
    path: "https://mianshiya.com",
    target: "_blank",
    icon: <GlobalOutlined />,
  },
  {
    path: "/admin",
    name: "管理",
    icon: <CrownOutlined />,
    access: ACCESS_ENUM.ADMIN,
    children: [
      {
        path: "/admin/user",
        name: "用户管理",
        access: ACCESS_ENUM.ADMIN,
        icon: <UserOutlined />,
      },
      {
        path: "/admin/bank",
        name: "题库管理",
        access: ACCESS_ENUM.ADMIN,
        icon: <DatabaseOutlined />,
      },
      {
        path: "/admin/question",
        name: "题目管理",
        access: ACCESS_ENUM.ADMIN,
        icon: <FileAddOutlined />,
      },
      {
        path: "/admin/mockInterview",
        name: "模拟面试管理",
        access: ACCESS_ENUM.ADMIN,
        icon: <CommentOutlined />,
      },
    ],
  },
] as MenuDataItem[];

// 根据全部路径查找菜单
export const findAllMenuItemByPath = (path: string): MenuDataItem | null => {
  return findMenuItemByPath(menus, path);
};

// 根据路径查找菜单（递归）
export const findMenuItemByPath = (
  menus: MenuDataItem[],
  path: string,
): MenuDataItem | null => {
  for (const menu of menus) {
    if (menu.path === path) {
      return menu;
    }
    if (menu.children) {
      const matchedMenuItem = findMenuItemByPath(menu.children, path);
      if (matchedMenuItem) {
        return matchedMenuItem;
      }
    }
  }
  return null;
};
