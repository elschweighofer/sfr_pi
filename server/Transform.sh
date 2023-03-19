#!/bin/bash
./build.sh
mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.OLDTemperatureAggregator #-Dexec.args="$HOME/.confluent/java.config"


