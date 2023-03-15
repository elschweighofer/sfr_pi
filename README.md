# sfr_pi

## Setup Kafka
Docker Compose in the Sever Folder for included Schema Registry, otherwise use Startup Script
## Server
Java Maven Project with Consumer, Producer and Avro Schemas based on https://docs.confluent.io/platform/current/schema-registry/schema_registry_onprem_tutorial.html#schema-definition
Build with 
$ cd server
$ mvn clean compile package

Run with 
$ mvn exec:java -Dexec.mainClass=io.confluent.examples.clients.basicavro.ProducerExample \
  -Dexec.args="$HOME/.confluent/java.config"
(java.config is optional when Kafka Broker listens on localhost:9092)

Config File:

'# Required connection configs for Kafka producer, consumer, and admin
bootstrap.servers=localhost:9092
security.protocol=PLAINTEXT
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='{{ CLUSTER_API_KEY }}' password='{{ CLUSTER_API_SECRET }}';
sasl.mechanism=PLAIN
# Required for correctness in Apache Kafka clients prior to 2.6
client.dns.lookup=use_all_dns_ips

# Best practice for higher availability in Apache Kafka clients prior to 3.0
session.timeout.ms=45000

# Best practice for Kafka producer to prevent data loss
acks=all

# Required connection configs for Confluent Cloud Schema Registry
schema.registry.url=http://0.0.0.0:8081'


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


