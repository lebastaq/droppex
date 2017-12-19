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
    comments = "Source: filetransfer.proto")
public final class downloaderGrpc {

  private downloaderGrpc() {}

  public static final String SERVICE_NAME = "com.fcup.generated.downloader";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStartDownloaderMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.Info,
      com.fcup.generated.Status> METHOD_START_DOWNLOADER = getStartDownloaderMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.Info,
      com.fcup.generated.Status> getStartDownloaderMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.Info,
      com.fcup.generated.Status> getStartDownloaderMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.Info, com.fcup.generated.Status> getStartDownloaderMethod;
    if ((getStartDownloaderMethod = downloaderGrpc.getStartDownloaderMethod) == null) {
      synchronized (downloaderGrpc.class) {
        if ((getStartDownloaderMethod = downloaderGrpc.getStartDownloaderMethod) == null) {
          downloaderGrpc.getStartDownloaderMethod = getStartDownloaderMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.Info, com.fcup.generated.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.fcup.generated.downloader", "StartDownloader"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.Info.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new downloaderMethodDescriptorSupplier("StartDownloader"))
                  .build();
          }
        }
     }
     return getStartDownloaderMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static downloaderStub newStub(io.grpc.Channel channel) {
    return new downloaderStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static downloaderBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new downloaderBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static downloaderFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new downloaderFutureStub(channel);
  }

  /**
   */
  public static abstract class downloaderImplBase implements io.grpc.BindableService {

    /**
     */
    public void startDownloader(com.fcup.generated.Info request,
        io.grpc.stub.StreamObserver<com.fcup.generated.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getStartDownloaderMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getStartDownloaderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.Info,
                com.fcup.generated.Status>(
                  this, METHODID_START_DOWNLOADER)))
          .build();
    }
  }

  /**
   */
  public static final class downloaderStub extends io.grpc.stub.AbstractStub<downloaderStub> {
    private downloaderStub(io.grpc.Channel channel) {
      super(channel);
    }

    private downloaderStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected downloaderStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new downloaderStub(channel, callOptions);
    }

    /**
     */
    public void startDownloader(com.fcup.generated.Info request,
        io.grpc.stub.StreamObserver<com.fcup.generated.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStartDownloaderMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class downloaderBlockingStub extends io.grpc.stub.AbstractStub<downloaderBlockingStub> {
    private downloaderBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private downloaderBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected downloaderBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new downloaderBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.fcup.generated.Status startDownloader(com.fcup.generated.Info request) {
      return blockingUnaryCall(
          getChannel(), getStartDownloaderMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class downloaderFutureStub extends io.grpc.stub.AbstractStub<downloaderFutureStub> {
    private downloaderFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private downloaderFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected downloaderFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new downloaderFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.Status> startDownloader(
        com.fcup.generated.Info request) {
      return futureUnaryCall(
          getChannel().newCall(getStartDownloaderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START_DOWNLOADER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final downloaderImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(downloaderImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_DOWNLOADER:
          serviceImpl.startDownloader((com.fcup.generated.Info) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.Status>) responseObserver);
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

  private static abstract class downloaderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    downloaderBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.DownloaderProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("downloader");
    }
  }

  private static final class downloaderFileDescriptorSupplier
      extends downloaderBaseDescriptorSupplier {
    downloaderFileDescriptorSupplier() {}
  }

  private static final class downloaderMethodDescriptorSupplier
      extends downloaderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    downloaderMethodDescriptorSupplier(String methodName) {
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
      synchronized (downloaderGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new downloaderFileDescriptorSupplier())
              .addMethod(getStartDownloaderMethod())
              .build();
        }
      }
    }
    return result;
  }
}
