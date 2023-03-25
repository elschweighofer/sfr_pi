## Q1 AOT (Ahead-of-time) VS JIT (Just-in-time) 

AOT compilation is a good choice when performance and memory usage are important. By compiling the code before it runs, the system can be optimized for performance 
and use less memory. AOT-compiled code is also harder to decompile than JIT-compiled code, making it better for high-security applications. Because of the better 
startup Time of native binaries compared to the JVM AOT is better suited to cloud environments and FaaS.

On the other hand, JIT compilation is ideal when dynamic optimization is necessary, e.g. in web applications. JIT compilation can optimize the performance of the 
application by compiling the code into machine code just before it is executed, resulting in faster execution times. Additionally, JIT compilation can support hot 
reloading, which allows developers to make changes to the code and see the results immediately without restarting the application.

In summary, the choice between AOT and JIT compilation depends on the specific needs of the system. If performance and memory usage are critical, AOT compilation may 
be the best choice. If dynamic optimization is needed, JIT compilation may be better.

To summarize, for our Backend System which mainly deals with a Kafka Stream and stores data in a Database, Ahead of Time Compilation would be preferable. By reducing 
overhead, the Database can be updated closer to real-time data. While the App is still in inital development it is useful to have hot reloading and less compile time. 
Because of this we have not switched to an AOT Approach so far.

## Q2Is it possible to also achieve this demand (store data from Kafka in a DB) with a Kafka Streams Application?

Yes, it's possible to store data from Kafka in a database using a Kafka Streams application. Kafka Streams is a Java library for building real-time streaming 
applications that can process data from Kafka topics and write the output to a database. It is also possible to use Kafka as persistence Service because Kafka is 
fault tolerant (Cluster) and persists the messages on Disk. It's also possible to consume a topic -from-beginning, returning every message from the start of the 
topic.
## Q3 Would a Lambda (FaaS) be a promising approach for the given demand at hand?

Using a Lambda (FaaS) for this demand can also be a good approach, depending on the use case and requirements. AWS Lambda or Google Cloud Functions can be used to 
build serverless applications that can process data in real-time. However, it's important to keep in mind that using a FaaS solution may introduce additional 
complexity and require more effort to set up compared to using Kafka Streams, which is a simpler and more lightweight option.
