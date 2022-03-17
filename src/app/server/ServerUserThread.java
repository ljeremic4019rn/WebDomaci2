package app.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ServerUserThread implements Runnable {

    private final Socket socket;
    private final ServerMain serverMain;
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
            String username = inSocket.readLine();

            if (serverMain.getUserThreads().containsKey(username)){
                System.err.println("existing user");
                outSocket.println("existing user");
                socket.close();
            }
            else outSocket.println("welcome");

            synchronized (ServerMain.HLOCK) {
                for (String msg : serverMain.getChatHistory()){
                    outSocket.println(msg);
                }
            }

            serverMain.getUserThreads().put(username,outSocket);
            serverMain.welcomeNewUser(username);

            do {
                clientMessage = inSocket.readLine();
                serverMessage = "<" + username + "> <" + LocalDateTime.now() + ">: " + serverMain.censure(clientMessage);

                serverMain.broadcast(serverMessage, username);
            }while (!clientMessage.equals("/leave"));

            serverMain.removeUser(username);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inSocket != null) {
                try {
                    inSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outSocket != null) {
                outSocket.close();
            }
            if (this.socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
