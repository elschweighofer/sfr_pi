# sfr_pi
This project will read temparature and humidity values from a sensor and push them to a kafka cluster. Java console apps then either conusme or transoform the values.


## Setup Step 1: Startup Kafka Cluster
Run 
- $ cd server
- $ docker-compose up 
To start all docker containers and the kafka cluster including a schema registry with avro. Wait for around 2 minutes to give the contianers a chance to boot up.
Check the health status with $ docker ps

## Setup Step 2: Producer, Transformer & Consumer
Java Maven Project features a transformer, 2 consumer and a producer each with Avro Schemas based on https://docs.confluent.io/platform/current/schema-registry/schema_registry_onprem_tutorial.html#schema-definition

The transformer is a running average of temparatures, its comitting the values into an own Avro schema.

### Build with 
- $ cd server # if not already there
- $ ./build.sh # or $ mvn clean compile package

### Run with 
Use the multiple startup scripts to run the producer and all consumers and transformers.

### Check with
You can navigate to http://localhost:9021 to acces the gui webservice. It provides the status of our cluster and all topics and messages.

## Python 

-
-
-
under construction



