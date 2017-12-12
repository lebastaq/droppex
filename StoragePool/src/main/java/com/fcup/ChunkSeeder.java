package com.fcup;

import java.io.*;
import java.net.Socket;

public class ChunkSeeder extends Thread{
    private final Socket socket;
    String chunk;
    File file;

    public ChunkSeeder(Socket socket, String chunk) {
        this.chunk = chunk;
        this.socket = socket;
    }

    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))

            System.out.println("Requested chunk" + chunk);

            openFile();

            try(FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {

                long fileLength = file.length();

                byte[] contents = new byte[(int)fileLength]; // 1MB
                int bytesRead = 0;

                while((bytesRead = bis.read(contents)) > 0){
                    os.write(contents, 0, bytesRead);

                    // update every 20%
                    if((bytesRead*100)/fileLength % 20 == 0) {
                        System.out.print("Sending file ... " + (bytesRead * 100) / fileLength + "% complete!");
                    }
                }

                os.flush();
            }


        }
        catch(java.lang.NullPointerException e)
        {
            System.out.println("Coulnd't retrieve seeder from hash map (" + filename + ")");
            try{
                sb.createSingleSeeder(filename);
            }
            catch (Exception f) {
                // Do nothing, client can request again.
                f.printStackTrace();
            }
        }
        catch (IOException e) {
            // Do nothing, client can request again.
            e.printStackTrace();

        }
    }

    private File openFile() {
        file = new File("storage/" + chunk);
    }
}
