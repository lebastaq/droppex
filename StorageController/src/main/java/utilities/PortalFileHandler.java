package utilities;

import java.io.*;
import java.net.Socket;

public class PortalFileHandler implements Runnable {
    Socket socket;

    public PortalFileHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = in.readLine();

            System.out.println(action);

            if (action.equals("upload")) {
                receiveUpload(in, out);

            } else if (action.equals("download")) {
                sendDownload(in, os, out);

            } else {
                out.println("Error: Operation not supported. Need 'upload' or 'download'");
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void receiveUpload(BufferedReader in, PrintWriter out) {

    }

    private void sendDownload(BufferedReader in, OutputStream os, PrintWriter out) {

    }

}
