package com.fcup;

import com.fcup.utilities.ChunkDownloader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloaderPool implements Runnable{
    private final String STORAGE_FOLDER;
    private final int BASE_PORT = 26002;
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    public DownloaderPool(String storage_folder) {
        this.STORAGE_FOLDER = storage_folder;
    }

    @Override
    public void run() {
        System.out.println(String.format("Starting Download ThreadPool with %s threads", MAX_THREADS));
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        boolean listening = true;
        try {
            ServerSocket serverSocket = new ServerSocket(BASE_PORT);
            while (listening) {
                ChunkDownloader chunkDownloader = new ChunkDownloader(serverSocket.accept(), STORAGE_FOLDER);
                executor.execute(chunkDownloader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }

}
