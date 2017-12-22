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
    comments = "Source: poolregister.proto")
public final class registererGrpc {

  private registererGrpc() {}

  public static final String SERVICE_NAME = "com.fcup.generated.registerer";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRegisterMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.PoolInfo,
      com.fcup.generated.PoolRegistrationStatus> METHOD_REGISTER = getRegisterMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.PoolInfo,
      com.fcup.generated.PoolRegistrationStatus> getRegisterMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.PoolInfo,
      com.fcup.generated.PoolRegistrationStatus> getRegisterMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.PoolInfo, com.fcup.generated.PoolRegistrationStatus> getRegisterMethod;
    if ((getRegisterMethod = registererGrpc.getRegisterMethod) == null) {
      synchronized (registererGrpc.class) {
        if ((getRegisterMethod = registererGrpc.getRegisterMethod) == null) {
          registererGrpc.getRegisterMethod = getRegisterMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.PoolInfo, com.fcup.generated.PoolRegistrationStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.fcup.generated.registerer", "Register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.PoolInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.PoolRegistrationStatus.getDefaultInstance()))
                  .setSchemaDescriptor(new registererMethodDescriptorSupplier("Register"))
                  .build();
          }
        }
     }
     return getRegisterMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static registererStub newStub(io.grpc.Channel channel) {
    return new registererStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static registererBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new registererBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static registererFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new registererFutureStub(channel);
  }

  /**
   */
  public static abstract class registererImplBase implements io.grpc.BindableService {

    /**
     */
    public void register(com.fcup.generated.PoolInfo request,
        io.grpc.stub.StreamObserver<com.fcup.generated.PoolRegistrationStatus> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRegisterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.PoolInfo,
                com.fcup.generated.PoolRegistrationStatus>(
                  this, METHODID_REGISTER)))
          .build();
    }
  }

  /**
   */
  public static final class registererStub extends io.grpc.stub.AbstractStub<registererStub> {
    private registererStub(io.grpc.Channel channel) {
      super(channel);
    }

    private registererStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected registererStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new registererStub(channel, callOptions);
    }

    /**
     */
    public void register(com.fcup.generated.PoolInfo request,
        io.grpc.stub.StreamObserver<com.fcup.generated.PoolRegistrationStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class registererBlockingStub extends io.grpc.stub.AbstractStub<registererBlockingStub> {
    private registererBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private registererBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected registererBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new registererBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fcup.generated.PoolRegistrationStatus register(com.fcup.generated.PoolInfo request) {
      return blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class registererFutureStub extends io.grpc.stub.AbstractStub<registererFutureStub> {
    private registererFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private registererFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected registererFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new registererFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.PoolRegistrationStatus> register(
        com.fcup.generated.PoolInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final registererImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(registererImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTER:
          serviceImpl.register((com.fcup.generated.PoolInfo) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.PoolRegistrationStatus>) responseObserver);
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

  private static abstract class registererBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    registererBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.PoolRegisterProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("registerer");
    }
  }

  private static final class registererFileDescriptorSupplier
      extends registererBaseDescriptorSupplier {
    registererFileDescriptorSupplier() {}
  }

  private static final class registererMethodDescriptorSupplier
      extends registererBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    registererMethodDescriptorSupplier(String methodName) {
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
      synchronized (registererGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new registererFileDescriptorSupplier())
              .addMethod(getRegisterMethod())
              .build();
        }
      }
    }
    return result;
  }
}
