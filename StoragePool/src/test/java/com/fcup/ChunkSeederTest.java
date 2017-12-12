package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.*;

public class ChunkSeederTest {
    private ChunkSeeder chunkSeeder;
    private String testChunk = "testChunk";
    private Socket socket;

    @Before
    public void initChunkSeeeder() {
        // TODO something with the socket...
        chunkSeeder = new ChunkSeeder(socket, testChunk);
    }


    @Test
    public void testInitChunkSeederNotNull() {
        if (chunkSeeder.chunk == null) {
            fail("Could not init chunk seeder");
        }
    }

    @Test
    public void testInitChunkSeeder() {
        if (chunkSeeder.chunk != testChunk) {
            fail("Could not set chunk seeder's chunk to seed");
        }
    }

}