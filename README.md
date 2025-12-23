# custom-auth1
服务器和认证器部署方式没变

数据库更改966c4778b4ccddf14bd7e8ab30b77e51073dd2f22aa60c8ee07db6890827ac97这个对应的phoneid为用户id用于返回在keycloak服务器进行比对

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
