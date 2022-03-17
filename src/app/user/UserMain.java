package app.user;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserMain {

    private String username;
    public static Socket socket = null;
    public static BufferedReader hardIn = null;
    public static PrintWriter hardOut = null;


    public UserMain() throws IOException, InterruptedException {
        socket = new Socket("localhost", 9001);
        hardIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        hardOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println("[Server]: Enter your name");
        username = scanner.nextLine();
        hardOut.println(username);

        if (hardIn.readLine().equals("existing user")){
            System.err.println("User already exists stop trying to steal peoples profiles");
            return;
        }

        Thread readThread = new Thread(new UserRead(socket,this));
        Thread sendThread = new Thread(new UserSend(socket,this));
        readThread.start();
        sendThread.start();
        sendThread.join();
        readThread.join();

    }

    public static void main(String[] args) {

        try {
            new UserMain();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (hardIn != null) {
                try {
                    hardIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (hardOut != null) {
                hardOut.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
