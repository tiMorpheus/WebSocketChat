package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class UserDAO {

    private Set<User> users = new HashSet<>();

    private Connection connection = null;  // соединение с БД
    private Statement statement = null; // operator

    public void getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:8085/webChat_db";
        connection = DriverManager.getConnection(url,"root","root");
        statement = connection.createStatement();
    }

    public User getAllUsers(){

    }


    public boolean addUser(){

    }





}
