#!/usr/bin/env bash
set -euo pipefail
# To be run on server

./stop.sh

APP_DIR=/app/application
echo "Removing previous fallback"
rm -f "${APP_DIR}/server_old.jar" || true

echo "Renaming current version to old"
mv "${APP_DIR}/server.jar" "${APP_DIR}/server_old.jar" || true

echo "Renaming new version to current"
mv "${APP_DIR}/server_new.jar" "${APP_DIR}/server.jar"

echo "Starting server"
./start.sh

