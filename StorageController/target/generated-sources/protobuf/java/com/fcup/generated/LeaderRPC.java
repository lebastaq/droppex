// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: leader.proto

package com.fcup.generated;

public final class LeaderRPC {
  private LeaderRPC() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_leader_LeaderIP_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_leader_LeaderIP_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_leader_EmptyReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_leader_EmptyReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014leader.proto\022\006leader\"\026\n\010LeaderIP\022\n\n\002ip" +
      "\030\001 \001(\t\"\014\n\nEmptyReply2I\n\rLeaderService\0228\n" +
      "\016AnnounceLeader\022\020.leader.LeaderIP\032\022.lead" +
      "er.EmptyReply\"\000B!\n\022com.fcup.generatedB\tL" +
      "eaderRPCP\001b\006proto3"
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
    internal_static_leader_LeaderIP_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_leader_LeaderIP_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_leader_LeaderIP_descriptor,
        new java.lang.String[] { "Ip", });
    internal_static_leader_EmptyReply_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_leader_EmptyReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_leader_EmptyReply_descriptor,
        new java.lang.String[] { });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
