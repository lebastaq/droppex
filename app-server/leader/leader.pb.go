// Code generated by protoc-gen-go. DO NOT EDIT.
// source: leader.proto

/*
Package leader is a generated protocol buffer package.

It is generated from these files:
	leader.proto

It has these top-level messages:
	LeaderIP
	EmptyReply
*/
package leader

import proto "github.com/golang/protobuf/proto"
import fmt "fmt"
import math "math"

import (
	context "golang.org/x/net/context"
	grpc "google.golang.org/grpc"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion2 // please upgrade the proto package

type LeaderIP struct {
	Ip string `protobuf:"bytes,1,opt,name=ip" json:"ip,omitempty"`
}

func (m *LeaderIP) Reset()                    { *m = LeaderIP{} }
func (m *LeaderIP) String() string            { return proto.CompactTextString(m) }
func (*LeaderIP) ProtoMessage()               {}
func (*LeaderIP) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{0} }

func (m *LeaderIP) GetIp() string {
	if m != nil {
		return m.Ip
	}
	return ""
}

// Return an error from controller if something went wrong
type EmptyReply struct {
}

func (m *EmptyReply) Reset()                    { *m = EmptyReply{} }
func (m *EmptyReply) String() string            { return proto.CompactTextString(m) }
func (*EmptyReply) ProtoMessage()               {}
func (*EmptyReply) Descriptor() ([]byte, []int) { return fileDescriptor0, []int{1} }

func init() {
	proto.RegisterType((*LeaderIP)(nil), "leader.LeaderIP")
	proto.RegisterType((*EmptyReply)(nil), "leader.EmptyReply")
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// Client API for LeaderService service

type LeaderServiceClient interface {
	// Forwards new leader IP after election
	AnnounceLeader(ctx context.Context, in *LeaderIP, opts ...grpc.CallOption) (*EmptyReply, error)
}

type leaderServiceClient struct {
	cc *grpc.ClientConn
}

func NewLeaderServiceClient(cc *grpc.ClientConn) LeaderServiceClient {
	return &leaderServiceClient{cc}
}

func (c *leaderServiceClient) AnnounceLeader(ctx context.Context, in *LeaderIP, opts ...grpc.CallOption) (*EmptyReply, error) {
	out := new(EmptyReply)
	err := grpc.Invoke(ctx, "/leader.LeaderService/AnnounceLeader", in, out, c.cc, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// Server API for LeaderService service

type LeaderServiceServer interface {
	// Forwards new leader IP after election
	AnnounceLeader(context.Context, *LeaderIP) (*EmptyReply, error)
}

func RegisterLeaderServiceServer(s *grpc.Server, srv LeaderServiceServer) {
	s.RegisterService(&_LeaderService_serviceDesc, srv)
}

func _LeaderService_AnnounceLeader_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(LeaderIP)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(LeaderServiceServer).AnnounceLeader(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/leader.LeaderService/AnnounceLeader",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(LeaderServiceServer).AnnounceLeader(ctx, req.(*LeaderIP))
	}
	return interceptor(ctx, in, info, handler)
}

var _LeaderService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "leader.LeaderService",
	HandlerType: (*LeaderServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "AnnounceLeader",
			Handler:    _LeaderService_AnnounceLeader_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "leader.proto",
}

func init() { proto.RegisterFile("leader.proto", fileDescriptor0) }

var fileDescriptor0 = []byte{
	// 159 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0xe2, 0xe2, 0xc9, 0x49, 0x4d, 0x4c,
	0x49, 0x2d, 0xd2, 0x2b, 0x28, 0xca, 0x2f, 0xc9, 0x17, 0x62, 0x83, 0xf0, 0x94, 0xa4, 0xb8, 0x38,
	0x7c, 0xc0, 0x2c, 0xcf, 0x00, 0x21, 0x3e, 0x2e, 0xa6, 0xcc, 0x02, 0x09, 0x46, 0x05, 0x46, 0x0d,
	0xce, 0x20, 0xa6, 0xcc, 0x02, 0x25, 0x1e, 0x2e, 0x2e, 0xd7, 0xdc, 0x82, 0x92, 0xca, 0xa0, 0xd4,
	0x82, 0x9c, 0x4a, 0x23, 0x4f, 0x2e, 0x5e, 0x88, 0xca, 0xe0, 0xd4, 0xa2, 0xb2, 0xcc, 0xe4, 0x54,
	0x21, 0x0b, 0x2e, 0x3e, 0xc7, 0xbc, 0xbc, 0xfc, 0xd2, 0xbc, 0xe4, 0x54, 0x88, 0x84, 0x90, 0x80,
	0x1e, 0xd4, 0x0e, 0x98, 0x91, 0x52, 0x42, 0x30, 0x11, 0x84, 0x41, 0x4a, 0x0c, 0x4e, 0x8a, 0x5c,
	0x42, 0xc9, 0xf9, 0xb9, 0x7a, 0x69, 0xc9, 0xa5, 0x05, 0x7a, 0xe9, 0xa9, 0x79, 0xa9, 0x45, 0x89,
	0x25, 0xa9, 0x29, 0x4e, 0x9c, 0x10, 0x5d, 0x41, 0x01, 0xce, 0x01, 0x8c, 0x49, 0x6c, 0x60, 0x67,
	0x1a, 0x03, 0x02, 0x00, 0x00, 0xff, 0xff, 0x5d, 0x95, 0x72, 0xa0, 0xb6, 0x00, 0x00, 0x00,
}
