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
    comments = "Source: shardDeleter.proto")
public final class deleterGrpc {

  private deleterGrpc() {}

  public static final String SERVICE_NAME = "com.fcup.generated.deleter";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.ShardId,
      com.fcup.generated.ShardDeletionStatus> METHOD_DELETE = getDeleteMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.ShardId,
      com.fcup.generated.ShardDeletionStatus> getDeleteMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.ShardId,
      com.fcup.generated.ShardDeletionStatus> getDeleteMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.ShardId, com.fcup.generated.ShardDeletionStatus> getDeleteMethod;
    if ((getDeleteMethod = deleterGrpc.getDeleteMethod) == null) {
      synchronized (deleterGrpc.class) {
        if ((getDeleteMethod = deleterGrpc.getDeleteMethod) == null) {
          deleterGrpc.getDeleteMethod = getDeleteMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.ShardId, com.fcup.generated.ShardDeletionStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.fcup.generated.deleter", "Delete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.ShardId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.ShardDeletionStatus.getDefaultInstance()))
                  .setSchemaDescriptor(new deleterMethodDescriptorSupplier("Delete"))
                  .build();
          }
        }
     }
     return getDeleteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static deleterStub newStub(io.grpc.Channel channel) {
    return new deleterStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static deleterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new deleterBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static deleterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new deleterFutureStub(channel);
  }

  /**
   */
  public static abstract class deleterImplBase implements io.grpc.BindableService {

    /**
     */
    public void delete(com.fcup.generated.ShardId request,
        io.grpc.stub.StreamObserver<com.fcup.generated.ShardDeletionStatus> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDeleteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.ShardId,
                com.fcup.generated.ShardDeletionStatus>(
                  this, METHODID_DELETE)))
          .build();
    }
  }

  /**
   */
  public static final class deleterStub extends io.grpc.stub.AbstractStub<deleterStub> {
    private deleterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private deleterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected deleterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new deleterStub(channel, callOptions);
    }

    /**
     */
    public void delete(com.fcup.generated.ShardId request,
        io.grpc.stub.StreamObserver<com.fcup.generated.ShardDeletionStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class deleterBlockingStub extends io.grpc.stub.AbstractStub<deleterBlockingStub> {
    private deleterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private deleterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected deleterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new deleterBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fcup.generated.ShardDeletionStatus delete(com.fcup.generated.ShardId request) {
      return blockingUnaryCall(
          getChannel(), getDeleteMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class deleterFutureStub extends io.grpc.stub.AbstractStub<deleterFutureStub> {
    private deleterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private deleterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected deleterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new deleterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.ShardDeletionStatus> delete(
        com.fcup.generated.ShardId request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DELETE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final deleterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(deleterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DELETE:
          serviceImpl.delete((com.fcup.generated.ShardId) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.ShardDeletionStatus>) responseObserver);
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

  private static abstract class deleterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    deleterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.ShardDeleterProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("deleter");
    }
  }

  private static final class deleterFileDescriptorSupplier
      extends deleterBaseDescriptorSupplier {
    deleterFileDescriptorSupplier() {}
  }

  private static final class deleterMethodDescriptorSupplier
      extends deleterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    deleterMethodDescriptorSupplier(String methodName) {
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
      synchronized (deleterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new deleterFileDescriptorSupplier())
              .addMethod(getDeleteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
