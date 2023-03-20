#!/bin/bash
./build.sh
mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ConsumerRunningAverage #-Dexec.args="$HOME/.confluent/java.config"


