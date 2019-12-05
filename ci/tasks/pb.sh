#!/bin/sh
set -e
pb --version

pb login -a ${PB_API} -u ${PB_USER} -p ${PB_PSWD} -k
echo ""