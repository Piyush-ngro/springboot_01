#!/bin/bash

echo "Starting sysdig capture in background..."
sysdig -w /tmp/sysdig_capture.scap &
disown %1  # Disown the backgrounded Sysdig process

echo "Starting Tomcat server..."
exec catalina.sh run  # Properly handle signals with exec
