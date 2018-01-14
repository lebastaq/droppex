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
    comments = "Source: servrpc.proto")
public final class PortalControllerGrpc {

  private PortalControllerGrpc() {}

  public static final String SERVICE_NAME = "servrpc.PortalController";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteFileMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.DeletionRequest,
      com.fcup.generated.Empty> METHOD_DELETE_FILE = getDeleteFileMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.DeletionRequest,
      com.fcup.generated.Empty> getDeleteFileMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.DeletionRequest,
      com.fcup.generated.Empty> getDeleteFileMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.DeletionRequest, com.fcup.generated.Empty> getDeleteFileMethod;
    if ((getDeleteFileMethod = PortalControllerGrpc.getDeleteFileMethod) == null) {
      synchronized (PortalControllerGrpc.class) {
        if ((getDeleteFileMethod = PortalControllerGrpc.getDeleteFileMethod) == null) {
          PortalControllerGrpc.getDeleteFileMethod = getDeleteFileMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.DeletionRequest, com.fcup.generated.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "servrpc.PortalController", "DeleteFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.DeletionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new PortalControllerMethodDescriptorSupplier("DeleteFile"))
                  .build();
          }
        }
     }
     return getDeleteFileMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFileSearchMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.fcup.generated.SearchRequest,
      com.fcup.generated.SearchResponse> METHOD_FILE_SEARCH = getFileSearchMethod();

  private static volatile io.grpc.MethodDescriptor<com.fcup.generated.SearchRequest,
      com.fcup.generated.SearchResponse> getFileSearchMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.fcup.generated.SearchRequest,
      com.fcup.generated.SearchResponse> getFileSearchMethod() {
    io.grpc.MethodDescriptor<com.fcup.generated.SearchRequest, com.fcup.generated.SearchResponse> getFileSearchMethod;
    if ((getFileSearchMethod = PortalControllerGrpc.getFileSearchMethod) == null) {
      synchronized (PortalControllerGrpc.class) {
        if ((getFileSearchMethod = PortalControllerGrpc.getFileSearchMethod) == null) {
          PortalControllerGrpc.getFileSearchMethod = getFileSearchMethod = 
              io.grpc.MethodDescriptor.<com.fcup.generated.SearchRequest, com.fcup.generated.SearchResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "servrpc.PortalController", "FileSearch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.SearchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fcup.generated.SearchResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new PortalControllerMethodDescriptorSupplier("FileSearch"))
                  .build();
          }
        }
     }
     return getFileSearchMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PortalControllerStub newStub(io.grpc.Channel channel) {
    return new PortalControllerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PortalControllerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PortalControllerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PortalControllerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PortalControllerFutureStub(channel);
  }

  /**
   */
  public static abstract class PortalControllerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Forwards file deletion request from client
     * </pre>
     */
    public void deleteFile(com.fcup.generated.DeletionRequest request,
        io.grpc.stub.StreamObserver<com.fcup.generated.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteFileMethod(), responseObserver);
    }

    /**
     * <pre>
     * Forwards search query from Client
     * If there are multiple files that match the query, return them as a stream
     * </pre>
     */
    public void fileSearch(com.fcup.generated.SearchRequest request,
        io.grpc.stub.StreamObserver<com.fcup.generated.SearchResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFileSearchMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getDeleteFileMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.fcup.generated.DeletionRequest,
                com.fcup.generated.Empty>(
                  this, METHODID_DELETE_FILE)))
          .addMethod(
            getFileSearchMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.fcup.generated.SearchRequest,
                com.fcup.generated.SearchResponse>(
                  this, METHODID_FILE_SEARCH)))
          .build();
    }
  }

  /**
   */
  public static final class PortalControllerStub extends io.grpc.stub.AbstractStub<PortalControllerStub> {
    private PortalControllerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PortalControllerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PortalControllerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PortalControllerStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards file deletion request from client
     * </pre>
     */
    public void deleteFile(com.fcup.generated.DeletionRequest request,
        io.grpc.stub.StreamObserver<com.fcup.generated.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteFileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Forwards search query from Client
     * If there are multiple files that match the query, return them as a stream
     * </pre>
     */
    public void fileSearch(com.fcup.generated.SearchRequest request,
        io.grpc.stub.StreamObserver<com.fcup.generated.SearchResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFileSearchMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PortalControllerBlockingStub extends io.grpc.stub.AbstractStub<PortalControllerBlockingStub> {
    private PortalControllerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PortalControllerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PortalControllerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PortalControllerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards file deletion request from client
     * </pre>
     */
    public com.fcup.generated.Empty deleteFile(com.fcup.generated.DeletionRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteFileMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Forwards search query from Client
     * If there are multiple files that match the query, return them as a stream
     * </pre>
     */
    public java.util.Iterator<com.fcup.generated.SearchResponse> fileSearch(
        com.fcup.generated.SearchRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getFileSearchMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PortalControllerFutureStub extends io.grpc.stub.AbstractStub<PortalControllerFutureStub> {
    private PortalControllerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PortalControllerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PortalControllerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PortalControllerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Forwards file deletion request from client
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.fcup.generated.Empty> deleteFile(
        com.fcup.generated.DeletionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteFileMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DELETE_FILE = 0;
  private static final int METHODID_FILE_SEARCH = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PortalControllerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PortalControllerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DELETE_FILE:
          serviceImpl.deleteFile((com.fcup.generated.DeletionRequest) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.Empty>) responseObserver);
          break;
        case METHODID_FILE_SEARCH:
          serviceImpl.fileSearch((com.fcup.generated.SearchRequest) request,
              (io.grpc.stub.StreamObserver<com.fcup.generated.SearchResponse>) responseObserver);
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

  private static abstract class PortalControllerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PortalControllerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fcup.generated.PortalRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PortalController");
    }
  }

  private static final class PortalControllerFileDescriptorSupplier
      extends PortalControllerBaseDescriptorSupplier {
    PortalControllerFileDescriptorSupplier() {}
  }

  private static final class PortalControllerMethodDescriptorSupplier
      extends PortalControllerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PortalControllerMethodDescriptorSupplier(String methodName) {
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
      synchronized (PortalControllerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PortalControllerFileDescriptorSupplier())
              .addMethod(getDeleteFileMethod())
              .addMethod(getFileSearchMethod())
              .build();
        }
      }
    }
    return result;
  }
}
