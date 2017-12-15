package utilities;

import com.fcup.StoragePoolRegisterer;
import io.grpc.ServerBuilder;

public class GrpcServer {
    int port = 50051;
    private io.grpc.Server server;

    public GrpcServer() {
    }

    private void startGrpcServer() throws Exception {
        server = ServerBuilder.forPort(port)
                .addService(new StoragePoolRegisterer())
                .build()
                .start();
        System.out.println("Grpc server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                GrpcServer.this.stopServer();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stopServer() {
        if (server != null) {
            server.shutdown();
        }
    }
}
