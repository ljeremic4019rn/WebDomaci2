package app.server;

import app.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {



    private final Map<String, PrintWriter> userThreads = new HashMap<>(); //username, outThread
//    private final Set<PrintWriter> userThreads = new HashSet<>();
    private final ArrayList<Message> chatHistory = new ArrayList<>();


    public ServerMain() throws IOException {
        @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server slusa na portu 2019.");

        while(true){
            Socket socket = serverSocket.accept();
            Thread serverUserThread = new Thread(new ServerUserThread(socket,this));
            serverUserThread.start();
            System.out.println("New client has been connected.");
        }
    }


    public static void main(String[] args) {
        try {
            new ServerMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void broadcast(String message, String excludedUser) {
        for (String username : userThreads.keySet()) {
            if (!username.equals(excludedUser))
                userThreads.get(username).println(message);
        }
    }

    public void welcomeNewUser(String newUsername){
        for (String username : userThreads.keySet()) {
            if (!username.equals(newUsername)) {
                userThreads.get(username).println("New user connected: " + newUsername);
            }
            else {
                userThreads.get(username).println("Welcome " + newUsername + " enjoy chatting and be respectful");
            }
        }
    }

    public void removeUser(ServerUserThread user) {
        //todo
    }



    public Map<String, PrintWriter> getUserThreads() {
        return userThreads;
    }

    public ArrayList<Message> getChatHistory() {
        return chatHistory;
    }
}
