import React, { useState, useEffect } from "react";
import { Avatar, Card, Divider, Typography, Tooltip, message, Skeleton, Collapse, Space, Badge } from "antd";
import { 
  UserOutlined, 
  EditOutlined, 
  SettingOutlined, 
  CopyOutlined, 
  IdcardOutlined,
  CalendarOutlined,
  PhoneOutlined,
  MailOutlined,
  TrophyOutlined,
  HeartOutlined,
  InfoCircleOutlined,
  ContactsOutlined
} from "@ant-design/icons";
import styles from "./index.module.css";

const { Title, Paragraph, Text } = Typography;
const { Panel } = Collapse;

interface UserInfoProps {
  user: API.User | API.LoginUserVO | any;
  showEditButton?: boolean;
  onEdit?: () => void;
  loading?: boolean;
}

const UserInfo: React.FC<UserInfoProps> = ({
  user = {},
  showEditButton = false,
  onEdit,
  loading = false,
}) => {
  // 添加动画状态
  const [mounted, setMounted] = useState(false);
  
  // 组件挂载后触发动画
  useEffect(() => {
    setMounted(true);
  }, []);

  // 格式化时间
  const formatDate = (dateString: string) => {
    if (!dateString) return "未设置";
    try {
      const date = new Date(dateString);
      return isNaN(date.getTime()) 
        ? "日期格式错误" 
        : date.toLocaleDateString("zh-CN");
    } catch (err) {
      return "日期格式错误";
    }
  };

  // 复制ID
  const copyId = () => {
    if (user.id) {
      navigator.clipboard.writeText(String(user.id))
        .then(() => {
          message.success('ID已复制到剪贴板');
        })
        .catch(() => {
          message.error('复制失败，请手动复制');
        });
    }
  };

  // 处理擅长方向数据
  const getExpertiseTags = (): string[] => {
    if (!user.expertiseDirection) return [];
    return user.expertiseDirection.split(',').map((tag: string) => tag.trim()).filter((tag: string) => tag);
  };

  // 工作经验处理
  const getExperienceTags = (): string[] => {
    if (!user.workExperience) return [];
    // 如果工作经验中有逗号，按逗号分割
    if (user.workExperience.includes(',')) {
      return user.workExperience.split(',').map((tag: string) => tag.trim()).filter((tag: string) => tag);
    }
    return [user.workExperience];
  };

  // 年级处理
  const getGradeTags = (): string[] => {
    if (!user.grade) return [];
    // 如果年级中有逗号，按逗号分割
    if (user.grade.includes(',')) {
      return user.grade.split(',').map((tag: string) => tag.trim()).filter((tag: string) => tag);
    }
    return [user.grade];
  };

  // 获取关键标签
  const getKeyTags = (): React.ReactNode[] => {
    const tags: React.ReactNode[] = [];
    
    // 所有标签统一展示
    const gradeTags = getGradeTags();
    gradeTags.forEach((tag, index) => {
      tags.push(
        <Tooltip title="年级" key={`grade-${index}`}>
          <span className={`${styles.userTag} ${styles.gradeTag}`}>
            {tag}
          </span>
        </Tooltip>
      );
    });
    
    const expTags = getExperienceTags();
    expTags.forEach((tag, index) => {
      tags.push(
        <Tooltip title="工作经验" key={`exp-${index}`}>
          <span className={`${styles.userTag} ${styles.experienceTag}`}>
            {tag}
          </span>
        </Tooltip>
      );
    });
    
    const expertiseTags = getExpertiseTags();
    expertiseTags.forEach((tag, index) => {
      tags.push(
        <Tooltip title="擅长方向" key={`expertise-${index}`}>
          <span className={`${styles.userTag} ${styles.expertiseTag}`}>
            {tag}
          </span>
        </Tooltip>
      );
    });
    
    return tags;
  };

  // 获取角色标签
  const getRoleTag = (): React.ReactNode => {
    if (!user.userRole) return null;
    
    // 根据不同角色返回不同样式的标签
    switch (user.userRole) {
      case 'admin':
        return (
          <Tooltip title="管理员权限">
            <span className={`${styles.userTag} ${styles.adminRoleTag}`}>
              管理员 <TrophyOutlined />
            </span>
          </Tooltip>
        );
      case 'vip':
        return (
          <Tooltip title="享受VIP特权">
            <span className={`${styles.userTag} ${styles.vipRoleTag}`}>
              VIP会员 <HeartOutlined />
            </span>
          </Tooltip>
        );
      case 'creator':
        return (
          <Tooltip title="认证内容创作者">
            <span className={`${styles.userTag} ${styles.creatorRoleTag}`}>
              内容创作者 <EditOutlined />
            </span>
          </Tooltip>
        );
      default:
        return (
          <span className={`${styles.userTag} ${styles.userRoleTag}`}>
            普通用户
          </span>
        );
    }
  };

  // 信息项组件
  const InfoItem = ({ 
    label, 
    value, 
    copyable = false,
    icon
  }: { 
    label: string; 
    value: any; 
    copyable?: boolean;
    icon: React.ReactNode;
  }) => (
    <div className={styles.infoItem}>
      <div className={styles.infoLabelContainer}>
        {icon}
        <Text type="secondary" className={styles.infoLabel}>{label}</Text>
      </div>
      <div className={styles.infoValueContainer}>
        <Text className={styles.infoValue}>{value || "未设置"}</Text>
        {copyable && (
          <Tooltip title="复制ID">
            <CopyOutlined className={styles.copyIcon} onClick={copyId} />
          </Tooltip>
        )}
      </div>
    </div>
  );

  // 基础信息组
  const BasicInfoGroup = () => (
    <div className={styles.infoGroup}>
      <InfoItem 
        label="我的ID" 
        value={user.id ? String(user.id) : "未知"} 
        copyable={true} 
        icon={<IdcardOutlined className={styles.infoIcon} />}
      />
      <InfoItem 
        label="注册时间" 
        value={formatDate(user.createTime)} 
        icon={<CalendarOutlined className={styles.infoIcon} />}
      />
    </div>
  );

  // 联系信息组
  const ContactInfoGroup = () => (
    <div className={styles.infoGroup}>
      <InfoItem 
        label="手机号" 
        value={user.phoneNumber} 
        icon={<PhoneOutlined className={styles.infoIcon} />}
      />
      <InfoItem 
        label="邮箱" 
        value={user.email} 
        icon={<MailOutlined className={styles.infoIcon} />}
      />
    </div>
  );

  // 简洁风格的用户信息卡片
  const CompactUserCard = () => (
    <Skeleton loading={loading} avatar paragraph={{ rows: 2 }} active>
      <div className={styles.compactUserHeader}>
        <div className={styles.avatarContainer}>
          <Avatar
            src={user.userAvatar}
            size={70}
            className={styles.userAvatar}
            icon={!user.userAvatar && <UserOutlined />}
          />
          <Badge 
            count={getRoleTag()} 
            className={styles.roleBadge}
          />
        </div>
        
        <div className={styles.userInfoMain}>
          <Title level={4} className={styles.userName}>
            {user.userName || "未设置用户名"}
          </Title>
          
          <div className={styles.keyTagContainer}>
            {getKeyTags()}
          </div>
        </div>
      </div>

      {user.userProfile && (
        <Paragraph 
          className={styles.userProfile}
          ellipsis={{ rows: 2, expandable: true, symbol: '展开' }}
        >
          {user.userProfile}
        </Paragraph>
      )}

      <Collapse 
        bordered={false} 
        className={styles.infoCollapse}
        expandIconPosition="end"
      >
        <Panel 
          header={
            <div className={styles.panelHeader}>
              <InfoCircleOutlined /> 基础信息
            </div>
          } 
          key="basic"
          className={styles.infoPanel}
        >
          <BasicInfoGroup />
        </Panel>
        
        <Panel 
          header={
            <div className={styles.panelHeader}>
              <ContactsOutlined /> 联系方式
            </div>
          } 
          key="contact"
          className={styles.infoPanel}
        >
          <ContactInfoGroup />
        </Panel>
      </Collapse>
    </Skeleton>
  );

  return (
    <Card
      className={`${styles.userInfoCard} ${mounted ? styles.cardVisible : ''}`}
      title={
        <div className={styles.cardHeader}>
          <span className={styles.cardHeaderTitle}>个人信息</span>
          {showEditButton && (
            <div className={styles.headerButtons}>
              <Tooltip title="编辑">
                <div 
                  className={styles.editBtn} 
                  onClick={(e) => {
                    e.stopPropagation();
                    if (onEdit) onEdit();
                  }}
                >
                  <EditOutlined />
                </div>
              </Tooltip>
              <Tooltip title="设置">
                <div 
                  className={styles.settingBtn}
                  onClick={(e) => {
                    e.stopPropagation();
                  }}
                >
                  <SettingOutlined />
                </div>
              </Tooltip>
            </div>
          )}
        </div>
      }
      bordered={false}
    >
      <CompactUserCard />
    </Card>
  );
};

export default UserInfo;
