// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: servrpc.proto

package com.fcup.generated;

public final class PortalRPC {
  private PortalRPC() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_main_File_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_main_File_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_main_Empty_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_main_Empty_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_main_Query_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_main_Query_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_main_FileMetadata_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_main_FileMetadata_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rservrpc.proto\022\004main\"\030\n\004File\022\020\n\010filenam" +
      "e\030\001 \001(\t\"\007\n\005Empty\"\030\n\005Query\022\017\n\007pattern\030\001 \001" +
      "(\t\"2\n\014FileMetadata\022\020\n\010filename\030\001 \001(\t\022\020\n\010" +
      "fileSize\030\002 \001(\0032o\n\021StorageController\022\'\n\nD" +
      "eleteFile\022\n.main.File\032\013.main.Empty\"\000\0221\n\n" +
      "FileSearch\022\013.main.Query\032\022.main.FileMetad" +
      "ata\"\0000\001B!\n\022com.fcup.generatedB\tPortalRPC" +
      "P\001b\006proto3"
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
    internal_static_main_File_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_main_File_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_main_File_descriptor,
        new java.lang.String[] { "Filename", });
    internal_static_main_Empty_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_main_Empty_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_main_Empty_descriptor,
        new java.lang.String[] { });
    internal_static_main_Query_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_main_Query_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_main_Query_descriptor,
        new java.lang.String[] { "Pattern", });
    internal_static_main_FileMetadata_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_main_FileMetadata_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_main_FileMetadata_descriptor,
        new java.lang.String[] { "Filename", "FileSize", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
