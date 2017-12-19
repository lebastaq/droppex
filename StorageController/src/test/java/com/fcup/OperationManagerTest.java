package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class OperationManagerTest {
    private DbManager dbManager;
    private OperationManager operationManager;

    @Before
    public void setUpDbManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
        dbManager.connect();

        operationManager = new OperationManager();
    }

    @Test
    public void writeOperationIntoDBAndThenLoadIt() throws Exception {
        Operation operation = new Operation();

        operationManager.storeOperation(operation);

        operationManager.operations = new LinkedList<>();
        operationManager.loadLocalOperationsFromDB();

        List<Operation> operationsExpected;
        operationsExpected = dbManager.readEntries();

        if(operationManager.operations.size() != operationsExpected.size())
        {
            fail("Stored " + operationManager.operations.size() + " operations from db instead of " + operationsExpected.size());
        }
    }





}