#!/usr/bin/env bash
set -euo pipefail
source config/env

/opt/java/jdk-23/bin/java -jar application/server.jar --spring.profiles.active=h2,ssl --logging.config=/app/config/log.xml&
echo $! > pid

