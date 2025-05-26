#!/bin/bash

# Start Sysdig capture with nohup and log redirection
echo "Starting sysdig capture..."
nohup sysdig -w /tmp/sysdig_capture.scap > /tmp/sysdig.log 2>&1 &

# Ensure Sysdig starts before Tomcat
sleep 5

# Start Tomcat
echo "Starting Tomcat server..."
exec catalina.sh run
