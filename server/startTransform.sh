#!/bin/bash
CURR_OS=$(./detectOs.sh)
CONFIG_PATH=''

./build.sh

if [[ $CURR_OS = "Windows" ]];
then
    echo "Using Windows for path"
    CONFIG_PATH=.\\config\\java.config

else
    echo "Using Linux pFath"
    CONFIG_PATH="$./.config/java.config"
fi


mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.TemperatureAggregator -Dexec.args="$CONFIG_PATH"


