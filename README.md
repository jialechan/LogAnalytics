# LogAnalytics
```shell
#建立文件目录
mkdir /userdata1/server_script/
cd /userdata1/server_script

#获取代码
git clone https://github.com/jialechan/logAnalytics-repo
cd logAnalytics-repo/

#根据具体配置
cp application.yml.template application.yml
vim application.yml
cp nginxConfig.sh.template nginxConfig.sh
vim nginxConfig.sh

#给予运行权限
chmod 700 LogAnalytics.jar LogAnalytics.sh log_daily.sh nginxConfig.sh

#设置定时运行任务
crontab -e
00 01 * * * /userdata1/server_script/logAnalytics-repo/LogAnalytics.sh >> /userdata1/server_script/logAnalytics-repo/cronJob.log

#切换到root，设置nginx定时生成每天日志
su -
crontab -e
00 00 * * * /userdata1/server_script/logAnalytics-repo/log_daily.sh
```
## info
```shell
http {
  ...
  log_format chatLog 'T[$time_iso8601] STAT[$status] REQ_T[$upstream_response_time] URL[$request]';
  ...
}  
```

```shell
server {
    listen       80;
    server_name  xxxx.com;
    access_log /userdata1/logs/xxxx/api.xxxx.com.access.log chatLog;
    location / {
        proxy_pass http://172.17.10.61:8224;
        proxy_set_header Host   $host;
        proxy_set_header X-Real-IP      $remote_addr;
        proxy_set_header X-Forwarded-For        $remote_addr;
    }
}
```
