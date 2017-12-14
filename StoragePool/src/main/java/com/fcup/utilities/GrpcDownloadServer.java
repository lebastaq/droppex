package com.fcup.utilities;

import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

public class GrpcDownloadServer extends downloaderGrpc.downloaderImplBase{
    @Override
    public void startDownloader(Info request, StreamObserver<Status> responseObserver) {
        responseObserver.onNext(startDownload(request));
        responseObserver.onCompleted();
    }

    private Status startDownload(Info request) {
        return Status.newBuilder().setOk(false).build();
    }

}
