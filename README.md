# bysj
本系统为毕业设计的相关文档，通过搭建Janusgraph后建立一个王者荣耀游戏数据搜索引擎
gamesearch是搜索引擎Web服务，使用SpringBoot作为底层框架，前端使用Bootstrap+Vue.js+jQuery+d3.js
janusdata是java爬虫程序

需要搭配Janusgraph服务器可使用，本系统使用的为Janusgraph单机版，http连接Janusgraph的Url为ip/8182
部署完成后访问http://localhost:8002/gameSearch/resultChart.html 为关系图查询界面，http://localhost:8002/gameSearch/result.html 为文本查询界面效果如下

