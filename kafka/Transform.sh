#!/bin/bash
./build.sh
mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.RunningAverage #-Dexec.args="$HOME/.confluent/java.config"


