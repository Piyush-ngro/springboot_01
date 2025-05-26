#!/bin/bash

# Start sysdig capture in background (captures all container activity)
sysdig -w /tmp/sysdig_capture.scap "evt.type=open" &

# Start Tomcat normally
exec catalina.sh run
