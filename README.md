# sfr_pi
## Build project
Java Maven Project with Consumer, Producer and Avro Schemas based on https://docs.confluent.io/platform/current/schema-registry/schema_registry_onprem_tutorial.html#schema-definition
Build with 
$ cd server
$ mvn clean compile package


### optionally create a custom config file
By default, the project will use the config file in <project_dir>/server/config/java.config
enable the commented out line in the script or directly run with 
$ mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ProducerExample \
  -Dexec.args="$<pathToConfig>.confluent/java.config"
(java.config is optional when Kafka Broker listens on localhost:9092)


## Start containers
### Setup Docker on Raspberry
- You need a running docker and docker-compose installed: https://dev.to/elalem>
- $ cd server
- $ ./startup.sh or $ docker-compose up

### Access the control center
- To access a UI that gives you information about topics and the kafka cluster
 navigate to http://localhost:9021

## start producing, transforming and consuming
- Navigate to the server folder $ cd server
- For the producer, transformer and the consumer you will find a script for eac>
- Start the scripts and keep them running, check out the kafka control center h>


## Python 

-
-
-
under construction



### Setup Docker on Raspberry
- You need a running docker and docker-compose installed: https://dev.to/elalemanyo/how-to-install-docker-and-docker-compose-on-raspberry-pi-1mo


## Pi python stetup for dev
- cd to the directory where requirements.txt is located.
- activate your virtualenv.
- run: pip install -r requirements.txt in your shell.
- execute sensor.py

### Setup Env
- set the KAFKA_BROKER variable `$ KAFKA_BROKER = IPOFKAFKA`


