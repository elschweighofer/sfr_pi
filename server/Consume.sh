#!/bin/bash
mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ConsumerTemperature #-Dexec.args="$HOME/.confluent/java.config"


