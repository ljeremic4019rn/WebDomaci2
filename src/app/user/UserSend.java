package app.user;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserSend implements Runnable{
    private final Socket socket;
    private UserMain userMain;


    public UserSend(Socket socket, UserMain userMain){
        this.socket = socket;
        this.userMain = userMain;
    }



    @Override
    public void run() {
        try {
            PrintWriter outSocket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            Scanner scanner = new Scanner(System.in);
            String message;

            do{
                message = scanner.nextLine();
                outSocket.println(message);
            }while (!message.equals("/leave"));

            socket.close();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
