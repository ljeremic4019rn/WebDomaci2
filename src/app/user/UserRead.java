package app.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class UserRead implements Runnable{
    private UserMain userMain;
    private final Socket socket;

    public UserRead(Socket socket, UserMain userMain){
        this.socket = socket;
        this.userMain = userMain;

    }

    @Override
    public void run() {
        try {
            BufferedReader inSocket= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String recievedMessage;
            while (true) {
                recievedMessage = inSocket.readLine();
                System.out.println(recievedMessage);
            }
        } catch (IOException e) {
            System.out.println("You have left a chat");
        }
    }
}
