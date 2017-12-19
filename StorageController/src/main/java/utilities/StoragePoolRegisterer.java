package utilities;
import com.fcup.generated.*;
import io.grpc.stub.StreamObserver;

// TODO
public class StoragePoolRegisterer extends registererGrpc.registererImplBase {
    public StoragePoolRegisterer() {
    }

    @Override
    public void register(PoolInfo request, StreamObserver<com.fcup.generated.PoolRegistrationStatus> responseObserver) {
        System.out.println("Received pool storage registration");
        // TODO register the pool storage in local (insert in db and send operation in jgroups)!!
    }
}
