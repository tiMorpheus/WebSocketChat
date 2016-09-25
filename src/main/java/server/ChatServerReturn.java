package server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatServerReturn implements Runnable {

    Socket socket;
    private Scanner in;
    private PrintWriter out;
    String message = "";

    //logger
    private static final Logger log = Logger.getLogger(ChatServerReturn.class);

    public ChatServerReturn(Socket socket) {
        this.socket = socket;
    }

    public void checkConnection() throws IOException {
        if (!socket.isConnected()){
            for (int i = 0; i < ChatServer.connectionArray.size(); i++) {
                if (ChatServer.connectionArray.get(i) == socket){
                    ChatServer.connectionArray.remove(i);
                }
            }

            for (Socket tempSocket : ChatServer.connectionArray) {
                PrintWriter output = new PrintWriter(tempSocket.getOutputStream());
                output.println(tempSocket.getLocalAddress().getHostName()+ "disconnected");
                output.flush();

                log.info("User" + tempSocket.getLocalAddress().getHostName()+ " disconnected");
            }
        }
    }

    @Override
    public void run(){
        try{
            try{
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());

                while (true) {
                    checkConnection();

                    if (!in.hasNext()) {
                        return;
                    }

                    message = in.nextLine();

                    log.info("Client said---" + message);

                    for (Socket tempSocket : ChatServer.connectionArray) {
                        PrintWriter output = new PrintWriter(tempSocket.getOutputStream());
                        output.println(message);
                        output.flush();
                        log.info("Sent to---" + tempSocket.getLocalAddress().getHostName());
                    }
                }
            }finally {
                socket.close();
            }
        }catch (Exception x){
            log.error(x.getMessage(),x);
        }
    }
}
