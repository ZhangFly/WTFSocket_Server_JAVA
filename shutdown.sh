#!/bin/bash

JAR_NAME="TestServer"

ps -ef |grep "$JAR_NAME" |grep -v grep |awk '{print "kill " $2}' |sh

s=`ps -ef |grep "$JAR_NAME" |grep -v grep`
while [ -n "$s" ]
do
	echo "shutdwoning..."
	sleep 0.5s
	s=`ps -ef |grep "$JAR_NAME" |grep -v grep`
done

echo "$JAR_NAME shutdwoned"