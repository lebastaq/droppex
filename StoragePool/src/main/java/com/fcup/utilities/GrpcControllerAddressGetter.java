package com.fcup.utilities;

import com.fcup.StoragePool;
import com.fcup.generated.*;
import com.fcup.generated.addressChangedStatus;
import com.fcup.generated.storageControllerInfo;
import com.google.protobuf.InvalidProtocolBufferException;

public class GrpcControllerAddressGetter extends addressgetterGrpc.addressgetterImplBase{
    private StoragePool storagePool;

    public GrpcControllerAddressGetter(StoragePool storagePool)
    {
        this.storagePool = storagePool;
    }


    public addressChangedStatus getAddress(storageControllerInfo request) {
        System.out.println("Received request !");
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
