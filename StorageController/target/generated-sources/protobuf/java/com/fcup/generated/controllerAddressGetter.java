// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: controllerAddressGetter.proto

package com.fcup.generated;

public final class controllerAddressGetter {
  private controllerAddressGetter() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_fcup_generated_storageControllerInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_fcup_generated_storageControllerInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_fcup_generated_addressChangedStatus_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_fcup_generated_addressChangedStatus_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\035controllerAddressGetter.proto\022\022com.fcu" +
      "p.generated\"1\n\025storageControllerInfo\022\n\n\002" +
      "ip\030\001 \001(\t\022\014\n\004port\030\002 \001(\005\"\"\n\024addressChanged" +
      "Status\022\n\n\002ok\030\001 \001(\0102t\n\raddressgetter\022c\n\nS" +
      "etAddress\022).com.fcup.generated.storageCo" +
      "ntrollerInfo\032(.com.fcup.generated.addres" +
      "sChangedStatus\"\000B!B\027controllerAddressGet" +
      "terP\001\242\002\003PRPb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_fcup_generated_storageControllerInfo_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_fcup_generated_storageControllerInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_fcup_generated_storageControllerInfo_descriptor,
        new java.lang.String[] { "Ip", "Port", });
    internal_static_com_fcup_generated_addressChangedStatus_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_fcup_generated_addressChangedStatus_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_fcup_generated_addressChangedStatus_descriptor,
        new java.lang.String[] { "Ok", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
