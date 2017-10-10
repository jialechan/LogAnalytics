# LogAnalytics

wget "https://github-production-release-asset-2e65be.s3.amazonaws.com/105360415/d7c575c2-ad7a-11e7-928e-9e745ebc22c8?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20171010%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20171010T102212Z&X-Amz-Expires=300&X-Amz-Signature=542658cbb77020ddb9a01c67e57f99e93dbf402688b5963eb46721d780f2fa0f&X-Amz-SignedHeaders=host&actor_id=7006540&response-content-disposition=attachment%3B%20filename%3DLogAnalytics.jar&response-content-type=application%2Foctet-stream"    

mkdir -p /userdata1/server_script   
cd /userdata1/server_script   

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
