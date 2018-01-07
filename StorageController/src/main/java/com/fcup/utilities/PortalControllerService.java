package com.fcup.utilities;

import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class PortalControllerService extends PortalControllerGrpc.PortalControllerImplBase {

    @Override
    public void deleteFile(DeletionRequest request, StreamObserver<Empty> responseObserver) {
        String fileToDelete = request.getFilename();

        // TODO: Actually delete the file
        // TODO: Send error if there's an issue

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void fileSearch(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        String pattern = request.getPattern();

        // TODO: Remove this fake data
        Map<String, Long> results = new HashMap<>();
        results.put("Billsz", 2128934L);
        results.put("Bill", 238934L);
        results.put("Bob", 432L);
        results.put("Claire", 389L);

        for (Map.Entry<String, Long> entry : results.entrySet()) {

            if (entry.getKey().indexOf(pattern) != -1) {
                SearchResponse resultFile = SearchResponse.newBuilder()
                        .setFilename(entry.getKey())
                        .setFileSize(entry.getValue())
                        .build();

                responseObserver.onNext(resultFile);
            }
        }

        responseObserver.onCompleted();
    }
}
