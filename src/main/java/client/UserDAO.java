package client;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO {

    private static Set<User> users = new HashSet<>();
    private static Connection connection = null;  // соединение с БД
    private static Statement statement = null; // operator

    // UserDao logger
    private static final Logger log = Logger.getLogger(UserDAO.class);

    /**
     * Create connection to the MySQL
     */
    public static void getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/webchat_db";
            connection = DriverManager.getConnection(url, "root", "root");
            statement = connection.createStatement();
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * Search User in database
     * @param userName target name
     * @return User if userName is found otherwise return null
     */
    public static User getUserByLogin(String userName){
        try {
                Set<User> users = getAllUsers();

                for (User tempUser : users) {
                    if (tempUser.getUsername().equals(userName)) {
                    return tempUser;
                    }
                }
                throw new Exception();
            }catch (Exception e){
                log.error(e.getMessage(), e);
                return null;
        }
    }

    public static boolean checkPassword(String userFromTF, String userFromDB){

        return userFromTF.equals(userFromDB) ? true : false ;
    }


    /**
     * here occur loggin magic
     * @param user user from LogIn form
     * @return true if user from form equals user from DB otherwise return false
     * @throws Exception
     */
    public static boolean logIn(User user) throws Exception {
        getConnection();
        User tempUser = getUserByLogin(user.getUsername());

            if (checkPassword(user.getPassword(), tempUser.getPassword())){
                return true;
            } else {
                throw new Exception();
            }
    }

    /**
     * Get all Users from DB
     * @return list of users from DB
     */
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
            log.error(e.getMessage(),e);
        }
        return users;
    }

    /**
     * Add User to DB
     * @param user from registration form
     * @return true if adding user was correct otherwise return false
     * @throws Exception
     */
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
            log.error(e.getMessage(),e);
            return false;
        }
    }

}
