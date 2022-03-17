package app.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;

public class ServerUserThread implements Runnable {

    private final Socket socket;
    private final ServerMain serverMain;
    private String username;
    PrintWriter outSocket;
    BufferedReader inSocket;

    String serverMessage = "";
    String clientMessage;



    public ServerUserThread(Socket socket, ServerMain serverMain) {
        super();
        this.socket = socket;
        this.serverMain = serverMain;
    }

    @Override
    public void run() {
        try {
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            username = inSocket.readLine();

            if (serverMain.getUserThreads().containsKey(username)){
                System.err.println("existing user");
                outSocket.println("existing user");
                socket.close();
            }
            else outSocket.println("welcome");

            serverMain.getUserThreads().put(username,outSocket);
            serverMain.welcomeNewUser(username);


            do {
                clientMessage = inSocket.readLine();				//prima poruku od klijenta
//                System.err.println(clientMessage);
                serverMessage = "<" + username + ">  <" + LocalDateTime.now() + ">  :" + clientMessage;
//                System.out.println(serverMessage);
                serverMain.broadcast(serverMessage, username);
            }while (!clientMessage.equals("/leave"));

            serverMain.removeUser(this);
            socket.close();

            serverMessage = username + " has left.";
            System.out.println(serverMessage);
            serverMain.broadcast(serverMessage,username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        outSocket.println(message);
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerMain getServerMain() {
        return serverMain;
    }

    public String getUsername() {
        return username;
    }

    public PrintWriter getOutSocket() {
        return outSocket;
    }

    public BufferedReader getInSocket() {
        return inSocket;
    }
}
