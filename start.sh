#!/bin/bash

echo "Starting sysdig capture in background..."
sysdig -w /tmp/sysdig_capture.scap &

echo "Starting Tomcat server..."
exec catalina.sh run
