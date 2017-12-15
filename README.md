# droppex
Distributed file hosting


# Quentin notes
## Storage pool
* Creating downloaders as thread to be non-blocking, and be able to download multiple files at the same time
* Seeding: to other pools (tested), to app server
* Downloading: from other pools (tested), from app server

Upon start, announces itself to the storage controllers. Gets their address via the google bucket.