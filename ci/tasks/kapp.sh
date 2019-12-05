#!/bin/sh
set -e
mkdir -p ~/.kube
set +x
echo "------- KUBECONFIG ---------------------"
echo "$KUBECONFIG_CONTENTS" 
echo "$KUBECONFIG_CONTENTS" > ~/.kube/config
set -x
kapp deploy -a venues -f venues/k8s/deployment.yaml -c -y