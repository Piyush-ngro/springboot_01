#!/bin/bash
# Start recording syscalls in background
sysdig -w /tmp/sysdig_capture.scap &

# Start Tomcat
exec catalina.sh run
