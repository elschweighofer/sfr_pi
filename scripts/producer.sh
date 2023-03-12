#!/bin/bash

docker-compose up -d
docker exec --interactive --tty broker \
kafka-topics --create --topic sensor_readings --bootstrap-server broker:9092
kafka-console-producer \
  --topic sensor_readings \
  --bootstrap-server broker:9092 \
  --property parse.key=true \
  --property key.separator=":"


