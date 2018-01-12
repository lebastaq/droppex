package com.fcup.utilities;
import com.fcup.Shard;
import com.fcup.StoragePoolsManager;
import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

// TODO
class StoragePoolRegisterer extends registererGrpc.registererImplBase {

    private final StoragePoolsManager storagePoolsManager;

    public StoragePoolRegisterer(StoragePoolsManager storagePoolsManager) {
        this.storagePoolsManager = storagePoolsManager;
    }

    @Override
    public void register(PoolInfo request, StreamObserver<PoolRegistrationStatus> responseObserver) {
        System.out.println("Received new Registration message from pool...");
        if (storagePoolsManager.enoughGroupMembersOnlineToAnswer()) {
            responseObserver.onNext(registerPool(request));
            responseObserver.onCompleted();
        }
        else{
            responseObserver.onNext(failToRegisterPool(request));
            responseObserver.onCompleted();
        }
    }

    private PoolRegistrationStatus failToRegisterPool(PoolInfo request) {
        return PoolRegistrationStatus.newBuilder().setOk(false).build();
    }

    private PoolRegistrationStatus registerPool(PoolInfo request) {
        Shard shardRegister = new Shard();
        shardRegister.changeKeyValue("shardID", "0");
        shardRegister.changeKeyValue("storagePoolPort", Integer.toString(request.getPort()));
        shardRegister.changeKeyValue("storagePoolIP", request.getIp());
        storagePoolsManager.sendMessage(shardRegister);
        return PoolRegistrationStatus.newBuilder().setOk(true).build();
    }
}
