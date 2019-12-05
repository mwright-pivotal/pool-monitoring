#!/bin/sh
set -e
export ROOT_FOLDER=$( pwd )
export REPO=repo

M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"
[[ -d "${M2_CACHE}" && ! -d "${M2_HOME}" ]] && ln -s "${M2_CACHE}" "${M2_HOME}"

cd venues
mvn package
cp target/venue-0.0.1-SNAPSHOT.jar ../build-output/
cp manifest.yml ../build-output/