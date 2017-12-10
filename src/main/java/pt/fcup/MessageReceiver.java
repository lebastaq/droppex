package pt.fcup;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.nio.charset.StandardCharsets;

public class MessageReceiver extends ReceiverAdapter {
    public void receive(Message msg) {
        System.out.println("received message from " + msg.src() + "> " + new String(msg.rawBuffer(), StandardCharsets.UTF_8));
    }

    public void viewAccepted(View view) {
        System.out.println("received view " + view);
    }
}

