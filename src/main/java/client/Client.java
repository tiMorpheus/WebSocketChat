package client;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    Socket socket;
    Scanner in;
    PrintWriter out;


    public Client(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try{
            try{
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                out.flush();
                checkStream();

            }finally {
                socket.close();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //----------------------------------------------------------------------------------------------
    public void disconnect() throws IOException {
        out.println(ClientGUI.userName + " has disconnected.");
        out.flush();
        socket.close();
        JOptionPane.showMessageDialog(null,"you disconnected!");
        System.exit(0);
    }

    //----------------------------------------------------------------------------------------------
    public void checkStream(){
        while (true){
            receive();
        }
    }

    //----------------------------------------------------------------------------------------------
    public void receive(){
        if (in.hasNext()){
            String MESSAGE = in.nextLine();

            if (MESSAGE.contains("#?!")){
                String TEMP1 = MESSAGE.substring(3);
                TEMP1 = TEMP1.replace("[","");
                TEMP1 = TEMP1.replace("]","");

                String[] currentUsers = TEMP1.split(", ");
                ClientGUI.JL_ONLINE.setListData(currentUsers);
            }else {
                ClientGUI.TA_CONVERSATION.append(MESSAGE + "\n");
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    public void SEND(String X){
        out.println(ClientGUI.userName + ": " + X);
        out.flush();
        ClientGUI.TF_Message.setText("");
    }
    //----------------------------------------------------------------------------------------------

}