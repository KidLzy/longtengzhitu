import React from "react";
import { Descriptions, Tag, Typography, Tooltip } from "antd";
import styles from "./index.module.css";

const { Paragraph } = Typography;

interface UserInfoItemProps {
  icon: React.ReactNode;
  label: string;
  value: string | string[] | number | undefined | null;
  isParagraph?: boolean;
  isTag?: boolean;
}

const UserInfoItem: React.FC<UserInfoItemProps> = ({
                                                     icon,
                                                     label,
                                                     value,
  isParagraph = false,
                                                     isTag = false,
                                                   }) => {
  // 添加调试日志
  console.log(`渲染 ${label}:`, { value, isParagraph, isTag });

  // 处理空值，显示"未设置"
  if (value === undefined || value === null || (Array.isArray(value) && value.length === 0) || value === "") {
    return (
      <Descriptions.Item
        label={
          <div className={styles.userInfoLabel}>
            {icon}
            <span className={styles.labelText}>{label}</span>
          </div>
        }
      >
        <span className={styles.emptyValue}>未设置</span>
      </Descriptions.Item>
    );
  }

  if (isTag && Array.isArray(value)) {
    // 显示标签列表
  return (
      <Descriptions.Item
          label={
          <div className={styles.userInfoLabel}>
          {icon}
            <span className={styles.labelText}>{label}</span>
          </div>
          }
      >
        <div className={styles.tagContainer}>
              {value.map((tag, index) => (
            <Tooltip title={tag} key={index}>
              <Tag 
                className={`${styles.expertiseTag} ${styles.animatedTag}`}
                color={getTagColor(index)}
              >
                    {tag}
                  </Tag>
            </Tooltip>
              ))}
            </div>
      </Descriptions.Item>
    );
  }

  if (isParagraph) {
    // 显示多行文本
    return (
      <Descriptions.Item
        label={
          <div className={styles.userInfoLabel}>
            {icon}
            <span className={styles.labelText}>{label}</span>
          </div>
        }
      >
        <Paragraph 
          className={styles.userInfoParagraph}
          ellipsis={{ rows: 3, expandable: true, symbol: '展开' }}
        >
          {String(value)}
        </Paragraph>
      </Descriptions.Item>
    );
  }

  // 默认显示普通文本
  return (
    <Descriptions.Item
      label={
        <div className={styles.userInfoLabel}>
          {icon}
          <span className={styles.labelText}>{label}</span>
        </div>
      }
    >
      <span className={styles.userInfoValue}>{String(value)}</span>
      </Descriptions.Item>
  );
};

// 根据索引生成不同的标签颜色
const getTagColor = (index: number): string => {
  const colors = [
    '#1890ff', '#52c41a', '#fa8c16', '#eb2f96',
    '#13c2c2', '#722ed1', '#faad14', '#a0d911',
    '#f5222d', '#2f54eb'
  ];
  return colors[index % colors.length];
};

export default UserInfoItem;