package com.fcup.generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.8.0)",
    comments = "Source: leader.proto")
public final class LeaderServiceGrpc {

  private LeaderServiceGrpc() {}

  public static final String SERVICE_NAME = "leader.LeaderService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAnnounceLeaderMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.LeaderIP,
      com.fcup.generated.EmptyReply> METHOD_ANNOUNCE_LEADER = getAnnounceLeaderMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.LeaderIP,
      com.fcup.generated.EmptyReply> getAnnounceLeaderMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.LeaderIP,
      com.fcup.generated.EmptyReply> getAnnounceLeaderMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.LeaderIP, com.fcup.generated.EmptyReply> getAnnounceLeaderMethod;
    if ((getAnnounceLeaderMethod = LeaderServiceGrpc.getAnnounceLeaderMethod) == null) {
      synchronized (LeaderServiceGrpc.class) {
        if ((getAnnounceLeaderMethod = LeaderServiceGrpc.getAnnounceLeaderMethod) == null) {
          LeaderServiceGrpc.getAnnounceLeaderMethod = getAnnounceLeaderMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.LeaderIP, com.fcup.generated.EmptyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "leader.LeaderService", "AnnounceLeader"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.LeaderIP.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.EmptyReply.getDefaultInstance()))
                  .setSchemaDescriptor(new LeaderServiceMethodDescriptorSupplier("AnnounceLeader"))
                  .build();
          }
        }
     }
     return getAnnounceLeaderMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LeaderServiceStub newStub(io.grpc.Channel channel) {
    return new LeaderServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LeaderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LeaderServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LeaderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LeaderServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class LeaderServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Forwards new leader IP after election
     * </pre>
     */
    public void announceLeader(com.fcup.generated.LeaderIP request,
        io.grpc.stub.StreamObserver<com.fcup.generated.EmptyReply> responseObserver) {
      asyncUnimplementedUnaryCall(getAnnounceLeaderMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAnnounceLeaderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.LeaderIP,
                com.fcup.generated.EmptyReply>(
                  this, METHODID_ANNOUNCE_LEADER)))
          .build();
    }
  }

  /**
   */
  public static final class LeaderServiceStub extends io.grpc.stub.AbstractStub<LeaderServiceStub> {
    private LeaderServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeaderServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeaderServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeaderServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards new leader IP after election
     * </pre>
     */
    public void announceLeader(com.fcup.generated.LeaderIP request,
        io.grpc.stub.StreamObserver<com.fcup.generated.EmptyReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAnnounceLeaderMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class LeaderServiceBlockingStub extends io.grpc.stub.AbstractStub<LeaderServiceBlockingStub> {
    private LeaderServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeaderServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeaderServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeaderServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards new leader IP after election
     * </pre>
     */
    public com.fcup.generated.EmptyReply announceLeader(com.fcup.generated.LeaderIP request) {
      return blockingUnaryCall(
          getChannel(), getAnnounceLeaderMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class LeaderServiceFutureStub extends io.grpc.stub.AbstractStub<LeaderServiceFutureStub> {
    private LeaderServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeaderServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeaderServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeaderServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards new leader IP after election
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.EmptyReply> announceLeader(
        com.fcup.generated.LeaderIP request) {
      return futureUnaryCall(
          getChannel().newCall(getAnnounceLeaderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANNOUNCE_LEADER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LeaderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LeaderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ANNOUNCE_LEADER:
          serviceImpl.announceLeader((com.fcup.generated.LeaderIP) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.EmptyReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class LeaderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LeaderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.LeaderRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LeaderService");
    }
  }

  private static final class LeaderServiceFileDescriptorSupplier
      extends LeaderServiceBaseDescriptorSupplier {
    LeaderServiceFileDescriptorSupplier() {}
  }

  private static final class LeaderServiceMethodDescriptorSupplier
      extends LeaderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    LeaderServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (LeaderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LeaderServiceFileDescriptorSupplier())
              .addMethod(getAnnounceLeaderMethod())
              .build();
        }
      }
    }
    return result;
  }
}
