#FROM:基于mongo镜像进行构建新的镜像,在构建时会自动从docker hub拉取base镜像 必须作为Dockerfile的第一个指令出现
FROM java:8-jre
#镜像维护者的姓名和邮箱地址
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>
#ADD用来从context上下文复制新文件、目录或远程文件url，并将它们添加到位于指定路径的映像文件系统中。
ADD ./target/notification-service.jar /app/
# 在镜像内执行　java -Xmx200m -jar /app/notification-service.jar命令
# -Xmx200m　代表分配最大的内存空间200m给java虚拟机　
CMD ["java", "-Xmx200m", "-jar", "/app/notification-service.jar"]
# 代表容器对外暴露端口为8000 , 这个可能会被docker-compose.yml文件中设置的端口覆盖，因为docker-compose.yml的优先级高
EXPOSE 8000