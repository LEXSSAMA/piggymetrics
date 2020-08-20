# 基于java 8-jre镜像构建新镜像
FROM java:8-jre
# 程序开发者是Alexander Lukyanchikov <sqshq@sqshq.com>
MAINTAINER Chi Dov <d.chiproeng@gmail.com>
# 基于Dockerfile所目录的　将./target/turbine-stream-service.jar　复制一份到镜像内部的/app/目录下
ADD ./target/turbine-stream-service.jar /app/
# 在镜像内执行　java -Xmx200m -jar /app/turbine-stream-service.jar命令
# -Xmx200m　代表分配最大的内存空间200m给java虚拟机　
CMD ["java", "-Xmx200m", "-jar", "/app/turbine-stream-service.jar"]
# 代表容器对外暴露端口为8989
EXPOSE 8989