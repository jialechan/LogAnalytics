# LogAnalytics

mkdir -p /userdata1/server_script   
cd /userdata1/server_script   

wget "https://github.com/jialechan/LogAnalytics/releases/download/0.0.1/LogAnalytics.jar" -O LogAnalytics.jar

[vim application.yml](https://gist.github.com/jialechan/7b545757ea70358d5e77770a5893ce3a)   
[vim log_daily.sh](https://gist.github.com/ae96910add7a9a13c9e0d314071ba5a7)   
chmod 700 log_daily.sh   
[vim LogAnalytics.sh](https://gist.github.com/c7fe0ac9da371e8fe285ee759e582d53)      
chmod 700 LogAnalytics.sh   

[vim /xxx/.../nginx.conf](https://gist.github.com/9d4b42d55a0321683d2c651d5dda0c37)   
/userdata1/software/nginx/sbin/nginx -s reload   
ls -l /userdata1/software/nginx/logs   

crontab -e   
00 00 * * * /userdata1/server_script/log_daily.sh   
00 01 * * * /userdata1/server_script/LogAnalytics.sh   
