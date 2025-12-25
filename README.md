# custom-auth1

注册用户:登录 Keycloak 管理后台 → 选择你的 Realm（如 myrealm）→ 左侧菜单「Clients」→ 点击「Create client」。
<img width="548" height="395" alt="image" src="https://github.com/user-attachments/assets/c65f84ac-4890-464a-ac00-6260ef54431b" />
<img width="279" height="288" alt="image" src="https://github.com/user-attachments/assets/2621b440-7f30-4323-8f03-3e7bfafa2b78" />

创建完成后，进入该客户端（credential-server-admin）→ 切换到「Credentials」标签页：Client secret：系统自动生成的字符串（如 abc123-xxxx-xxxx），复制下来！
去credential-server/src/main/resources/application里改
<img width="508" height="31" alt="image" src="https://github.com/user-attachments/assets/c40d0373-e69a-4bf1-b484-1ed68fe5254e" />

赋予 realm-admin 权限（关键）

    进入客户端 → 切换到「Service account roles」标签页；
    「Client roles」下拉框：选择 realm-management（必须选这个，不是默认的 credential-server-admin）；
    「Available roles」列表中找到 realm-admin → 点击「Add selected」；
    确认「Assigned roles」中显示 realm-admin，表示权限赋予成功。(版本不一样可能不一样,反正你找到realm-admin这个添加就可以了)


起完数据库和credential-server服务器后,可以用这个补全你前面数据库里没有注册的用户:

curl -X POST http://localhost:8081/api/fill-empty-userid

成功与否你开着keycloak服务器的用户那里看最直观或者看数据库变化也可以


模拟采集完成后数据入库时同步创建用户:

curl -X POST http://localhost:8081/api/save-auto-user \
-H "Content-Type: application/json" \
-d '{
    "phoneNumberHash": "test_phone_hash_123",
    "deviceFingerprintHash": "test_device_hash_456",
    "finalCredentialHash": "test_new_hash_123456"
}'







服务器和认证器部署方式没变
更改服务器代码让其返回数据库新增列userid (我改过了,重新下一下credential-server就可以✔)

数据库新增user_id列为用户id用于返回在keycloak服务器进行比对

启动test-app:
python -m http.server 3000

client设置:

Root URL :http://localhost:3000

Valid redirect URIs:http://localhost:3000/*

Web origins:http://localhost:3000

keycloak管理:
<img width="982" height="611" alt="微信图片_20251223232734_49_8" src="https://github.com/user-attachments/assets/08765c18-c480-46e3-95e4-3e9ca705c45a" />


测试链接:
http://localhost:8080/realms/myrealm/protocol/openid-connect/auth?client_id=test-app&redirect_uri=http://localhost:3000&response_type=code&scope=openid&device_credential_hash=966c4778b4ccddf14bd7e8ab30b77e51073dd2f22aa60c8ee07db6890827ac97
