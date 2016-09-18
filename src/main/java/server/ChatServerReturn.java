package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatServerReturn implements Runnable {

    Socket socket;
    private Scanner in;
    private PrintWriter out;
    String message = "";

    public ChatServerReturn(Socket socket) {
        this.socket = socket;
    }

    public void checkConnection() throws IOException {
        if (!socket.isConnected()){
            for (int i = 1; i <= ChatServer.connectionArray.size(); i++) {
                if (ChatServer.connectionArray.get(i) == socket){
                    ChatServer.connectionArray.remove(i);
                }
            }

            for (Socket tempSocket : ChatServer.connectionArray) {
                PrintWriter OUTPUT = new PrintWriter(tempSocket.getOutputStream());
                OUTPUT.println(tempSocket.getLocalAddress().getHostName()+ "disconnected");
                OUTPUT.flush();
                //show disconnection at server
                System.out.println(tempSocket.getLocalAddress().getHostName()+ " disconnected");
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

                    System.out.println("Client said: " + message);

                    for (Socket tempSocket : ChatServer.connectionArray) {
                        PrintWriter output = new PrintWriter(tempSocket.getOutputStream());
                        output.println(message);
                        output.flush();
                        System.out.println("Sent to: " + tempSocket.getLocalAddress().getHostName());
                    }
                }
            }finally {
                socket.close();
            }
        }catch (Exception x){
            System.out.println(x);
        }
    }
}
