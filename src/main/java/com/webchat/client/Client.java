package com.webchat.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;

public class Client implements Runnable {

    Socket socket;
    Scanner in;
    PrintWriter output;

    // Client logger
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            try{
                in = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream());
                output.flush();
                checkStream();

            }finally {
                socket.close();
            }
        }catch (Exception e){
            System.out.println(e);
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * Close socket connection and turn off the chat program
     * @throws IOException
     */
    public void disconnect() throws IOException {
        log.info(ClientGUI.userName + " has disconnected.");
        output.flush();
        socket.close();
        JOptionPane.showMessageDialog(null,"you disconnected!");
        System.exit(0);
    }

    public void checkStream(){
        while (true){
            receive();
        }
    }

    /**
     * Receive messages from the server
     */
    public void receive(){

        if (in.hasNext()){
            String message = in.nextLine();

            // If server return "online list"
            if (message.contains("#?!")){
                String strOnlineUsers = message.substring(3);
                strOnlineUsers = strOnlineUsers.replace("[","");
                strOnlineUsers = strOnlineUsers.replace("]","");

                String[] currentUsers = strOnlineUsers.split(", ");
                ClientGUI.jlOnline.setListData(currentUsers);
            }else {
                // add message to the client UI text area
                ClientGUI.taConversation.append(message + "\n");
            }
        }
    }

    /**
     * Send message from UI text field to the server(concat username to massage)
     * @param message from UI
     */
    public void send(String message){
        output.println(ClientGUI.userName + ": " + message);
        output.flush();
        //
        ClientGUI.tfMessage.setText("");
    }
}