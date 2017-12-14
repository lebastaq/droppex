// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: filetransfer.proto

package com.fcup.generated;

/**
 * Protobuf type {@code com.fcup.generated.Info}
 */
public  final class Info extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.fcup.generated.Info)
        InfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Info.newBuilder() to construct.
  private Info(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Info() {
    chunkId_ = "";
    ip_ = "";
    port_ = 0;
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Info(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            String s = input.readStringRequireUtf8();

            chunkId_ = s;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            ip_ = s;
            break;
          }
          case 24: {

            port_ = input.readInt32();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.fcup.generated.DownloaderProto.internal_static_com_fcup_generated_Info_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.fcup.generated.DownloaderProto.internal_static_com_fcup_generated_Info_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.fcup.generated.Info.class, com.fcup.generated.Info.Builder.class);
  }

  public static final int CHUNKID_FIELD_NUMBER = 1;
  private volatile Object chunkId_;
  /**
   * <code>string chunkId = 1;</code>
   */
  public String getChunkId() {
    Object ref = chunkId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      chunkId_ = s;
      return s;
    }
  }
  /**
   * <code>string chunkId = 1;</code>
   */
  public com.google.protobuf.ByteString
      getChunkIdBytes() {
    Object ref = chunkId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      chunkId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int IP_FIELD_NUMBER = 2;
  private volatile Object ip_;
  /**
   * <code>string ip = 2;</code>
   */
  public String getIp() {
    Object ref = ip_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs =
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      ip_ = s;
      return s;
    }
  }
  /**
   * <code>string ip = 2;</code>
   */
  public com.google.protobuf.ByteString
      getIpBytes() {
    Object ref = ip_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      ip_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PORT_FIELD_NUMBER = 3;
  private int port_;
  /**
   * <code>int32 port = 3;</code>
   */
  public int getPort() {
    return port_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getChunkIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, chunkId_);
    }
    if (!getIpBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, ip_);
    }
    if (port_ != 0) {
      output.writeInt32(3, port_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getChunkIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, chunkId_);
    }
    if (!getIpBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, ip_);
    }
    if (port_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, port_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.fcup.generated.Info)) {
      return super.equals(obj);
    }
    com.fcup.generated.Info other = (com.fcup.generated.Info) obj;

    boolean result = true;
    result = result && getChunkId()
        .equals(other.getChunkId());
    result = result && getIp()
        .equals(other.getIp());
    result = result && (getPort()
        == other.getPort());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + CHUNKID_FIELD_NUMBER;
    hash = (53 * hash) + getChunkId().hashCode();
    hash = (37 * hash) + IP_FIELD_NUMBER;
    hash = (53 * hash) + getIp().hashCode();
    hash = (37 * hash) + PORT_FIELD_NUMBER;
    hash = (53 * hash) + getPort();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.fcup.generated.Info parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.fcup.generated.Info parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.fcup.generated.Info parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.fcup.generated.Info parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.fcup.generated.Info parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.fcup.generated.Info parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.fcup.generated.Info parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.fcup.generated.Info parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.fcup.generated.Info parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.fcup.generated.Info parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.fcup.generated.Info parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.fcup.generated.Info parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.fcup.generated.Info prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.fcup.generated.Info}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.fcup.generated.Info)
      com.fcup.generated.InfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.fcup.generated.DownloaderProto.internal_static_com_fcup_generated_Info_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.fcup.generated.DownloaderProto.internal_static_com_fcup_generated_Info_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.fcup.generated.Info.class, com.fcup.generated.Info.Builder.class);
    }

    // Construct using com.fcup.generated.Info.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      chunkId_ = "";

      ip_ = "";

      port_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.fcup.generated.DownloaderProto.internal_static_com_fcup_generated_Info_descriptor;
    }

    public com.fcup.generated.Info getDefaultInstanceForType() {
      return com.fcup.generated.Info.getDefaultInstance();
    }

    public com.fcup.generated.Info build() {
      com.fcup.generated.Info result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.fcup.generated.Info buildPartial() {
      com.fcup.generated.Info result = new com.fcup.generated.Info(this);
      result.chunkId_ = chunkId_;
      result.ip_ = ip_;
      result.port_ = port_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.fcup.generated.Info) {
        return mergeFrom((com.fcup.generated.Info)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.fcup.generated.Info other) {
      if (other == com.fcup.generated.Info.getDefaultInstance()) return this;
      if (!other.getChunkId().isEmpty()) {
        chunkId_ = other.chunkId_;
        onChanged();
      }
      if (!other.getIp().isEmpty()) {
        ip_ = other.ip_;
        onChanged();
      }
      if (other.getPort() != 0) {
        setPort(other.getPort());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.fcup.generated.Info parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.fcup.generated.Info) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object chunkId_ = "";
    /**
     * <code>string chunkId = 1;</code>
     */
    public String getChunkId() {
      Object ref = chunkId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        chunkId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string chunkId = 1;</code>
     */
    public com.google.protobuf.ByteString
        getChunkIdBytes() {
      Object ref = chunkId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        chunkId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string chunkId = 1;</code>
     */
    public Builder setChunkId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      chunkId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string chunkId = 1;</code>
     */
    public Builder clearChunkId() {

      chunkId_ = getDefaultInstance().getChunkId();
      onChanged();
      return this;
    }
    /**
     * <code>string chunkId = 1;</code>
     */
    public Builder setChunkIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      chunkId_ = value;
      onChanged();
      return this;
    }

    private Object ip_ = "";
    /**
     * <code>string ip = 2;</code>
     */
    public String getIp() {
      Object ref = ip_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        ip_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string ip = 2;</code>
     */
    public com.google.protobuf.ByteString
        getIpBytes() {
      Object ref = ip_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        ip_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ip = 2;</code>
     */
    public Builder setIp(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }

      ip_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ip = 2;</code>
     */
    public Builder clearIp() {

      ip_ = getDefaultInstance().getIp();
      onChanged();
      return this;
    }
    /**
     * <code>string ip = 2;</code>
     */
    public Builder setIpBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

      ip_ = value;
      onChanged();
      return this;
    }

    private int port_ ;
    /**
     * <code>int32 port = 3;</code>
     */
    public int getPort() {
      return port_;
    }
    /**
     * <code>int32 port = 3;</code>
     */
    public Builder setPort(int value) {

      port_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 port = 3;</code>
     */
    public Builder clearPort() {

      port_ = 0;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.fcup.generated.Info)
  }

  // @@protoc_insertion_point(class_scope:com.fcup.generated.Info)
  private static final com.fcup.generated.Info DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.fcup.generated.Info();
  }

  public static com.fcup.generated.Info getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Info>
      PARSER = new com.google.protobuf.AbstractParser<Info>() {
    public Info parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Info(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Info> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Info> getParserForType() {
    return PARSER;
  }

  public com.fcup.generated.Info getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

