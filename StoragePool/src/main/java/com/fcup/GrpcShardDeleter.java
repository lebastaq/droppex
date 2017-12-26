package com.fcup;

import com.fcup.generated.ShardDeletionStatus;
import com.fcup.generated.ShardId;
import com.fcup.generated.deleterGrpc;
import io.grpc.stub.StreamObserver;

public class GrpcShardDeleter extends deleterGrpc.deleterImplBase {
    StoragePool storagePool;

    public GrpcShardDeleter(StoragePool storagePool) {
        this.storagePool = storagePool;
    }

    @Override
    public void delete(ShardId request, StreamObserver<ShardDeletionStatus> responseObserver) {
        responseObserver.onNext(deleteShardFromPool(request));
        responseObserver.onCompleted();
    }

    private ShardDeletionStatus deleteShardFromPool(ShardId request) {
        try {
            storagePool.deleteShard(request.getId());
            return ShardDeletionStatus.newBuilder().setOk(true).build();
        } catch (Exception e) {
            System.out.println("Could not delete shard: ");
            e.printStackTrace();
        }

        return ShardDeletionStatus.newBuilder().setOk(false).build();
    }
}
