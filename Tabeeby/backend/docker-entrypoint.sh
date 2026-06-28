#!/bin/sh
set -e

mkdir -p /app/uploads
chown -R spring:spring /app/uploads

exec runuser -u spring -- /opt/java/openjdk/bin/java \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -jar /app/app.jar
