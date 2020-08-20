# 基于java 8-jre镜像
FROM java:8-jre
# 程序开发者是Alexander Lukyanchikov <sqshq@sqshq.com>
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>
# 基于Dockerfile所目录的　将./target/gateway.jar　复制一份到镜像内部的/app/目录下
ADD ./target/gateway.jar /app/
# 在镜像内执行　java -Xmx200m -jar /app/gateway.jar命令
# -Xmx200m　代表分配最大的内存空间200m给java虚拟机　
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]
# 代表容器对外暴露端口为4000 , 这个会被docker-compose.yml文件中设置的端口覆盖，因为docker-compose.yml的优先级高
EXPOSE 4000