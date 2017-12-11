package com.fcup;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class StorageContainerTest {
    @Test
    public void viewAccepted() throws Exception {
        List<String> expectedNewOperations = new LinkedList<>();
        StorageContainer storageContainer = new StorageContainer();

        storageContainer.connectToChannel();

        assertEquals(storageContainer.operations, expectedNewOperations);
    }

    @Test
    public void receive() throws Exception {
    }

    @Test
    public void setState() throws Exception {
    }

}