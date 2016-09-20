package client;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO {

    private static Set<User> users = new HashSet<>();
    private static Connection connection = null;  // соединение с БД
    private static Statement statement = null; // operator



    public static void getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/webchat_db";
            connection = DriverManager.getConnection(url, "root", "root");
            statement = connection.createStatement();
        } catch (Exception e){
            System.out.println("Exception in getConnection()");
        }
    }

    public static User getUserByLogin(String userName){
        try {
                Set<User> users = getAllUsers();

                for (User temp : users) {
                    if (temp.getUsername().equals(userName)) {
                    return temp;
                    }
                }
                throw new Exception();
            }catch (Exception e){
                System.out.println("USER NOT FOUND");
                return null;
        }
    }
    public static boolean checkPassword(String userFromTF, String userFromDB){

        return userFromTF.equals(userFromDB) ? true : false ;

    }

    public static boolean logIn(User user) throws Exception {
        getConnection();
        User tempUser = getUserByLogin(user.getUsername());

            if (checkPassword(user.getPassword(), tempUser.getPassword())){
                return true;
            } else {
                throw new Exception();
            }
    }


    public static Set<User> getAllUsers(){
        getConnection();
        String sql = "SELECT * FROM users";
        try{
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String login = resultSet.getString(2);
                String password = resultSet.getString(3);
                User user = new User(login, password);
                users.add(user);
            }
            resultSet.close();
        }catch (SQLException e){
            System.out.println("Exception in getAllUsers()");
        }
        return users;
    }



    public static boolean addUser(User user) throws Exception {

        getConnection();
        Set<User> userSet = getAllUsers();

        for (User tempUser : userSet ) {
            if(tempUser.getUsername().equals(user.getUsername())){
                throw new Exception();
            }
        }
        String sql = "INSERT INTO users (user_login, user_password) VALUES ('"+user.getUsername()+"', '"+user.getPassword()+"')";
        try {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println(">>>> "+e.getMessage());
            return false;
        }
    }
}
