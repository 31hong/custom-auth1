# Keycloak自定义认证模块
## 📖项目简介
本项目是一个基于Keycloak平台开发的自定义认证器。认证模块在custom-authenticator实现，核心目的是Keycloak提供的标准身份验证流程增加一层额外的安全校验机制，即基于移动终端设备属性的凭证认证，满足跨主体数据鉴权系统的安全要求。本模块在Keycloak认证流中充当协调者，将认证决策转交给Credential-Server服务。代码结构如下：  
**custom-authenticator：自定义认证开发模块，实现用户访问应用服务时触发认证，进行访问控制（重点开发这个）；**  
**themes/mycustomtheme**： 基于Keycloak模板的主题文件，用于修改keycloak自定义登录页面的样式；  
**credential-server**： 后端服务器，与终端侧的采集插件连接，接收终端数据，生成与存储身份凭证，管理mysql数据库；  
**config/realm-export.json**： 自定义领域配置（可以直接导入，也可以不使用，自己配置）。
#### 核心认证流程
**触发**： 用户访问时触发认证。  
**数据交互**： custom-authenticator与credential-server通信，获取凭证比对结果。  
**决策**： 若设备凭证比对成功，用户直接通过设备认证；若失败，认证流程将导向传统的账号密码登录界面。
## 🏗️环境依赖与Keycloak实例构建
### 环境依赖
本项目依赖于 Keycloak 24.0.5 版本的源码进行编译和部署。需从 https://github.com/keycloak/keycloak/releases/tag/24.0.5 下载 Source code (zip)后，本地构建实例生成 keycloak-999.0.0-SNAPSHOT。推荐使用 JDK 17 或更高版本。
### 开发实例构建步骤
要运行本项目，要先完成 Keycloak 服务器的构建和启动：  
· **源码构建**： 下载 Keycloak 24.0.5 源码，并在本地完成构建，生成版本名为 **keycloak-999.0.0-SNAPSHOT** 的服务器发行包。  
· **启动服务器**： 进入本地构建的 Keycloak 服务器 bin 目录（例如：D:\keycloak\keycloak-main\keycloak-999.0.0-SNAPSHOT\bin），执行以下命令，即可启动开发模式服务器：
```bash
kc.bat start-dev --hostname=localhost
```
## 🎨自定义认证模块的部署&配置
### 第一阶段：自定义模块编译与部署
**编译模块:**   
导航至认证模块custom-authenticator根目录，执行 Maven 编译和打包命令，生成 JAR 包：
```bash
mvn clean install
mvn clean compile
mvn clean package
```
生成的jar包将存储在custom-authenticator\target目录下。  
将jar包复制到keycloak-999.0.0-SNAPSHOT\providers\文件夹下之后，在keycloak-999.0.0-SNAPSHOT\bin目录下运行：
```bash
kc.bat build  //识别新加入的Provider
kc.bat start-dev --hostname=localhost  //启动服务器
```
**部署验证:**  
JAR 包安装成功后，会被 Keycloak 服务器自动加载。此时可以访问后台（登录账号：admin，密码：admin）
```bash
http://localhost:8080/admin
```
### 第二阶段：Keycloak 管理控制台配置
❌如果是**初次构建**，则需要完成以下自定义配置：    
**配置领域(Realm)**: 创建或选择目标领域。  
**创建新的认证流（Authentication Flow）**: 导航至 Authentication -> Flows 选项卡，创建一个新流（例如命名为 My Authentication Flow）。  
**添加自定义执行器（Authenticator）**: 在新流中，添加认证执行器（例如命名为 My Custom Authenticator）。  
**绑定认证流**: 将 Browser Flow 设置为刚创建的 My Authentication Flow。  

✅如果**已创建过**自定义领域，则执行以下操作导入：  
· 在左侧导航栏，点击 Add realm。  
· 选择 Import，上传 realm-export.json 文件（在config文件夹下）。  
这会自动创建包含自定义认证流和所需客户端的领域。

### 第三阶段：界面显示与自定义主题
自定义主题文件位于仓库的 /themes/mycustomtheme 目录下。在完成 Keycloak 认证模块的部署后，需要执行以下操作以应用自定义界面：  
· **复制主题文件**： 将 /themes/ 目录完整复制到本地 Keycloak 服务器的安装目录下，确保存在 keycloak-999.0.0-SNAPSHOT\themes\mycustomtheme 文件夹。  
· **在控制台启用自定义主题**：  
登录 Keycloak 管理界面。  
导航至 Realm Settings -> Themes 选项卡。  
在 Login Theme 下拉菜单中，确保选择 mycustomtheme。

## 🚨触发自定义认证
```bash
http://localhost:8080/realms/myrealm/protocol/openid-connect/auth?client_id=test-app&redirect_uri=http://localhost&response_type=code&scope=openid
```
登录账号testuser，密码123456，该账号已注册，可认证成功，进入下一个页面（不稳定，有时候显示错误），后续页面还没开发。

## 💻 后续开发计划与协作需求
本项目目前已完成基础环境搭建和自定义模块框架部署。为实现最终**凭证认证**功能，我们需要继续完成以下**认证逻辑**和**前端主题**的实现。
#### 目标：
**期望的认证流程示意：**  
用户访问服务：用户访问受 Keycloak 保护的第三方应用。  
   |  
Keycloak 拦截：Keycloak 认证流启动，首先调用 My Custom Authenticator 自定义认证器。  
   |  
显示过渡页：用户看到 “正在凭证认证” 界面。  
   |  
后端比对：自定义模块调用 credential-server服务器 进行设备凭证比对。  
   |  
认证决策：  
   |——— ✅ 成功：用户直接通过认证，被重定向回第三方服务。  
   |——— ❌ 失败：用户被重定向到账号密码登录界面，并显示失败提示。  
**需求目标：**   
**1. 核心认证逻辑实现 (custom-authenticator)** ：完成 custom-authenticator 模块内部的 authenticate() 方法逻辑，实现与 credential-server 的交互和认证决策。  
**2. 前端主题实现与流程展示 (themes/mycustomtheme)**：实现认证流程中所需的用户界面，以提供良好的用户体验和流程反馈。  

<img width="629" height="406" alt="屏幕截图 2025-12-16 224847" src="https://github.com/user-attachments/assets/c6d00433-2050-434a-b5c7-ea7dd9f24326" />  
<br>如图为当前设计的认证流程，有其它想法和建议也请提出，感谢配合~
