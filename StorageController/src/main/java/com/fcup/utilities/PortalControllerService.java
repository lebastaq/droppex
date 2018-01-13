package com.fcup.utilities;

import com.fcup.DbManager;
import com.fcup.Shard;
import com.fcup.StorageController;
import com.fcup.StoragePoolsManager;
import com.fcup.generated.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.sql.SQLException;
import java.util.*;

public class PortalControllerService extends PortalControllerGrpc.PortalControllerImplBase {
    private final DbManager db;

    public PortalControllerService() throws SQLException, ClassNotFoundException {
        db = new DbManager();
        db.connect();
    }

    @Override
    public void deleteFile(DeletionRequest request, StreamObserver<Empty> responseObserver) {
        String fileToDelete = request.getFilename();

        try {
            StorageController sc = StorageController.getController();

            if(!sc.enoughGroupMembersOnlineToAnswer())
                throw new Exception("Not enough storage controllers online!");

            // First get all the shards for the file
            Map<String, String> params = new HashMap<>();
            params.put("filename", fileToDelete);

            // Read all shards from the DB that match the filename received
            List<Shard> shards = db.readEntries(params);

            // Only take action if the file is in the DB, makes RPC call idempotent
            if (shards.size() > 0) {

                // For each resulting shard, send a message to delete it
                for (Shard shard : shards) {
                    sc.sendGroupMessageToDeleteShard(shard.getId());

                }

                // TODO: Has the deletion from the pools succeeded at this point?

                // Delete shards from the DB that match a filename
                db.deleteFileShards(fileToDelete);

            }

            // Send nothing back, only care about errors
            responseObserver.onNext(null);
            responseObserver.onCompleted();

        } catch (Exception e) {
            // Sending back an error via gRPC if the shard isn't deleted
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }


    }

    @Override
    public void fileSearch(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        // Not secure
        String pattern = request.getPattern();

        try {
            if(!StorageController.getController().enoughGroupMembersOnlineToAnswer())
                throw new Exception("Not enough storage controllers online!");

            List<Shard> files = db.searchFiles(pattern);

            for (Shard file : files) {
                SearchResponse resultFile = SearchResponse.newBuilder()
                        .setFilename(file.getFilename())
                        .setFileSize(file.getFilesize())
                        .build();

                responseObserver.onNext(resultFile);
            }

            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());

        }
    }
}
