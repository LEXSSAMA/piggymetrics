#FROM:基于mongo镜像进行构建新的镜像,在构建时会自动从docker hub拉取base镜像 必须作为Dockerfile的第一个指令出现
FROM mongo:3
#镜像维护者的姓名和邮箱地址
MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>
#ADD用来从context上下文复制新文件、目录或远程文件url，并将它们添加到位于指定路径的映像文件系统中。
ADD init.sh /init.sh
ADD ./dump /
#RUN指令将在当前映像之上的新层中执行任何命令并提交结果。生成的提交映像将用于Dockerfile中的下一步(容器构建过程中就要执行)
# ubuntu的指令
RUN \
#给init.sh脚本赋予执行权限
 chmod +x /init.sh && \
#apt-get update :update是下载源里面的metadata的. 包括这个源有什么包, 每个包什么版本之类的.
# apt-get dist-upgrade:更新packages -y(自动对更新packages的时候的提示确认＜就像我们输入yes＞) --force-yes(强制yes,和-y功能差不多)
# apt-get install dos2unix :安装软件dos2unix
 apt-get update && apt-get dist-upgrade -y --force-yes && apt-get install dos2unix && \
# 安装软件psmisc -y （automatic yes） - q (quiet,忽略进程执行过程的指示)
# psmisc中包含killall工具
 apt-get install psmisc -y -q && \
 # autoremove: 自动清除在安装软件时为了满足软件依赖而安装并且现在已经不需要的dependencies
 # clean: 清除本地仓库中检索到的packages文件
 apt-get autoremove -y && apt-get clean && \
 #删除/var/cache和/var/lib/apt/lists两个文件夹里的所有文件
 rm -rf /var/cache/* && rm -rf /var/lib/apt/lists/* && \
 #dos2unix是一个文件转化器，将DOS/MAC系统的文件格式转为UNIX系统下的文件格式
 # dos2unix -n infile outfile ,将infile格式转化然后输出到outfile中
 dos2unix -n /init.sh /initx.sh && chmod +x /initx.sh

#用来指定容器启动时执行命令
ENTRYPOINT ["/initx.sh"]
