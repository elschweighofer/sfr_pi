#!/bin/bash

docker exec --interactive --tty broker \
kafka-console-producer \
  --topic sensor_readings \
  --bootstrap-server broker:9092 \
  --property parse.key=true \
  --property key.separator=":"


