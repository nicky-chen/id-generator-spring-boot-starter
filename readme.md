`todo`
 - 本地缓存号码段 状态机
 - 状态监听基于eventbus
 - 获取远程服务基于eventbus 加 责任链
 - 注解校验器问题
 - 获取远程服务对象池
 - id服务 熔断 基于代理模式和resilience4j
 - 获取本地property文件处理
 - 号码段步幅需要计算 基于timewheel
 - id规则 和 失败策略 基于spi
 - id获取规则基于 策略 + 组合
 - 号码段预加载及持久化
 - DI 基于 guice
 - 异常处理基于原型模式优化
 - 缓存号码段使用LongAdd
 - id获取失败重试基于 guava-retryer
 - id持久化日志基于 disruptor
 - junit单元测试
 - 加入适配器基于javassit
 - **<u>id生成服务失败切换需要考虑 id规则一致性问题和持久化问题</u>**
 
 
  
















[百度id](https://github.com/baidu/uid-generator/blob/2fcbc13d2016fcfb7648a18296951f6942215255/src/main/java/com/baidu/fsg/uid/buffer/RejectedPutBufferHandler.java)

[didi生成器](https://github.com/nicky-chen/tinyid)

大致流程

![avatar](http://assets.processon.com/chart_image/5c526110e4b056ae2a026149.png)
