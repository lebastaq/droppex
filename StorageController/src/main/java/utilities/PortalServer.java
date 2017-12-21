package utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortalServer implements Runnable {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private final int PORT = 29200;

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        boolean listening = true;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (listening) {
                PortalFileHandler fh = new PortalFileHandler(serverSocket.accept());
                executor.execute(fh);

            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        executor.shutdown();

    }
}