# iDouban SpringBoot 重构版

## 项目简介

本项目是将原有的 JavaWeb 项目重构为基于 SpringBoot + Maven + MyBatis 的现代化 Web 应用。

## 技术栈

- **SpringBoot 2.7.18** - 核心框架
- **MyBatis** - 持久层框架
- **MySQL 8.0** - 数据库
- **Druid** - 数据库连接池
- **Thymeleaf** - 模板引擎
- **PageHelper** - 分页插件
- **Lombok** - 代码简化工具
- **SLF4J + Logback** - 日志框架

## 项目结构

```
idouban-springboot/
├── src/main/java/com/lzh/idouban/
│   ├── IDoubanApplication.java    # 启动类
│   ├── config/                     # 配置类
│   │   ├── WebMvcConfig.java       # Web MVC配置
│   │   └── MyBatisConfig.java      # MyBatis配置
│   ├── controller/                 # 控制器层
│   │   ├── UserController.java     # 用户相关
│   │   ├── ArticleController.java  # 文章相关
│   │   ├── CommentController.java  # 评论相关
│   │   ├── DoumailController.java  # 豆邮相关
│   │   └── FriendController.java   # 好友相关
│   ├── service/                    # 服务层
│   │   ├── UserService.java
│   │   ├── ArticleService.java
│   │   ├── ArticleCommentService.java
│   │   ├── DoumailService.java
│   │   ├── FriendService.java
│   │   └── impl/                   # 实现类
│   ├── mapper/                     # 数据访问层
│   │   ├── UserMapper.java
│   │   ├── ArticleMapper.java
│   │   ├── ArticleCommentMapper.java
│   │   ├── ArticleReplyMapper.java
│   │   ├── DoumailMapper.java
│   │   └── FriendMapper.java
│   ├── entity/                     # 实体类
│   │   ├── User.java
│   │   ├── Article.java
│   │   ├── ArticleComment.java
│   │   ├── ArticleReply.java
│   │   ├── Doumail.java
│   │   ├── Friend.java
│   │   └── Tag.java
│   ├── interceptor/                # 拦截器
│   │   ├── LoginInterceptor.java   # 登录拦截
│   │   └── VisitorInterceptor.java # 访客统计
│   ├── util/                       # 工具类
│   │   ├── MD5Util.java            # MD5加密
│   │   └── ValidationUtil.java     # 参数校验
│   └── common/                     # 公共类
│       ├── Result.java             # 统一响应结果
│       └── PageResult.java         # 分页结果
├── src/main/resources/
│   ├── mapper/                     # MyBatis XML映射文件
│   ├── templates/                  # Thymeleaf模板
│   ├── static/                     # 静态资源
│   └── application.yml             # 配置文件
└── pom.xml                         # Maven配置
```

## 主要功能

### 用户模块
- 用户注册/登录/登出
- 用户信息管理
- 密码找回
- 头像上传

### 文章模块
- 文章发布/编辑/删除
- 文章列表（分页）
- 文章搜索
- 点赞/收藏/转发
- 浏览量统计

### 评论模块
- 发表评论
- 评论列表
- 评论点赞

### 豆邮模块
- 发送私信
- 聊天记录
- 未读消息提醒

### 好友模块
- 关注/取消关注
- 好友列表
- 粉丝列表
- 黑名单

## 数据库表结构

- `user` - 用户表
- `a_article` - 文章表
- `a_comment` - 评论表
- `a_reply` - 回复表
- `a_collection` - 收藏表
- `a_star` - 点赞表
- `a_share` - 转发表
- `doumail` - 豆邮表
- `friend` - 好友关系表

## 快速开始

### 1. 环境准备
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置
修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/douban?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: your_password
```

### 3. 导入数据库
执行 `sql/douban.sql` 文件创建数据库和表结构。

### 4. 运行项目
```bash
mvn spring-boot:run
```

或

```bash
mvn clean package
java -jar target/idouban-springboot-1.0.0.jar
```

### 5. 访问应用
打开浏览器访问：http://localhost:8080

## 优化点

### 1. 代码结构优化
- 采用分层架构：Controller -> Service -> Mapper
- 统一响应结果封装
- 统一异常处理
- 统一的参数校验

### 2. 数据库优化
- 使用 MyBatis 替代 JDBC
- 使用 Druid 连接池
- SQL 语句优化（使用索引、避免N+1查询）
- 分页查询优化

### 3. 日志优化
- 使用 SLF4J + Logback
- 统一的日志格式
- 日志文件自动分割

### 4. 命名规范
- 类名使用大驼峰
- 方法名和变量名使用小驼峰
- 数据库字段使用下划线命名
- 统一的方法命名规范

### 5. 安全性优化
- 密码使用 MD5 加密
- SQL 注入防护（使用预编译语句）
- XSS 防护（HTML转义）

## 与原项目对比

| 特性 | 原项目 | 重构后 |
|------|--------|--------|
| 框架 | Servlet + JSP | SpringBoot + Thymeleaf |
| 数据访问 | JDBC | MyBatis |
| 连接池 | 自定义 | Druid |
| 构建工具 | 无 | Maven |
| 配置方式 | properties | YAML |
| 日志 | System.out | SLF4J + Logback |
| 分页 | 手动实现 | PageHelper |
| 事务 | 手动控制 | 注解控制 |

## 作者

林泽鸿

## 许可证

MIT License
