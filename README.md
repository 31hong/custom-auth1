# custom-auth1
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
