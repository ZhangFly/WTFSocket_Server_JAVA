#!/bin/bash

JAR_NAME="Main.jar"

nohup java -jar "$JAR_NAME" &

echo "$JAR_NAME started"