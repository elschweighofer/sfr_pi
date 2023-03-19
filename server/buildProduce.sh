#!/bin/bash
mvn clean compile package

mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ProducerTemperature #-Dexec.args="$HOME/.confluent/java.config"



