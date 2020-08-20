#FROM用来基于哪个镜像进行构建新的镜像,在构建时会自动从docker hub拉取base镜像 必须作为Dockerfile的第一个指令出现
FROM java:8-jre
#镜像维护者的姓名和邮箱地址
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>
#ADD用来从context上下文复制新文件、目录或远程文件url，并将它们添加到位于指定路径的映像文件系统中
ADD ./target/config.jar /app/
#CMD用来为启动的容器指定执行的命令,在Dockerfile中只能有一条CMD指令。如果列出多个命令，则只有最后一个命令才会生效
CMD ["java", "-Xmx200m", "-jar", "/app/config.jar"]
# HEALTHCHECK用来做健康检测的命令结构是HEALTHCHECK...CMD..
# --interval=duration(默认30s) : 每个设置的时间就会执行一次健康检测
# --timeout=duration(默认30s) : 如果一个检测超过30s没有返回结果就看作失败
# --retries=N(默认3次) : 如果健康检测连续失败3次就看做应用不健康
# --start-period=duration(默认0s) : 设置一个启动准备时间，在容器启动后这个时间内失败的健康检测都不会被计入retries,但是在这个时间内
# 如果有一次健康检测成功，就当作应用已经启动完成，开始正式记数
# CMD 后面的命令可以是shell指令也可以是exec array（这是啥?）
# 容器有三个状态: starting状态（容器启动时没有开始健康检测的时候的状态）,healthy状态(通过健康检测),
# unhealthy状态(连续失败超过retries指定数目后的状态)
# HEALTHCHECK指令只能在dockerfile中出现一次
# CMD后面的命令退出的状态码：0(代表健康正常运行),1(不健康),2(保留，不用这个)
# 详细的HEALTHCHECK看这个:https://docs.docker.com/engine/reference/builder/
HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1
#用来指定构建的镜像在运行为容器时对外暴露的端口,没有指定暴露的都是80端口
EXPOSE 8888