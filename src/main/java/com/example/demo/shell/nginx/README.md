# Nginx

# MAC 安装[brew]
- brew info nginx  
- brew install nginx  
- [目录] /opt/homebrew/Cellar/nginx/1.25.2nginx -V
- [配置] /opt/homebrew/etc/nginx/nginx.conf 

# runing
- [启动] sudo nginx || /opt/homebrew/Cellar/nginx/1.25.2/bin/nginx
- [测试] /opt/homebrew/Cellar/nginx/1.25.2/bin/nginx -t
- [重启] /opt/homebrew/Cellar/nginx/1.25.2/bin/nginx -s reload
- [停止] /opt/homebrew/Cellar/nginx/1.25.2/bin/nginx -s stop
- [默认配置启动] http://localhost:8080/nginx

# config
- 分为三块：全局块、events块、http块
- 在http块中，又包含http全局块、多个server块。每个server块中，可以包含server全局块和多个location块。在同一配置块中嵌套的配置块，各个之间不存在次序关系。
- 某指令同时出现在http全局块中和server块中，并且配置不同，则应该以server块中的配置为准。
- 如果某个指令在两个不同层级的块中同时出现，则采用“就近原则”，即以较低层级块中的配置为准。
## 全局块
- 全局块是默认配置文件从开始到events块之间的一部分内容，主要设置一些影响Nginx服务器整体运行的配置指令，因此，这些指令的作用域是Nginx服务器全局。
- 通常包括配置运行Nginx服务器的用户（组）、允许生成的worker process数、Nginx进程PID存放路径、日志的存放路径和类型以及配置文件引入等。
### 指定可以运行nginx服务的用户和用户组，只能在全局块配置
- user [user] [group]
### 将user指令注释掉，或者配置成nobody的话所有用户都可以运行
- user nobody nobody;
### user指令在Windows上不生效，如果你制定具体用户和用户组会报小面警告
- nginx: [warn] "user" is not supported, ignored in D:\software\nginx-1.18.0/conf/nginx.conf:2
### 指定工作线程数，可以制定具体的进程数，也可使用自动模式，这个指令只能在全局块配置
- worker_processes number | auto；
### 列子：指定4个工作线程，这种情况下会生成一个master进程和4个worker进程
- worker_processes 4;
### 指定pid文件存放的路径，这个指令只能在全局块配置
- pid logs/nginx.pid;
### 指定错误日志的路径和日志级别，此指令可以在全局块、http块、server块以及location块中配置。(在不同的块配置有啥区别？？)
- 其中debug级别的日志需要编译时使用--with-debug开启debug开关
- error_log [path] [debug | info | notice | warn | error | crit | alert | emerg]
- error_log  logs/error.log  notice;
- error_log  logs/error.log  info;

## events块
- events块涉及的指令主要影响Nginx服务器与用户的网络连接。常用到的设置包括是否开启对多worker process下的网络连接进行序列化，是否允许同时接收多个网络连接，选取哪种事件驱动模型处理连接请求，每个worker process可以同时支持的最大连接数等。
- 这一部分的指令对Nginx服务器的性能影响较大，在实际配置中应该根据实际情况灵活调整。
### 当某一时刻只有一个网络连接到来时，多个睡眠进程会被同时叫醒，但只有一个进程可获得连接。如果每次唤醒的进程数目太多，会影响一部分系统性能。在Nginx服务器的多进程下，就有可能出现这样的问题。
### 开启的时候，将会对多个Nginx进程接收连接进行序列化，防止多个进程对连接的争抢
### 默认是开启状态，只能在events块中进行配置
- accept_mutex on | off;
    
### 如果multi_accept被禁止了，nginx一个工作进程只能同时接受一个新的连接。否则，一个工作进程可以同时接受所有的新连接。
### 如果nginx使用kqueue连接方法，那么这条指令会被忽略，因为这个方法会报告在等待被接受的新连接的数量。
### 默认是off状态，只能在event块配置
- multi_accept on | off;
    
### 指定使用哪种网络IO模型，method可选择的内容有：select、poll、kqueue、epoll、rtsig、/dev/poll以及eventport，一般操作系统不是支持上面所有模型的。
### 只能在events块中进行配置
- use method
- use epoll
    
### 设置允许每一个worker process同时开启的最大连接数，当每个工作进程接受的连接数超过这个值时将不再接收连接
### 当所有的工作进程都接收满时，连接进入logback，logback满后连接被拒绝
### 只能在events块中进行配置
### 注意：这个值不能超过超过系统支持打开的最大文件数，也不能超过单个进程支持打开的最大文件数，具体可以参考这篇文章：https://cloud.tencent.com/developer/article/1114773
- worker_connections  1024;

## http块
- http块是Nginx服务器配置中的重要部分，代理、缓存和日志定义等绝大多数的功能和第三方模块的配置都可以放在这个模块中。
- 前面已经提到，http块中可以包含自己的全局块，也可以包含server块，server块中又可以进一步包含location块，在本书中我们使用“http全局块”来表示http中自己的全局块，即http块中不包含在server块中的部分。
- 可以在http全局块中配置的指令包括文件引入、MIME-Type定义、日志自定义、是否使用sendfile传输文件、连接超时时间、单连接请求数上限等。
### 常用的浏览器中，可以显示的内容有HTML、XML、GIF及Flash等种类繁多的文本、媒体等资源，浏览器为区分这些资源，需要使用MIME Type。换言之，MIME Type是网络资源的媒体类型。Nginx服务器作为Web服务器，必须能够识别前端请求的资源类型。
    
### include指令，用于包含其他的配置文件，可以放在配置文件的任何地方，但是要注意你包含进来的配置文件一定符合配置规范，比如说你include进来的配置是worker_processes指令的配置，而你将这个指令包含到了http块中，着肯定是不行的，上面已经介绍过worker_processes指令只能在全局块中。
### 下面的指令将mime.types包含进来，mime.types和ngin.cfg同级目录，不同级的话需要指定具体路径
- include  mime.types;
    
### 配置默认类型，如果不加此指令，默认值为text/plain。
### 此指令还可以在http块、server块或者location块中进行配置。
- default_type  application/octet-stream;
    
### access_log配置，此指令可以在http块、server块或者location块中进行设置
### 在全局块中，我们介绍过errer_log指令，其用于配置Nginx进程运行时的日志存放和级别，此处所指的日志与常规的不同，它是指记录Nginx服务器提供服务过程应答前端请求的日志
- access_log path [format [buffer=size]]
### 如果你要关闭access_log,你可以使用下面的命令
- access_log off;
    
### log_format指令，用于定义日志格式，此指令只能在http块中进行配置
``` 
log_format  main '$remote_addr - $remote_user [$time_local] "$request" '
                 '$status $body_bytes_sent "$http_referer" '
                 '"$http_user_agent" "$http_x_forwarded_for"';
```
### 定义了上面的日志格式后，可以以下面的形式使用日志
- access_log  logs/access.log  main; 
### 开启关闭sendfile方式传输文件，可以在http块、server块或者location块中进行配置
- sendfile  on | off;
### 设置sendfile最大数据量,此指令可以在http块、server块或location块中配置
- sendfile_max_chunk size;
### 其中，size值如果大于0，Nginx进程的每个worker process每次调用sendfile()传输的数据量最大不能超过这个值(这里是128k，所以每次不能超过128k)；如果设置为0，则无限制。默认值为0。
- sendfile_max_chunk 128k;
### 配置连接超时时间,此指令可以在http块、server块或location块中配置。
### 与用户建立会话连接后，Nginx服务器可以保持这些连接打开一段时间
### timeout，服务器端对连接的保持时间。默认值为75s;header_timeout，可选项，在应答报文头部的Keep-Alive域设置超时时间：“Keep-Alive:timeout= header_timeout”。报文中的这个指令可以被Mozilla或者Konqueror识别。
- keepalive_timeout timeout [header_timeout]
### 下面配置的含义是，在服务器端保持连接的时间设置为120 s，发给用户端的应答报文头部中Keep-Alive域的超时时间设置为100 s。
- keepalive_timeout 120s 100s
### 配置单连接请求数上限，此指令可以在http块、server块或location块中配置。
### Nginx服务器端和用户端建立会话连接后，用户端通过此连接发送请求。指令keepalive_requests用于限制用户通过某一连接向Nginx服务器发送请求的次数。默认是100
- keepalive_requests number;

TODO https://www.cnblogs.com/54chensongxia/p/12938929.html


## 默认配置
```

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
-                  '$status $body_bytes_sent "$http_referer" '
-                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       8080;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

    - redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    - proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
    -    proxy_pass   http://127.0.0.1;
        #}

    - pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
    -    root           html;
    -    fastcgi_pass   127.0.0.1:9000;
    -    fastcgi_index  index.php;
    -    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    -    include        fastcgi_params;
        #}

    - deny access to .htaccess files, if Apache's document root
    - concurs with nginx's one
        #
        #location ~ /\.ht {
    -    deny  all;
        #}
    }


- another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
-    listen       8000;
-    listen       somename:8080;
-    server_name  somename  alias  another.alias;

-    location / {
-        root   html;
-        index  index.html index.htm;
-    }
    #}


- HTTPS server
    #
    #server {
-    listen       443 ssl;
-    server_name  localhost;

-    ssl_certificate      cert.pem;
-    ssl_certificate_key  cert.key;

-    ssl_session_cache    shared:SSL:1m;
-    ssl_session_timeout  5m;

-    ssl_ciphers  HIGH:!aNULL:!MD5;
-    ssl_prefer_server_ciphers  on;

-    location / {
-        root   html;
-        index  index.html index.htm;
-    }
    #}
    include servers/*;
}

```