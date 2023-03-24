#!/bin/bash
mvn exec:java -Dexec.mainClass=backend.kafka.ConsumerTemperature #-Dexec.args="$HOME/.confluent/java.config"


