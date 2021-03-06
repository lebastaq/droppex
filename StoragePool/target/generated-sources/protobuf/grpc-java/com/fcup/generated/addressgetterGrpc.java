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
    comments = "Source: controllerAddressGetter.proto")
public final class addressgetterGrpc {

  private addressgetterGrpc() {}

  public static final String SERVICE_NAME = "com.fcup.generated.addressgetter";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetAddressMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.storageControllerInfo,
      com.fcup.generated.addressChangedStatus> METHOD_SET_ADDRESS = getSetAddressMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.storageControllerInfo,
      com.fcup.generated.addressChangedStatus> getSetAddressMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.storageControllerInfo,
      com.fcup.generated.addressChangedStatus> getSetAddressMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.storageControllerInfo, com.fcup.generated.addressChangedStatus> getSetAddressMethod;
    if ((getSetAddressMethod = addressgetterGrpc.getSetAddressMethod) == null) {
      synchronized (addressgetterGrpc.class) {
        if ((getSetAddressMethod = addressgetterGrpc.getSetAddressMethod) == null) {
          addressgetterGrpc.getSetAddressMethod = getSetAddressMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.storageControllerInfo, com.fcup.generated.addressChangedStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.fcup.generated.addressgetter", "SetAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.storageControllerInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.addressChangedStatus.getDefaultInstance()))
                  .setSchemaDescriptor(new addressgetterMethodDescriptorSupplier("SetAddress"))
                  .build();
          }
        }
     }
     return getSetAddressMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static addressgetterStub newStub(io.grpc.Channel channel) {
    return new addressgetterStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static addressgetterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new addressgetterBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static addressgetterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new addressgetterFutureStub(channel);
  }

  /**
   */
  public static abstract class addressgetterImplBase implements io.grpc.BindableService {

    /**
     */
    public void setAddress(com.fcup.generated.storageControllerInfo request,
        io.grpc.stub.StreamObserver<com.fcup.generated.addressChangedStatus> responseObserver) {
      asyncUnimplementedUnaryCall(getSetAddressMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSetAddressMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.storageControllerInfo,
                com.fcup.generated.addressChangedStatus>(
                  this, METHODID_SET_ADDRESS)))
          .build();
    }
  }

  /**
   */
  public static final class addressgetterStub extends io.grpc.stub.AbstractStub<addressgetterStub> {
    private addressgetterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private addressgetterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected addressgetterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new addressgetterStub(channel, callOptions);
    }

    /**
     */
    public void setAddress(com.fcup.generated.storageControllerInfo request,
        io.grpc.stub.StreamObserver<com.fcup.generated.addressChangedStatus> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetAddressMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class addressgetterBlockingStub extends io.grpc.stub.AbstractStub<addressgetterBlockingStub> {
    private addressgetterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private addressgetterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected addressgetterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new addressgetterBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fcup.generated.addressChangedStatus setAddress(com.fcup.generated.storageControllerInfo request) {
      return blockingUnaryCall(
          getChannel(), getSetAddressMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class addressgetterFutureStub extends io.grpc.stub.AbstractStub<addressgetterFutureStub> {
    private addressgetterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private addressgetterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected addressgetterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new addressgetterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.addressChangedStatus> setAddress(
        com.fcup.generated.storageControllerInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getSetAddressMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SET_ADDRESS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final addressgetterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(addressgetterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SET_ADDRESS:
          serviceImpl.setAddress((com.fcup.generated.storageControllerInfo) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.addressChangedStatus>) responseObserver);
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

  private static abstract class addressgetterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    addressgetterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.controllerAddressGetter.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("addressgetter");
    }
  }

  private static final class addressgetterFileDescriptorSupplier
      extends addressgetterBaseDescriptorSupplier {
    addressgetterFileDescriptorSupplier() {}
  }

  private static final class addressgetterMethodDescriptorSupplier
      extends addressgetterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    addressgetterMethodDescriptorSupplier(String methodName) {
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
      synchronized (addressgetterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new addressgetterFileDescriptorSupplier())
              .addMethod(getSetAddressMethod())
              .build();
        }
      }
    }
    return result;
  }
}
