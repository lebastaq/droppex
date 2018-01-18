# What ?
Droppex is a simple storage facility. The goal is to provide a redundant storage system, resistant to failures.
The system implements Forward Error Correction using the [Reed-Solomon algorithm](https://www.backblaze.com/blog/reed-solomon/).

[Link to the paper](https://github.com/lebastaq/droppex/blob/master/paper.pdf)

# Architecture
![architecture](https://github.com/lebastaq/droppex/blob/master/architecture.png)

# Components
* [App Server](app-server): provide a single point of entry for the clients. REST server coded in golang that relay the requests to the master storage controller using Grpc
* [Storage Controller](StorageController): provide a group of controllers responsible to manage the storage pools. They communicate between each other using JGroups, and with the storage pools using Grpc. Responsible for chunking, hashing files, and applying the Reed-Solomon algorithm. Uses a local SQLite database for storage.
* [Storage Pool](StoragePool): provide a storage facility, that accepts uploads and downloads. Can be started independently of the controllers, and then registered.
* [Client](client): provide a simple API that enables the user to upload and download files. Coded in golang

# Deployment 
The app server, storage controller, and app server are all deployed on Google VMs using the compute engine.

# Contributors
* @lebastaq
* @freddygv
