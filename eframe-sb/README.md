springboot 做的demo 骨架项目。方便以后拷贝代码。


# 功能

## springboot

## 数据库层
1. jdbc+自己封装的简单dao抽象层。
2. test包有ddl生成器。
3. 生成的ddl是带有注解的。
4. 数据库连接池driud，配置监控

## http连接
okhttp、httpclient？




# 部署和配置

## pom.xml
1. log.base.path设置一个目录。
2. log.level 根据实际情况配置。研发环境可以debug，生产环境建议info。

## 数据库
