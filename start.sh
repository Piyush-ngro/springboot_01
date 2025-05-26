#!/bin/bash

# Start Sysdig with proper redirection
sysdig -w /tmp/sysdig_capture.scap > /var/log/sysdig.log 2>&1 &

# Verify Sysdig is running
sleep 2
if ! pgrep -x "sysdig" >/dev/null; then
    echo "Sysdig failed to start!" >&2
    exit 1
fi

# Start Tomcat
exec catalina.sh run
