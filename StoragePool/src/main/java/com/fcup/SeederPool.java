package com.fcup;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeederPool implements Runnable {

    private final int BASE_PORT = 26000;
    private final String STORAGE_FOLDER;
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    public void run() {
        System.out.println(String.format("Starting Upload ThreadPool with %s threads", MAX_THREADS));
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        boolean listening = true;
        try {
            ServerSocket serverSocket = new ServerSocket(BASE_PORT);
            while (listening) {
                ChunkSeeder chunkSeeder = new ChunkSeeder(serverSocket.accept(), STORAGE_FOLDER);
                executor.execute(chunkSeeder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

    public SeederPool(String STORAGE_FOLDER) {
        this.STORAGE_FOLDER = STORAGE_FOLDER;
    }
}
