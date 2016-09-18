package client;

import server.ChatServer;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientGUI {

    //Globals
    private static Client chatClient;
    public static String userName = "Anonymous";

    //GUI Globals - Main Window
    public  static JFrame MainWindow           = new JFrame();
    private static JButton B_ABOUT             = new JButton();
    private static JButton B_LOGIN             = new JButton();
    private static JButton B_DISCONNECT        = new JButton();
    private static JButton B_RegistMain = new JButton();
    private static JButton B_SEND              = new JButton();
    private static JLabel L_Message            = new JLabel("Message: ");
    public  static JTextField TF_Message       = new JTextField(20);
    private static JLabel L_Conversation       = new JLabel();
    public  static JTextArea TA_CONVERSATION   = new JTextArea();
    private static JScrollPane SP_CONVERSATION = new JScrollPane();
    private static JLabel L_ONLINE             = new JLabel();
    public  static JList JL_ONLINE             = new JList();
    private static JScrollPane SP_ONLINE       = new JScrollPane();
    private static JLabel L_LoggedInAs         = new JLabel();
    private static JLabel L_LoggedInAsBox      = new JLabel();

    //GUI Globals - LogIn window
    public  static JFrame LogInWindow          = new JFrame();
    public  static JTextField TF_UserNameBox   = new JTextField(20);
    private static JButton B_ENTER             = new JButton("Log in");
    private static JLabel L_EnterUserName      = new JLabel("Enter username: ");
    private static JLabel L_EnterPassword      = new JLabel("Enter password: ");
    public  static JTextField TF_PasswordBox   = new JTextField(20);
    private static JPanel P_LogIn              = new JPanel();

    //GUI Globals - Registration window
    public  static JFrame RegistrationWindow            = new JFrame();
    public  static JTextField TF_RegistrationUsername   = new JTextField(20);
    private static JButton B_REGISTRATION               = new JButton("REGISTRATION");
    private static JLabel L_RegistrationUsername        = new JLabel("Enter username: ");
    private static JLabel L_RegistrationPassword        = new JLabel("Enter password: ");
    public  static JTextField TF_RegistrationPassword   = new JTextField(20);
    private static JPanel P_REGISTRATION                = new JPanel();

    public static void main(String[] args) {
        buildMainWindow();
        initialize();
    }
    //----------------------------------------------------------------------------------------------
    public static void connect(){

        try {
            final int PORT = 8085;
            final String HOST = "localhost";
            Socket SOCK = new Socket(HOST,PORT);
            System.out.println("You connected to: " + HOST);

            chatClient = new Client(SOCK);

            // Send name to add to "Online" list
            PrintWriter out = new PrintWriter(SOCK.getOutputStream());
            out.println(userName);
            out.flush();

            Thread X = new Thread(chatClient);
            X.start();

        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Server not responding");
            System.exit(0);
        }

    }

    //----------------------------------------------------------------------------------------------
    public static void initialize() {
        B_SEND.setEnabled(false);
        B_DISCONNECT.setEnabled(false);
        B_LOGIN.setEnabled(true);

    }

    //----------------------------------------------------------------------------------------------
    public static void buildLogInWindow(){
        LogInWindow.setTitle("Log in");
        LogInWindow.setSize(400,120);
        LogInWindow.setLocation(250,200);
        LogInWindow.setResizable(false);
        P_LogIn = new JPanel();
        P_LogIn.add(L_EnterUserName);
        P_LogIn.add(TF_UserNameBox);
        P_LogIn.add(L_EnterPassword);
        P_LogIn.add(TF_PasswordBox);
        P_LogIn.add(B_ENTER);
        LogInWindow.add(P_LogIn);

        Login_Action();
        LogInWindow.setVisible(true);
    }



    //----------------------------------------------------------------------------------------------
    public static void buildRegistrationWindow(){
        RegistrationWindow.setTitle("REGISTRATION");
        RegistrationWindow.setSize(400,120);
        RegistrationWindow.setLocation(250,200);
        RegistrationWindow.setResizable(false);
        P_REGISTRATION = new JPanel();
        P_REGISTRATION .add(L_RegistrationUsername);
        P_REGISTRATION.add(TF_RegistrationUsername);
        P_REGISTRATION.add(L_RegistrationPassword);
        P_REGISTRATION.add(TF_RegistrationPassword);
        P_REGISTRATION.add(B_REGISTRATION);
        RegistrationWindow.add(P_REGISTRATION);

        registration_Action();
        RegistrationWindow.setVisible(true);
    }

    //----------------------------------------------------------------------------------------------
    public static void buildMainWindow(){
        MainWindow.setTitle(userName + "'s Chat Box");
        MainWindow.setSize(450,500);
        MainWindow.setLocation(220,180);
        MainWindow.setResizable(false);
        configureMainWindow();
        MainWindow_Action();
        MainWindow.setVisible(true);
    }



    //----------------------------------------------------------------------------------------------
    public static void configureMainWindow(){
        MainWindow.setBackground(new Color(255, 255, 255));
        MainWindow.setSize(500, 320);
        MainWindow.getContentPane().setLayout(null);

        B_SEND.setBackground(new Color(0, 0, 255));
        B_SEND.setForeground(new Color(255, 255, 255));
        B_SEND.setText("SEND");
        MainWindow.getContentPane().add(B_SEND);
        B_SEND.setBounds(10, 40, 110, 25);

        B_DISCONNECT.setBackground(new Color(0, 0, 255));
        B_DISCONNECT.setForeground(new Color(255, 255, 255));
        B_DISCONNECT.setText("DISCONNECT");
        MainWindow.getContentPane().add(B_DISCONNECT);
        B_DISCONNECT.setBounds(130, 40, 110, 25);

        B_LOGIN.setBackground(new Color(0, 0, 255));
        B_LOGIN.setForeground(new Color(255, 255, 255));
        B_LOGIN.setText("logIN");
        B_LOGIN.setToolTipText("");
        MainWindow.getContentPane().add(B_LOGIN);
        B_LOGIN.setBounds(340, 40, 75, 25);

        B_RegistMain.setBackground(new Color(0, 0, 255));
        B_RegistMain.setForeground(new Color(255, 255, 255));
        B_RegistMain.setText("REG");
        MainWindow.getContentPane().add(B_RegistMain);
        B_RegistMain.setBounds(420, 40, 70, 25);

        B_ABOUT.setBackground(new Color(0, 0, 255));
        B_ABOUT.setForeground(new Color(255, 255, 255));
        B_ABOUT.setText("ABOUT");
        MainWindow.getContentPane().add(B_ABOUT);
        B_ABOUT.setBounds(250, 40, 81, 25);

        L_Message.setText("Message: ");
        MainWindow.getContentPane().add(L_Message);
        L_Message.setBounds(10, 10, 60, 20);

        TF_Message.setForeground(new Color(0, 0, 255));
        TF_Message.requestFocus();
        MainWindow.getContentPane().add(TF_Message);
        TF_Message.setBounds(70,4, 260, 30);

        L_Conversation.setHorizontalAlignment(SwingConstants.CENTER);
        L_Conversation.setText("Conversation");
        MainWindow.getContentPane().add(L_Conversation);
        L_Conversation.setBounds(100, 70, 140, 16);

        TA_CONVERSATION.setColumns(20);
        TA_CONVERSATION.setFont(new Font("Tahoma", 0 , 12));
        TA_CONVERSATION.setForeground(new Color(0,0,255));
        TA_CONVERSATION.setLineWrap(true);
        TA_CONVERSATION.setRows(5);
        TA_CONVERSATION.setEditable(false);


        SP_CONVERSATION.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_CONVERSATION.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_CONVERSATION.setViewportView(TA_CONVERSATION);
        MainWindow.getContentPane().add(SP_CONVERSATION);
        SP_CONVERSATION.setBounds(10, 90, 330, 180);

        L_ONLINE.setHorizontalAlignment(SwingConstants.CENTER);
        L_ONLINE.setText("Currently Online");
        L_ONLINE.setToolTipText("");
        MainWindow.getContentPane().add(L_ONLINE);
        L_ONLINE.setBounds(350, 70, 130, 16);

//        String [] TestNames = {"Tima", "Lina", "Dima"};
        JL_ONLINE.setForeground(new Color(0, 0, 255));
//        JL_ONLINE.setListData(TestNames);

        SP_ONLINE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_ONLINE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_ONLINE.setViewportView(JL_ONLINE);
        MainWindow.getContentPane().add(SP_ONLINE);
        SP_ONLINE.setBounds(350, 90, 130, 180);


        L_LoggedInAs.setFont(new Font("Tahoma", 0, 12));
        L_LoggedInAs.setText("Currently logged in as");
        MainWindow.getContentPane().add(L_LoggedInAs);
        L_LoggedInAs.setBounds(348,0,140,15);

        L_LoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
        L_LoggedInAsBox.setFont(new Font("Tahoma", 0, 12));
        L_LoggedInAsBox.setForeground(new Color(255,0,0));
        L_LoggedInAsBox.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        MainWindow.getContentPane().add(L_LoggedInAsBox);
        L_LoggedInAsBox.setBounds(340,17,150,20);

    }

    //----------------------------------------------------------------------------------------------
    public static void Login_Action(){
        B_ENTER.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                ACTION_B_LOGIN();}
        });
    }

    public static void registration_Action(){
        B_REGISTRATION.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){ACTION_B_REGISTRATION();}
        });
    }


    //----------------------------------------------------------------------------------------------
    public static void ACTION_B_LOGIN(){
        if (!TF_UserNameBox.getText().equals("") & !TF_PasswordBox.getText().equals("")){
            userName = TF_UserNameBox.getText().trim();
            L_LoggedInAsBox.setText(userName);
            ChatServer.currentUsers.add(userName);
            MainWindow.setTitle(userName + "'s chat box");
            LogInWindow.setVisible(false);
            B_SEND.setEnabled(true);
            B_DISCONNECT.setEnabled(true);
            B_LOGIN.setEnabled(false);
            connect();
        } else {
            JOptionPane.showMessageDialog(null,"Please enter a name!");
        }
    }


    public static void ACTION_B_REGISTRATION(){
        if (!TF_RegistrationUsername.getText().equals("") & !TF_RegistrationPassword.getText().equals("")) {



        }
    }

    //----------------------------------------------------------------------------------------------
    public static void MainWindow_Action(){

        B_SEND.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){ACTION_B_SEND();}
        });

        B_DISCONNECT.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){ACTION_B_DISCONNECT();}
        });

        B_LOGIN.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                buildLogInWindow();}
        });

        B_RegistMain.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){buildRegistrationWindow();}
        });

        B_ABOUT.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){ACTION_B_HELP();}
        });

    }


    //----------------------------------------------------------------------------------------------
    public static void ACTION_B_SEND(){
        if (!TF_Message.getText().equals("")){
            chatClient.SEND(TF_Message.getText());
            TF_Message.requestFocus();
        }
    }

    //----------------------------------------------------------------------------------------------
    public static void ACTION_B_DISCONNECT() {
        try {
            chatClient.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    public static void ACTION_B_HELP(){
        JOptionPane.showMessageDialog(null, "Chat program");
    }
    //----------------------------------------------------------------------------------------------
}
