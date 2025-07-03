#!/usr/bin/env bash
set -euo pipefail

source remote_env

FILE_TO_UPLOAD=$(ls ../server/build/libs/server*-SNAPSHOT.jar)
REMOTE_TARGET=${REMOTE_HOST}:${UPLOAD_DIR}/server_new.jar

echo "Uploading $FILE_TO_UPLOAD to ${REMOTE_TARGET}"
scp "$FILE_TO_UPLOAD" "${REMOTE_TARGET}"
