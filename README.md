範例代碼下載回去後, 配置好你自己的資料庫  

啟動後  

取得授權
```
curl -X POST \
  http://localhost:5566/oauth/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'username=admin&password=123456&grant_type=password'
```

更新授權
```
curl -X POST \
  http://localhost:5566/oauth/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpX_SO8tnl15XkRrW6zHgBWQZUUUpW-qWOmwIk'
```

取得 JWT 金鑰
```
curl -X GET \
  http://localhost:5566/oauth/token_key
```

驗證 Token
```
curl -X POST \
  'http://localhost:5566/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9VudF9pZCI6IkFuZHJvaDwpaBY' \
  -H 'Postman-Token: d23ec25f-cae5-492a-afc3-e345dc51ff6e' \
  -H 'cache-control: no-cache'
```

如果想理解 Oauth 跟 JWT 或是 Spring 授權流程機制的可以看前一版的說明
https://github.com/samzhu/ps-authservice
