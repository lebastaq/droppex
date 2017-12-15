package com.fcup.utilities;

import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

public class GrpcDownloadServer extends downloaderGrpc.downloaderImplBase{
    private String STORAGE_FOLDER;

    public GrpcDownloadServer(String STORAGE_FOLDER)
    {
        this.STORAGE_FOLDER = STORAGE_FOLDER;
    }

    @Override
    public void startDownloader(Info request, StreamObserver<Status> responseObserver) {
        responseObserver.onNext(startDownload(request));
        responseObserver.onCompleted();
    }


    // called via GRPC by the storage controller (relaying the app server's message,
    // or downloading a file itself to reconstruct after a crash)
    private Status startDownload(Info request) {
        DownloadInfo downloadInfo = new DownloadInfo(request.getIp(), request.getPort(), request.getChunkId());

        System.out.println("Just got a grpc request: " + request.getIp() + ", " + request.getPort() + ", " + request.getChunkId());

        Downloader downloader = new Downloader(STORAGE_FOLDER, downloadInfo);
        downloader.run();
        System.out.println("Started downloader !");

        return Status.newBuilder().setOk(true).build();
    }

}