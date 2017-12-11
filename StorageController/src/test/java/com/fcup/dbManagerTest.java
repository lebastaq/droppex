package com.fcup;

import org.junit.Test;

import static org.junit.Assert.*;

public class dbManagerTest {

    @Test
    public void connectToNonExistentDB() {
        try {
            dbManager dbManager = new dbManager("non-existent-database");
            fail("DBManager didn't throw an exception when trying to connect to a non existent database");
        } catch (Exception e) {
        }
    }

    @Test
    public void getAllEntriesFromTable() throws Exception {
    }

}