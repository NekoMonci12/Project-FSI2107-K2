#!/bin/sh
set -e

Xvfb :1 -screen 0 1280x800x24 &
sleep 2

if [ -n "$VNC_PASSWORD" ]; then
  x11vnc -storepasswd "$VNC_PASSWORD" /tmp/vncpass
  VNC_AUTH="-rfbauth /tmp/vncpass"
else
  VNC_AUTH="-nopw"
fi

x11vnc -display :1 -forever -shared $VNC_AUTH -rfbport 5900 -bg -q
websockify --web=/usr/share/novnc 6080 localhost:5900 &

exec java -jar /app/app.jar