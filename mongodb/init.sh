#!/bin/bash
#test -z String 如果传入字符串的长度为0就返回true
if test -z "$MONGODB_PASSWORD"; then
    echo "MONGODB_PASSWORD not defined" #输出"MONGODB_PASSWORD not defined"
    # exit 1 表示有错误
    exit 1
fi
# 变量赋值
auth="-u user -p $MONGODB_PASSWORD"

# MONGODB USER CREATION
# (命令)表示创建一个子shell(subshell)后面加一个&表示在后台运行,下面有两个()&表示这两个subshell会同时在后台运行
(
echo "setup mongodb auth"
# 变量赋值
create_user="if (!db.getUser('user')) { db.createUser({ user: 'user', pwd: '$MONGODB_PASSWORD', roles: [ {role:'readWrite', db:'piggymetrics'} ]}) }"
#until [CONDITION]
#do
#  [COMMANDS]
#done
# until循环，先判断CONDITION如果返回ture结束循环，如果返回false执行COMMANDS
# mongo admin 指定连接本机上的admin database ,想了解连接远端可以看：https://docs.mongodb.com/manual/reference/program/mongo/#mongo-shell-options
# --eval 将后面传入的javascript代码执行
until mongo piggymetrics --eval "$create_user" || mongo piggymetrics $auth --eval "$create_user"; do sleep 5; done
# 杀死mongod进程
killall mongod
# 睡一秒
sleep 1
# 强制结束mongod进程
killall -9 mongod
) &

# INIT DUMP EXECUTION
(
# test - n String :字符串长度不为0返回true
# 在启动account-service微服务的时候在docker-compose.yml文件中引入了这个环境变量
# INIT_DUMP: account-service-dump.js
#这里为了给打开了访问控制的数据库，添加一个id为demo的数据
if test -n "$INIT_DUMP"; then
    echo "execute dump file"
	until mongo piggymetrics $auth $INIT_DUMP; do sleep 5; done
fi
) &
echo "start mongodb without auth"
# -R 表示递归的将/data/db文件夹下的所有文件的Owner改为mongodb
chown -R mongodb /data/db
# 用mongodb的身份来启动数据库
# $@:指定是外部传入的所有参数 : 在这里$@为空
# 详细可以看这篇文章:https://cloud.tencent.com/developer/article/1454344

#这个数据库没有打开访问控制，打开数据库是为了创建一个权限为readWrite的用户user
gosu mongodb mongod "$@"

echo "restarting with auth on"
sleep 5
# 用mongodb的身份来执行预处理逻辑脚本 --auth "@" 是作为参数传入脚本中，
# 脚本有一个set --mongod "$@" 表示就是将$@设置为 mongodb --auth "$@"(这个$@是外部传入的)
# docker-entrypoint.sh执行完预处理后，最后有一个命令exec $@ 这相当于exec mongod --auth "$@"
# --auth 表示打开访问控制
exec gosu mongodb /usr/local/bin/docker-entrypoint.sh --auth "$@"
