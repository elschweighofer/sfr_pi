#!/bin/bash
cd ../server/
quarkus build
echo "*"
echo "################### Building docker image #####################"
echo "*"

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/kafka-avro-schema-quickstart-jvm .