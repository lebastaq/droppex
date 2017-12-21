package com.fcup.utilities;
import com.fcup.Shard;
import com.fcup.StoragePoolsManager;
import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

// TODO
public class StoragePoolRegisterer extends registererGrpc.registererImplBase {

    private final StoragePoolsManager storagePoolsManager;

    public StoragePoolRegisterer(StoragePoolsManager storagePoolsManager) {
        this.storagePoolsManager = storagePoolsManager;
    }

    @Override
    public addressChangedStatus register(PoolInfo request, StreamObserver<PoolRegistrationStatus> responseObserver) {
        System.out.println("Received new Registration message from pool...");
        Shard shardRegister = new Shard();
        shardRegister.changeKeyValue("shardID", "0");
        shardRegister.changeKeyValue("storagePoolPort", Integer.toString(request.getPort()));
        shardRegister.changeKeyValue("storagePoolIP", request.getIp());
        storagePoolsManager.sendMessage(shardRegister);

        return addressChangedStatus.newBuilder().setOk(true).build();
    }
}
