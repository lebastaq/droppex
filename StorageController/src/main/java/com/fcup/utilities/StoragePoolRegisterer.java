package com.fcup.utilities;
import com.fcup.Shard;
import com.fcup.StorageController;
import com.fcup.StoragePoolsManager;
import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

// TODO
class StoragePoolRegisterer extends registererGrpc.registererImplBase {

    public StoragePoolRegisterer() {
    }

    @Override
    public void register(PoolInfo request, StreamObserver<PoolRegistrationStatus> responseObserver) {
        System.out.println("Received new Registration message from pool...");
        if (StorageController.getController().enoughGroupMembersOnlineToAnswer()) {
            responseObserver.onNext(registerPool(request));
            responseObserver.onCompleted();
        }
        else{
            responseObserver.onNext(failToRegisterPool());
            responseObserver.onCompleted();
        }
    }

    private PoolRegistrationStatus failToRegisterPool() {
        return PoolRegistrationStatus.newBuilder().setOk(false).build();
    }

    private PoolRegistrationStatus registerPool(PoolInfo request) {
        Shard shardRegister = new Shard();
        shardRegister.changeKeyValue("shardID", "0");
        shardRegister.changeKeyValue("storagePoolPort", Integer.toString(request.getPort()));
        shardRegister.changeKeyValue("storagePoolIP", request.getIp());
        StorageController.getController().sendMessage(shardRegister);
        return PoolRegistrationStatus.newBuilder().setOk(true).build();
    }
}
