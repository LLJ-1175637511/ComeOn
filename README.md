# ComeOn
来吧：是一款综合娱乐APP。


# 切换AGP版本[7.0.2]或[8.0.0]
## 1. 更改 java jdk 版本：
「Settings」>「Build, Execution, Deployment」>「Build Tools」>「Gradle」; 选择java jdk版本为 [11] 或 [17]；
## 2. 修改 agp_upgrade_disable.gradle 的 disableNewAGP 
将文件中的开关，修改为 disableNewAGP = false 或 true；
## 3. 修改 gradle-wrapper.properties 中的 distributionUrl 
将文件中 distributionUrl 的地址， 替换为 [gradle-wrapper-7.0.2.properties] 或 [gradle-wrapper-8.0.0.properties] 的内容。