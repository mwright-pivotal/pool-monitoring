#!/bin/sh
set -e
mkdir -p ~/.kube
set +x
echo "------- KUBECONFIG ---------------------"
echo "$KUBECONFIG_CONTENTS" 
echo "$KUBECONFIG_CONTENTS" > ~/.kube/config
set -x
kapp deploy -a pool-monitor -f pool-monitor/k8s/ -c -y