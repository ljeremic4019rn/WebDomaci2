package app.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {
    private final Map<String, PrintWriter> userThreads = new HashMap<>(); //username, outThread
    private final ArrayList<String> chatHistory = new ArrayList<>();
    public static final String HLOCK = "HistoryLock";
    public static final ArrayList<String> wordBlackList = new ArrayList<>();


    public ServerMain() throws IOException, InterruptedException {
        @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(9001);
        System.out.println("Server slusa na portu 2019.");

        Thread historyManager = new Thread(new HistoryManager(this));
        historyManager.start();
//        historyManager.join();

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
        chatHistory.add(message);
        for (String username : userThreads.keySet()) {
            if (!username.equals(excludedUser))
                userThreads.get(username).println(message);
        }
    }

    public void welcomeNewUser(String newUsername){//todo popravi sto se vise puta pokazuje da je neko dosao kod tog usera
        for (String username : userThreads.keySet()) {
            if (username != newUsername) {
                userThreads.get(username).println("New user connected: " + newUsername);
                chatHistory.add("New user connected: " + newUsername);
            }
            else {
                userThreads.get(username).println("Welcome " + newUsername + " enjoy chatting and be respectful");
                chatHistory.add("Welcome " + newUsername + " enjoy chatting and be respectful");

            }
        }
    }

    public void removeUser(String username) {
        broadcast(username + " has left the chat", username);
        userThreads.remove(username);
    }

    public void updateChatHistory(){

    }

    public Map<String, PrintWriter> getUserThreads() {
        return userThreads;
    }

    public ArrayList<String> getChatHistory() {
        return chatHistory;
    }
}
