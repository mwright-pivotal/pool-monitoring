#!/bin/sh
set -e
kubectl config use-context ${CONTEXT}
kubectl get pods
kubectl delete -f venues/deployment.yaml --ignore-not-found=true
kubectl create -f venues/deployment.yaml