package com.fcup.utilities;

import com.fcup.DbManager;
import com.fcup.Shard;
import com.fcup.generated.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.sql.SQLException;
import java.util.*;

public class PortalControllerService extends PortalControllerGrpc.PortalControllerImplBase {
    private final DbManager db;

    public PortalControllerService() {
        db = new DbManager();
    }

    @Override
    public void deleteFile(DeletionRequest request, StreamObserver<Empty> responseObserver) {
        String fileToDelete = request.getFilename();

        try {
            // TODO: Actually implement the file deletion, delete all shards

            responseObserver.onNext(null);
            responseObserver.onCompleted();

        } catch (Exception e) { // TODO: Use more specific exception
            // Sending back an error via gRPC if the shard isn't deleted
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }


    }

    @Override
    public void fileSearch(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        String pattern = request.getPattern();

        try {
            db.connect();
            List<Shard> files = db.readEntries();

            for (Shard file : files) {
                //
                String shardID = file.getId();

                if (shardID.indexOf(pattern) != -1) {
                    SearchResponse resultFile = SearchResponse.newBuilder()
                            .setFilename(shardID)
                            // TODO: Remove dummy data, store actual sizes and filename in sqlite?
                            .setFileSize(2128934L)
                            .build();

                    responseObserver.onNext(resultFile);
                }
            }

            responseObserver.onCompleted();

        } catch (SQLException | ClassNotFoundException e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());

        }
    }
}
