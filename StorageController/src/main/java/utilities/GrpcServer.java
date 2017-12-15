package utilities;

import com.fcup.StoragePoolRegisterer;
import io.grpc.ServerBuilder;

public class GrpcServer {
    int port = 50052;
    private io.grpc.Server server;

    public GrpcServer() {
    }

    public void startGrpcServer() throws Exception {
        server = ServerBuilder.forPort(port)
                .addService(new StoragePoolRegisterer())
                .build()
                .start();
        System.out.println("Grpc server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
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
