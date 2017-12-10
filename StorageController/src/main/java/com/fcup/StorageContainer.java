package com.fcup;


import org.jgroups.JChannel;
import org.jgroups.Message;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class StorageContainer {
    private String user_name = System.getProperty("user.name", "n/a");
    private JChannel channel;

    private StorageContainer() throws Exception {
        channel = new JChannel("config.xml");
    }

    private void run() throws Exception {
        channel.connect("ChatCluster");
        eventLoop();
        channel.disconnect();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                Message msg=new Message(null, line);
                channel.send(msg);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        StorageContainer storageContainer = null;
        try {
            storageContainer = new StorageContainer();
            storageContainer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
