# 龙腾智途后端系统

> 基于Java SpringBoot的互联网内容社区和智能学习平台

## 技术栈

### 核心框架与特性

- Spring Boot 2.7.x
- Spring MVC
- MyBatis + MyBatis Plus（含分页功能）
- Sa-Token 权限认证体系
- Spring AOP 切面编程
- Spring Scheduler 定时任务
- 热点监控和限流 (Sentinel)

### 数据存储与缓存

- MySQL 数据库
- Redis 分布式缓存
- Elasticsearch 搜索引擎
- 腾讯云 COS 对象存储

### 开发工具与辅助库

- Easy Excel 表格处理
- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解
- Knife4j 接口文档

### 系统特性

- Sa-Token 分布式登录认证
- 全局请求响应拦截器（日志记录）
- 全局异常处理
- AI 模型集成
- 热Key探测
- Nacos 配置中心
- 微信开放平台与公众号对接
- 安全黑名单过滤

## 核心业务功能

### 用户系统

- 用户注册、登录、登出、信息管理
- 基于Sa-Token的权限管理体系


### 题目与题库系统

- 题目的创建、编辑、审核、查询
- 题库管理（创建、维护题库）
- 题目-题库关联管理
- 数据库与ES双重检索方案
- 全量/增量ES同步定时任务
- 会员内容权限控制

### AI模拟面试

- 基于大模型的模拟面试系统
- 支持自定义岗位、难度、工作经验
- 智能对话及面试评估
- 面试记录保存与回顾

### 文件管理

- 基于腾讯云COS的文件存储
- 按业务分类的文件上传功能
- 文件安全控制

## 快速上手指南

### 数据库配置

1. 修改`application.yml`中的数据库配置:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ltzt_db
    username: root
    password: 你的密码
```

2. 执行`sql/create_table.sql`创建数据库表结构
3. 执行`sql/init_data.sql`初始化基础数据

### Redis配置

修改`application.yml`中的Redis配置:

```yaml
spring:
  redis:
    database: 0
    host: localhost
    port: 6379
    timeout: 5000
    # password: 你的密码
```

并取消`MainApplication.java`中的`RedisAutoConfiguration`排除配置。

### Elasticsearch配置

1. 修改`application.yml`中的Elasticsearch配置:

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    username: root
    password: 你的密码
```

2. 使用`sql/question_es_mapping.json`创建题目索引

3. 启用同步任务：取消`job`目录下`FullSyncQuestionToEs`和`IncSyncQuestionToEs`文件中的`@Component`注释


### 对象存储配置

修改`application.yml`中的腾讯云COS配置:

```yaml
cos:
  client:
    accessKey: 你的accessKey
    secretKey: 你的secretKey
    region: 你的region
    bucket: 你的bucket
```

### AI配置

修改`application.yml`中的AI模型配置，支持接入不同的大语言模型服务。

### 项目启动

1. 启动所有依赖服务（MySQL, Redis, Elasticsearch等）
2. 运行`MainApplication.java`
3. 访问 `http://localhost:8101/api/doc.html` 查看接口文档

## 项目部署

1. 使用Maven打包：`mvn clean package`
2. 使用提供的Dockerfile构建容器镜像
3. 部署到目标环境

## 注意事项

- 所有需要修改的配置项都已用`todo`标记
- 生产环境部署前请修改默认密钥和密码
- 确保相关依赖服务正常运行
- 部署前请进行完整的功能测试

## 开发说明

项目支持代码生成器功能，可快速生成新模块的基础代码：

1. 修改`generate.CodeGenerator`类中的生成参数
2. 运行生成器获取基础代码
3. 根据业务需求进行定制化开发

