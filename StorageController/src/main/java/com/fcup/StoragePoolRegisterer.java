package com.fcup;
import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

// TODO
public class StoragePoolRegisterer extends registererGrpc.registererImplBase {
    public StoragePoolRegisterer() {
    }

    @Override
    public void register(PoolInfo request, StreamObserver<com.fcup.generated.PoolRegistrationStatus> responseObserver) {
        System.out.println("Received pool storage registration");
        // register the pool storage !!
    }
}
