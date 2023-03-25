# DB

We decided early on using a NoSQL DB because our Avro Schema is really similar to JSON.
Here are some factors to consider when evaluating Couchbase as a storage solution for Kafka:
## Scalability: Couchbase is designed to scale horizontally, meaning it can distribute data across multiple nodes and handle high volumes of data traffic. 
This can be important if you expect your Kafka stream to grow in size or if you need to handle spikes in traffic.
## Performance: 
Couchbase offers high-performance data retrieval and querying, which can be beneficial for applications that require fast access to data. 
It also has a built-in caching system that can improve performance by keeping frequently accessed data in memory.
## Flexibility: 
Couchbase's document-oriented data model allows for flexible data storage and retrieval, which can be useful if your Kafka stream contains complex or unstructured data.
Another Aspect was an available Dockerfile to run it locally. (for different Plattforms, including arm)
## Integration: 
Couchbase has connectors for various data ingestion tools, including Kafka. https://docs.couchbase.com/kafka-connector/current/index.html
This means you can easily stream data from Kafka into Couchbase without having to write custom integration code.
In summary, Couchbase was a good choice for the backend, because it would also support directly writing a Kafka stream in the Database. (not implemented so far )

# Considered Alternatives:
## MongoDB: 
MongoDB is also a document-oriented NoSQL database that stores data in JSON-like documents. 
However, MongoDB has a more flexible query language and data model than Couchbase. 
It also has a larger community and ecosystem, with more third-party tools and integrations available.
We did not use MongoDB only because we were already familiar with it and wanted to try something new.

## Cassandra: 
Cassandra is a distributed NoSQL database that uses a ring architecture to distribute data across nodes. 
It's designed to handle large amounts of data and traffic, with built-in support for replication, sharding, and automatic data balancing. 
Cassandra has a wide range of use cases, from storing time-series data to powering large-scale web applications. 
However, Cassandra's data model is more limited than Couchbase's, as it only supports key-value and column-family data structures.
For our Project this would have been enough, because we only need key / value and no complicated queries.

## Redis: 
Redis is an in-memory NoSQL database that supports a variety of data structures, including strings, hashes, lists, and sets. 
It's designed for high-performance data storage and retrieval, with support for caching, pub/sub messaging, and distributed locking. 
Redis is often used for real-time applications, such as chat systems and gaming platforms. 
However, Redis's data model is less flexible than Couchbase's, and it doesn't support distributed transactions or query languages.

