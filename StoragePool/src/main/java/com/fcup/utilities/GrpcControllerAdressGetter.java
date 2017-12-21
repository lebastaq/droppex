package com.fcup.utilities;

import com.fcup.StoragePool;
import com.fcup.generated.*;
import com.fcup.generated.addressChangedStatus;
import com.fcup.generated.storageControllerInfo;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;

public class GrpcControllerAdressGetter extends addressgetterGrpc.addressgetterImplBase{
    private StoragePool storagePool;

    public GrpcControllerAdressGetter(StoragePool storagePool)
    {
        this.storagePool = storagePool;
    }

    @Override
    public void getAddress(storageControllerInfo request, StreamObserver<addressChangedStatus> responseObserver) throws InvalidProtocolBufferException {
        responseObserver.onNext(changeAddress(request));
        responseObserver.onCompleted();
    }

    private addressChangedStatus changeAddress(storageControllerInfo request) throws InvalidProtocolBufferException {
        System.out.println("Just got a new master controller address: " + request.getIp() + ", " + request.getPort());
        storagePool.changeMasterController(request.getIp(), request.getPort());
        return addressChangedStatus.newBuilder().setOk(true).build();
    }

    @Override
    public void startDownloader(Info request, StreamObserver<Status> responseObserver) {
        responseObserver.onNext(startDownload(request));
        responseObserver.onCompleted();
    }


    // called via GRPC by the storage controller (relaying the app server's message,
    // or downloading a file itself to reconstruct after a crash)
    private Status startDownload(Info request) {
//        DownloadInfo downloadInfo = new DownloadInfo(request.getIp(), request.getPort(), request.getChunkId());

//        System.out.println("Just got a grpc request: " + request.getIp() + ", " + request.getPort() + ", " + request.getChunkId());
//
//        Downloader downloader = new Downloader(STORAGE_FOLDER, downloadInfo);
//        downloader.run();
//        System.out.println("Started downloader !");
//
//        return Status.newBuilder().setOk(true).build();
    }

}
