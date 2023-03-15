#!/bin/bash
mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.TemperatureAggregator -Dexec.args="$HOME/.confluent/java.config"


