package service;

import com.webchat.dao.impl.UserDaoImpl;
import com.webchat.models.Role;
import com.webchat.models.RoleList;
import com.webchat.models.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class UserServiceTest {

    UserDaoImpl service = new UserDaoImpl();

    @Test
    public void testSaveRecord() throws Exception{
        User user1 = new User("21","5");

        User user = service.add(user1);

        System.out.println(user);
    }

    @Test
    public void duplicateTest() throws Exception{
        User original = new User("boss2","pass1");

        User duplicate = new User("boss2","pass2");

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

    @Test
    public void testEmptySelect(){
        try {
            User user1 = new User("fantomas", "123");

            User userFromDB = service.getByName(user1.getLogin());

            System.out.println(userFromDB);
        } catch (Exception e){
            e.getMessage();
        }
    }

    @Test
    public void testAddRoleInDb(){
        User adm = new User();
        Role admin = new Role();
        admin.setTitle(RoleList.ADMIN);
        Set<Role> s = new HashSet<>();
        s.add(admin);
        adm.setLogin("adminus");
        adm.setPassword("admin");
        adm.setRoles(s);

        admin.setDescription("The head of the application, creator, god, sex symbol");

        service.em.getTransaction().begin();
        service.em.merge(adm);
        service.em.getTransaction().commit();


    }


}
