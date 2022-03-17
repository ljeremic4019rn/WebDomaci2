package app.server;

import java.io.PrintWriter;
import java.net.Socket;

public class HistoryManager implements Runnable{

    private final ServerMain serverMain;

    public HistoryManager( ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("msg num: " + serverMain.getChatHistory().size());
            if(serverMain.getChatHistory().size() > 100){
                synchronized (ServerMain.HLOCK) {
                    serverMain.getChatHistory().remove(0);
                    System.err.println("limit exceeded msg deleted");
                }
            }
            try {
                Thread.sleep(1000);//cisto da ne bi zatrpalo server konzolu sa printovima
            } catch (InterruptedException e) {//ovo inace ne bi bilo ovde
                e.printStackTrace();
            }
        }
    }
}
