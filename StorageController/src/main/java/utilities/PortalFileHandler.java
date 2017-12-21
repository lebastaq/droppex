package utilities;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class PortalFileHandler implements Runnable {
    private final String TEMP_FILE_DIR = "tmp/";
    private final Socket socket;

    public PortalFileHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = in.readLine();

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

    private void sendDownload(BufferedReader in, OutputStream os, PrintWriter out) throws IOException {
        String fileName = in.readLine();

        /*
        TODO:
        Get the shards from the Storage Pools and encode them before continuing
         */

        File outgoingFile = new File(TEMP_FILE_DIR + fileName);

        try(FileInputStream fis = new FileInputStream(outgoingFile);
            FileChannel ch = fis.getChannel()) {

            long fileLength = outgoingFile.length();
            ByteBuffer buffer = ByteBuffer.allocate((int)fileLength);

            int bytesRead;
            while ((bytesRead = ch.read(buffer)) > 0){
                os.write(buffer.array(), 0, bytesRead);

            }

            os.flush();

        } catch (IOException e) {
            throw e;

        }
    }

}
