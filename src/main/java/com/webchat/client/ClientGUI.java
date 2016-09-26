package com.webchat.client;

import com.webchat.dao.UserDao;
import com.webchat.dao.impl.UserDaoImpl;
import com.webchat.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Technical class which
 * creates main frame, configure buttons
 * builds a logIn and registration form
 */
public class ClientGUI {

    //Globals
    private static Client chatClient;
    public static String userName = "Anonymous";

    // Dao
    private static UserDao userDao = new UserDaoImpl();

    // logger
    private static final Logger log = LoggerFactory.getLogger(ClientGUI.class);

    //GUI Globals - Main Window
    public  static JFrame mainWindow            = new JFrame();
    private static JButton bAbout               = new JButton();
    private static JButton bLogin               = new JButton();
    private static JButton bDisconnect          = new JButton();
    private static JButton bRegistrationMain    = new JButton();
    private static JButton bSEND                = new JButton();
    private static JLabel lMessage              = new JLabel();
    public  static JTextField tfMessage         = new JTextField(20);
    private static JLabel lConversation         = new JLabel();
    public  static JTextArea taConversation     = new JTextArea();
    private static JScrollPane spConversation   = new JScrollPane();
    private static JLabel lOnline               = new JLabel();
    public  static JList jlOnline               = new JList();
    private static JScrollPane spOnline         = new JScrollPane();
    private static JLabel lLoggedInAs           = new JLabel();
    private static JLabel lLoggedInAsBox        = new JLabel();

    //GUI Globals - LogIn window
    public  static JFrame logInWindow            = new JFrame();
    public  static JTextField tfUsernameBoxLogIn = new JTextField(20);
    private static JButton bEnterLogIn           = new JButton("Log in");
    private static JLabel lEnterUserNameLogIn    = new JLabel("Enter username: ");
    private static JLabel lEnterPasswordLogIn    = new JLabel("Enter password: ");
    public  static JTextField tfPasswordBoxLogIn = new JTextField(20);
    private static JPanel pLoginForm             = new JPanel();

    //GUI Globals - Registration window
    public  static JFrame registrationWindow            = new JFrame();
    public  static JTextField tfRegistrationUsername    = new JTextField(20);
    private static JButton bRegistration                = new JButton("REGISTRATION");
    private static JLabel lRegistrationUsername         = new JLabel("Enter username: ");
    private static JLabel lRegistrationPassword         = new JLabel("Enter password: ");
    public  static JTextField tfRegistrationPasswordBox = new JTextField(20);
    private static JPanel pRegistrationForm             = new JPanel();

    /**
     * Create connect with the server socket
     * Send Username to the server's online list of users
     * Make a thread for a new User
     */
    public static void connect(){
        try {
            final int PORT = 8085;
            final String HOST = "localhost";

            Socket socket = new Socket(HOST, PORT);
            log.info("You connected to: " + HOST);

            chatClient = new Client(socket);

            // Send name to add to "Online" list
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            output.println(userName);
            output.flush();


            Thread X = new Thread(chatClient);
            X.start();

        } catch (Exception e) {
            log.error("Server not responding", e);
            JOptionPane.showMessageDialog(null, "Server not responding");
            System.exit(0);
        }

    }

    // start program
    public static void main(String[] args) {
        buildMainWindow();
        initialize();
    }

    /**
     * Build main frame, and call configure method to set a content on client's UI
     */
    public static void buildMainWindow(){
        mainWindow.setTitle(userName + "'s Chat Box");
        mainWindow.setSize(450,500);
        mainWindow.setLocation(220,180);
        mainWindow.setResizable(false);

        configureMainWindow();
        mainWindowAction();

        mainWindow.setVisible(true);
    }

    /**
     * Set the main buttons visibility
     */
    public static void initialize() {
        bSEND.setEnabled(false);
        bDisconnect.setEnabled(false);
        bLogin.setEnabled(true);
    }

    /**
     * Configure a buttons, scrolls, text fields , text areas and other things
     */
    public static void configureMainWindow(){
        mainWindow.setBackground(new Color(255, 255, 255));
        mainWindow.setSize(500, 320);
        mainWindow.getContentPane().setLayout(null);

        bSEND.setBackground(new Color(0, 0, 255));
        bSEND.setForeground(new Color(255, 255, 255));
        bSEND.setText("Send");
        mainWindow.getContentPane().add(bSEND);
        bSEND.setBounds(10, 40, 110, 25);

        bDisconnect.setBackground(new Color(0, 0, 255));
        bDisconnect.setForeground(new Color(255, 255, 255));
        bDisconnect.setText("DISCONNECT");
        mainWindow.getContentPane().add(bDisconnect);
        bDisconnect.setBounds(130, 40, 110, 25);

        bLogin.setBackground(new Color(0, 0, 255));
        bLogin.setForeground(new Color(255, 255, 255));
        bLogin.setText("logIN");
        bLogin.setToolTipText("");
        mainWindow.getContentPane().add(bLogin);
        bLogin.setBounds(340, 40, 75, 25);

        bRegistrationMain.setBackground(new Color(0, 0, 255));
        bRegistrationMain.setForeground(new Color(255, 255, 255));
        bRegistrationMain.setText("REG");
        mainWindow.getContentPane().add(bRegistrationMain);
        bRegistrationMain.setBounds(420, 40, 70, 25);

        bAbout.setBackground(new Color(0, 0, 255));
        bAbout.setForeground(new Color(255, 255, 255));
        bAbout.setText("ABOUT");
        mainWindow.getContentPane().add(bAbout);
        bAbout.setBounds(250, 40, 81, 25);

        lMessage.setText("Message: ");
        mainWindow.getContentPane().add(lMessage);
        lMessage.setBounds(10, 10, 60, 20);

        tfMessage.setForeground(new Color(0, 0, 255));
        tfMessage.requestFocus();
        mainWindow.getContentPane().add(tfMessage);
        tfMessage.setBounds(70,4, 260, 30);

        lConversation.setHorizontalAlignment(SwingConstants.CENTER);
        lConversation.setText("Conversation");
        mainWindow.getContentPane().add(lConversation);
        lConversation.setBounds(100, 70, 140, 16);

        taConversation.setColumns(20);
        taConversation.setFont(new Font("Tahoma", 0 , 12));
        taConversation.setForeground(new Color(0,0,255));
        taConversation.setLineWrap(true);
        taConversation.setRows(5);
        taConversation.setEditable(false);

        spConversation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spConversation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spConversation.setViewportView(taConversation);
        mainWindow.getContentPane().add(spConversation);
        spConversation.setBounds(10, 90, 330, 180);

        lOnline.setHorizontalAlignment(SwingConstants.CENTER);
        lOnline.setText("Currently Online");
        lOnline.setToolTipText("");
        mainWindow.getContentPane().add(lOnline);
        lOnline.setBounds(350, 70, 130, 16);

        jlOnline.setForeground(new Color(0, 0, 255));

        spOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spOnline.setViewportView(jlOnline);
        mainWindow.getContentPane().add(spOnline);
        spOnline.setBounds(350, 90, 130, 180);

        lLoggedInAs.setFont(new Font("Tahoma", 0, 12));
        lLoggedInAs.setText("Currently logged in as");
        mainWindow.getContentPane().add(lLoggedInAs);
        lLoggedInAs.setBounds(348,0,140,15);

        lLoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
        lLoggedInAsBox.setFont(new Font("Tahoma", 0, 12));
        lLoggedInAsBox.setForeground(new Color(255,0,0));
        lLoggedInAsBox.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        mainWindow.getContentPane().add(lLoggedInAsBox);
        lLoggedInAsBox.setBounds(340,17,150,20);

    }

    /**
     * Build LogIn frame when User click on "logIN" button
     * Set listener on button log in
     */
    public static void buildLogInWindow(){
        logInWindow.setTitle("Log in");
        logInWindow.setSize(400,120);
        logInWindow.setLocation(250,200);
        logInWindow.setResizable(false);
        pLoginForm.add(lEnterUserNameLogIn);
        pLoginForm.add(tfUsernameBoxLogIn);
        pLoginForm.add(lEnterPasswordLogIn);
        pLoginForm.add(tfPasswordBoxLogIn);
        pLoginForm.add(bEnterLogIn);
        logInWindow.add(pLoginForm);

        logInAction();
        logInWindow.setVisible(true);
    }

    /**
     * Set actionListener on "Log in" button in LogIN frame
     */
    public static void logInAction(){
        bEnterLogIn.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                logInButtonAction();}
        });
    }


    /**
     * Build registration frame when User click on "REG" button
     */
    public static void buildRegistrationWindow(){
        registrationWindow.setTitle("Registration");
        registrationWindow.setSize(400,120);
        registrationWindow.setLocation(250,200);
        registrationWindow.setResizable(false);
        pRegistrationForm.add(lRegistrationUsername);
        pRegistrationForm.add(tfRegistrationUsername);
        pRegistrationForm.add(lRegistrationPassword);
        pRegistrationForm.add(tfRegistrationPasswordBox);
        pRegistrationForm.add(bRegistration);
        registrationWindow.add(pRegistrationForm);

        registrationAction();
        registrationWindow.setVisible(true);
    }

    /**
     * Set listener on "Registration" button in registration form
     */
    public static void registrationAction(){
        bRegistration.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                registrationButtonAction();}
        });
    }

    /**
     * Read values from text fields and send it to UserDao which check
     * current values on duplicate. If values are correct, method add Username
     * to "online list" and call connect() to start chatting
     */
    public static void logInButtonAction(){

        if (!tfUsernameBoxLogIn.getText().equals("") & !tfPasswordBoxLogIn.getText().equals("")){

            userName = tfUsernameBoxLogIn.getText().trim();
            String password = tfPasswordBoxLogIn.getText().trim();

            User logInUser = new User(userName,password);
            try {
               if(userDao.loggin(logInUser)){
                   lLoggedInAsBox.setText(userName);
                   mainWindow.setTitle(userName + "'s chat box");
                   logInWindow.setVisible(false);
                   bSEND.setEnabled(true);
                   bDisconnect.setEnabled(true);
                   bLogin.setEnabled(false);
                   connect();
               } else {
                   JOptionPane.showMessageDialog(null,"Неправильные данные");
               }
            }catch (Exception e){
               JOptionPane.showMessageDialog(null,"ERROR");
               log.warn(e.getMessage(),e);
            }
        } else {
            JOptionPane.showMessageDialog(null,"Введите логин и пароль");
        }
    }

    /**
     * Read fields in form and send in to UserDao to add in DATABASE
     */
    public static void registrationButtonAction(){
        User registrationUser;

        if (!tfRegistrationUsername.getText().equals("") & !tfRegistrationPasswordBox.getText().equals("")) {
            String login = tfRegistrationUsername.getText().trim();
            String password = tfRegistrationPasswordBox.getText().trim();

            registrationUser = new User(login, password);
            try {
                if(userDao.registrate(registrationUser)){
                JOptionPane.showMessageDialog(null, "ВАС ДОБАВИЛИ В БАЗУ ДАННЫХ");
                    registrationWindow.setVisible(false);
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Такой пользователь уже есть");
                log.warn(e.getMessage(),e);
            }
        }else {
            JOptionPane.showMessageDialog(null, "Введите все поля");
        }
    }

    /**
     * Set listeners on "SEND","DISCONNECT","LogIn","REG","ABOUT" buttons
     */
    public static void mainWindowAction(){

        bSEND.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                sendButtonAction();}
        });

        bDisconnect.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                disconnectButtonAction();}
        });

        bLogin.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                buildLogInWindow();}
        });

        bRegistrationMain.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){buildRegistrationWindow();}
        });

        bAbout.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                helpButtonAction();}
        });

    }

    /**
     * Read value from "Message" text field and send it to the server
     * @see Client method send()
     */
    public static void sendButtonAction(){
        if (!tfMessage.getText().equals("")){
            chatClient.send(tfMessage.getText());
            tfMessage.requestFocus();
        }
    }

    /**
     * Disconnect user from server
     * @see Client method disconnect()
     */
    public static void disconnectButtonAction() {
        try {
            chatClient.disconnect();
        } catch (Exception e){
            log.warn(e.getMessage(),e);
        }
    }

    // Here can be some useful information for user))
    public static void helpButtonAction() {
        JOptionPane.showMessageDialog(null, "TYMUR RULES");
    }
}
