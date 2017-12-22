# droppex
Distributed file hosting


# Quentin notes
## Storage pool
* Creating downloaders as thread to be non-blocking, and be able to download multiple files at the same time
* Seeding: to other pools (tested), to app server
* Downloading: from other pools (tested), from app server

Upon start, announces itself to the storage controllers. 
Tried to gets their address via the google bucket, but not working...

Storage controllers start with option specifying the master controller's adress

* Read from bucket ? Simplest...
* When new master controller, send grpc messages to storage pools to give them the new master ?

Problem scenario:
- Storage pool connects and stays empty
- New storage controller connects - get all the shards, but doesn't know the storage pool because she has not uploaded yet...
Solution: When a new storage pool connects, add a shard with id 0 to the storage controller database to make the storage pool known to every controller

## File redundancy
Multiple solutions evaluated:
* Have the storage pools ping the storage controller (heartbeat)
* Have the storage controller recompute the file and reassign the chunks dinamically
* Have the app server try to download all the chunks. If some are missing, reconstruct them and ask the storage controller where to put them

Chosen solution: 3

## File deletion and synchronicity
I'm storing the shards in a database, but not the operations. 

That means that if a storage controller goes down, and a file is deleted, when it gets back up it will not know that the file was deleted 
(eg. the other controllers would need to store in the database that the file was deleted)

That means that it will try to sync with the others and **send them the file info again to store in database**

There are two options to solve that:
- add an operation type, and log the PUT(shard).. then DELETE(shard), with a timestamp so that the storage controller knows which operation comes first
- consider that when a storage controller goes down, it will not come back up again, and don't do anything considering the deletion of the file

At the moment I'm only going to implement the second solution as I'm short on time