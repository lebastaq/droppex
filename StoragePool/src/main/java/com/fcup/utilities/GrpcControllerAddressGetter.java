package com.fcup.utilities;

import com.fcup.StoragePool;
import com.fcup.generated.*;
import com.fcup.generated.addressChangedStatus;
import com.fcup.generated.storageControllerInfo;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;

public class GrpcControllerAddressGetter extends addressgetterGrpc.addressgetterImplBase{
    private StoragePool storagePool;

    public GrpcControllerAddressGetter(StoragePool storagePool)
    {
        this.storagePool = storagePool;
    }

    @Override
    public void setAddress(storageControllerInfo request, StreamObserver<addressChangedStatus> responseObserver) {
        responseObserver.onNext(changeMasterController(request));
        responseObserver.onCompleted();
    }

    private addressChangedStatus changeMasterController(storageControllerInfo request) {
        try {
            return changeAddress(request);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    private addressChangedStatus changeAddress(storageControllerInfo request) throws InvalidProtocolBufferException {
        System.out.println("Just got a new master controller address: " + request.getIp() + ", " + request.getPort());
        storagePool.changeMasterController(request.getIp(), request.getPort());
        return addressChangedStatus.newBuilder().setOk(true).build();
    }

}