package service;

import com.webchat.crud.UserService;
import com.webchat.model.User;
import org.junit.Test;


public class UserServiceTest {

    UserService service = new UserService();

    @Test
    public void testSaveRecord() throws Exception{
        User user1 = new User("2","2");

        User user = service.add(user1);

        System.out.println(user);
    }

    @Test
    public void duplicateTest() throws Exception{
        User original = new User("boss","pass1");

        User duplicate = new User("boss","pass2");

        User userMain = service.add(original);
        try{
            User userDuplicate = service.add(duplicate);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSelect(){
        User user = new User("1", "1");

        User userFromDB = service.getByName(user.getLogin());

        System.out.println(userFromDB);
    }


}
