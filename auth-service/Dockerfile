#FROM用来基于哪个镜像进行构建新的镜像,在构建时会自动从docker hub拉取base镜像 必须作为Dockerfile的第一个指令出现
FROM java:8-jre
#镜像维护者的姓名和邮箱地址
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>
#ADD用来从context上下文复制新文件、目录或远程文件url，并将它们添加到位于指定路径的映像文件系统中。
ADD ./target/auth-service.jar /app/
# 在镜像内执行　java -Xmx200m -jar /app/auth-service.jar命令
# -Xmx200m　代表分配最大的内存空间200m给java虚拟机　
CMD ["java", "-Xmx200m", "-jar", "/app/auth-service.jar"]
#用来指定构建的镜像在运行为容器时对外暴露的端口,没有指定暴露的都是80端口
EXPOSE 5000