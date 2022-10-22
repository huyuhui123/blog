# blog
 基于码神之路的博客项目

# 项目介绍

这个博客项目是Spring Boot+Vue的前后端分离项目，有着前台和后台两个项目构成，前台采用Spring Boot+Vue3+Jwt+Redis+Mysql，而后台则是Spring Boot+Vue2+Spring Security

blog-api，blog-admin 

这个博客项目是有着功能齐全的博客项目，但还可以完善完善、做做补充

1. 先说路由

路由除了增删改查，还有就是统一异常处理

2. 登录

前台是用拦截器+jwt验证+md5加密来完成的，后台则是用security

## 问题

主要说一说问题，前后端分离，但参数依然需要参数验证，不然到了service肯定会报错，那么使用参数验证注解，可惜的一点是虽然有全局拦截异常，但日志还是会打出异常出来，这样在正式肯定不行

### 日志输出问题

首先是articleList路由，@RequestBody代表要使用bean来装载参数，如果使用get，返回的是全局异常，日志输出没有get请求，这被攻击了就完蛋了

普通的404是没有日志的

