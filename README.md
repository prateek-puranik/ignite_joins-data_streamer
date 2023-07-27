# Apache Ignite SQL Queries and Data Streaming

Apache Ignite enables lightning-fast SQL queries and efficient data streaming capabilities in a distributed computing environment. The project seamlessly integrates distributed joins, aggregation, and Apache DataStreamer to load bulk data into the Ignite cluster efficiently.


## Getting Started

### Apache Ignite Joins
1. This project requires the entire setup made in the CDC project. So please refer to the installation setup of that project especially the staring the Ignite Cluster part.
2. This project requires the use of at least 2 Ignite nodes, in order to demonstrate distributed joins, affinity keys, etc.
3. The sample caches and schemas are loaded onto the cluster on start. One can call the APIs localhost:<server-port>/sqlQuery.. to get the results of the various joins and aggregation queries.

### Data Streamer
1. Apache Ignite can load bulk data using 2 methods
   - [Streaming through a JDBC Thin Client](https://ignite.apache.org/docs/latest/sql-reference/operational-commands#set-streaming)
   - [Data Streamer](https://ignite.apache.org/docs/latest/data-streaming)

2. We have used the faster data streamer process to load a large [Banking Dataset](https://www.kaggle.com/datasets/ksabishek/massive-bank-dataset-1-million-rows) of 1 million+ rows in batches from XLSX.
3. A similar process can be used to stream data from Oracle or JMS into an Ignite Cluster. 

