# iDouBan Spring Boot 项目

## 项目简介
这是一个基于Spring Boot、MyBatis的仿豆瓣社区项目，由传统Java Web项目重构而来。

## 技术栈
- **后端框架**: Spring Boot 2.7.18
- **ORM框架**: MyBatis 2.3.1
- **数据库连接池**: Druid 1.2.20
- **数据库**: MySQL 8.0
- **前端**: JSP + CSS + JavaScript
- **构建工具**: Maven
- **其他**: Lombok, Hutool

## 项目结构
```
idouban-springboot/
├── pom.xml                          # Maven配置文件
├── src/
│   └── main/
│       ├── java/com/idouban/
│       │   ├── IDouBanApplication.java   # 启动类
│       │   ├── common/                   # 公共类
│       │   │   ├── Result.java           # 统一响应结果
│       │   │   ├── BusinessException.java # 业务异常
│       │   │   └── GlobalExceptionHandler.java # 全局异常处理
│       │   ├── config/                   # 配置类
│       │   │   ├── WebConfig.java        # Web配置
│       │   │   └── LoginInterceptor.java # 登录拦截器
│       │   ├── controller/               # 控制器层
│       │   ├── mapper/                   # MyBatis Mapper接口
│       │   ├── model/                    # 实体类
│       │   ├── service/                  # 服务层
│       │   └── util/                     # 工具类
│       ├── resources/
│       │   ├── application.yml           # 应用配置
│       │   ├── mapper/                   # MyBatis XML映射文件
│       │   └── static/                   # 静态资源
│       └── webapp/WEB-INF/jsp/           # JSP页面
```

## 功能模块
1. **用户模块**: 登录、注册、个人信息管理、头像上传
2. **文章模块**: 文章发布、编辑、删除、列表展示
3. **互动模块**: 点赞、收藏、转发、评论、回复
4. **好友模块**: 关注、好友管理、黑名单
5. **豆邮模块**: 私信功能

## 数据库配置
修改 `src/main/resources/application.yml` 中的数据库配置:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/douban?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
```

## 运行项目

### 1. 导入数据库
执行 `iDouban-master/iDouban-master/sql/douban.sql` 创建数据库和表

### 2. 编译项目
```bash
mvn clean install -DskipTests
```

### 3. 运行项目
```bash
mvn spring-boot:run
```
或者在IDE中运行 `IDouBanApplication.java`

### 4. 访问项目
打开浏览器访问: http://localhost:8080

## API接口文档

### 用户接口
- POST `/api/user/login` - 用户登录
- POST `/api/user/register` - 用户注册
- POST `/api/user/logout` - 退出登录
- GET `/api/user/info` - 获取当前用户信息
- POST `/api/user/update` - 更新用户信息
- POST `/api/user/portrait` - 更新头像

### 文章接口
- GET `/api/article/{id}` - 获取文章详情
- POST `/api/article/publish` - 发布文章
- GET `/api/article/list` - 获取文章列表
- GET `/api/article/my` - 获取我的文章
- GET `/api/article/search` - 搜索文章

### 互动接口
- POST `/api/interaction/star/{articleId}` - 点赞文章
- POST `/api/interaction/collect/{articleId}` - 收藏文章
- POST `/api/interaction/comment` - 发表评论

### 好友接口
- POST `/api/friend/follow/{userId}` - 关注用户
- DELETE `/api/friend/follow/{userId}` - 取消关注
- GET `/api/friend/list` - 获取好友列表

### 豆邮接口
- POST `/api/doumail/send` - 发送豆邮
- GET `/api/doumail/list/{userId}` - 获取豆邮列表
- GET `/api/doumail/contacts` - 获取联系人列表

## 重构优化说明

### 1. 代码结构优化
- 按照Spring Boot标准包结构重新组织代码
- 使用Lombok简化实体类代码
- 统一命名规范

### 2. 数据库操作优化
- 使用MyBatis替代原生JDBC
- 优化SQL语句，使用索引
- 使用连接池管理数据库连接

### 3. 配置管理
- 将配置集中到application.yml
- 支持多环境配置

### 4. 日志功能
- 使用SLF4J + Logback
- 关键操作添加日志记录

### 5. 异常处理
- 统一异常处理机制
- 友好的错误提示

## 注意事项
1. 确保MySQL数据库已启动
2. 确保数据库中存在douban数据库
3. 首次运行需要下载Maven依赖，请耐心等待
