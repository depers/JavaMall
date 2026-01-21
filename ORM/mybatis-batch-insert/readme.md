
# mybatis-batch-insert

## Intro

该项目是探讨Java批量插入如何才能达到最好的性能，通过比对JDBC、Mybatis和Mybatis-plus三种不同的工具，通过比较单个多次插入和批量之间的区别，最后得出结论，通过原生jdbc实现的批量插入配合使用MySQL提供的`rewriteBatchedStatements=true`能够达到最好的性能。

## 要点
1. mybatis配置sql打印逻辑
2. mybatis配置sql执行耗时
