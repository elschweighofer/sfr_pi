#!/bin/bash
mvn clean compile package

mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ProducerTemperature #-Dexec.args="$HOME/.confluent/java.config"
#mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ConsumerTemperature -Dexec.args="$HOME/.confluent/java.config" & disown
#mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.TemperatureAggregator -Dexec.args="$HOME/.confluent/java.config"


