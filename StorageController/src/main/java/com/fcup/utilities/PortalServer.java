package com.fcup.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortalServer implements Runnable {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private int port = 29200;
    private ServerSocket serverSocket;

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        boolean listening = true;
        bindPort();

        try {
            while (listening) {
                PortalFileHandler fh = null;
                    fh = new PortalFileHandler(serverSocket.accept());
                executor.execute(fh);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();

    }

    private void bindPort() {
        boolean connected = false;
        while (connected == false) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (java.net.BindException e) {
                port++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}