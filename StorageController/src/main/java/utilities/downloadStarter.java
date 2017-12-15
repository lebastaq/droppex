package utilities;

import com.fcup.generated.Info;
import com.fcup.generated.Status;
import com.fcup.generated.downloaderGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class downloadStarter {

    private final ManagedChannel grpcChannel;
    private downloaderGrpc.downloaderBlockingStub downloaderStarterStub;

    public downloadStarter(String host, int port) throws Exception {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build());
    }

    public downloadStarter(ManagedChannel channel) throws Exception {
        this.grpcChannel = channel;
        downloaderStarterStub = downloaderGrpc.newBlockingStub(this.grpcChannel);
    }

    public Status getAnswer(Info request) {
        return downloaderStarterStub.startDownloader(request);
    }
}
