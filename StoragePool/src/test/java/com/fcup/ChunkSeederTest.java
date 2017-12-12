package com.fcup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ChunkSeederTest {

    private final int BASE_PORT = 26000;
    private ChunkSeeder chunkSeeder = null;
    private String testChunk = "testChunk";
    private Socket socket;
    ServerSocket serverSocket;

    @Before
    public void initChunkSeeeder() throws IOException {
        // TODO something with the socket..
        //
        System.out.println("Awaiting connections on port " + BASE_PORT);
        serverSocket = new ServerSocket(BASE_PORT);
    }

    @Test
    public void testInitChunkSeederNotNull() {
        if (chunkSeeder == null) {
            fail("Could not init chunkName seeder");
        }
    }

    // TODO finish this
//    @Test
//    public void  testInitChunkSeeder() {
//
//        Thread t1 = new Thread(){
//            public void run(){
//                try {
//
//                    OutputStream os = socket.getOutputStream();
//                    PrintWriter out = new PrintWriter(os, true);
//                    out.println(testChunk);
//                } catch (IOException e) {
//                    fail("Could not open socket for writing");
//                    e.printStackTrace();
//                }
//            }};
//        Thread t2 = new Thread(){
//            public void run(){
//                try {
//                    socket = serverSocket.accept();
//                    if(socket == null)
//                        socket = serverSocket.accept();
//                    chunkSeeder = new ChunkSeeder(socket, "storage/");
//
//                    chunkSeeder.readFileName();
//                } catch (IOException e) {
//                    fail("Could not read file name !");
//                    e.printStackTrace();
//                }
//            }};
//
//        t1.start();
//        t2.start();
//
//
//        if (chunkSeeder.chunkName != testChunk) {
//            fail("Could not set chunkName seeder's chunkName to seed:" + chunkSeeder.chunkName);
//        }
//    }

    @After
    public void unbindSocketAfterTest() {
        try {
            if(serverSocket != null)
                serverSocket.close();
            socket = null;
        } catch (IOException e) {
            fail("Couldn't close socket");
            e.printStackTrace();
        }
    }

}