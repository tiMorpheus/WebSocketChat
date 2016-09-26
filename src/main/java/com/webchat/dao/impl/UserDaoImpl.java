package com.webchat.dao.impl;

import com.webchat.dao.UserDao;
import com.webchat.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class UserDaoImpl implements UserDao {

    public EntityManager em = Persistence.createEntityManagerFactory("WebChat")
                                            .createEntityManager();

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {

    }

    /**
     * Facade method to LogIn in chat
     *
     * @param user it's user from UI
     * @return true if all was fine otherwise return false
     */
    @Override
    public boolean loggin(User user) {
        try{
            log.debug("before searching userByName");
            User userFromDB = this.getByName(user.getLogin());
            log.debug("after searching userByName");

            //check password
            if (user.getPassword().equals(userFromDB.getPassword())){
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            log.warn("user enter incorrect value",e);
            return false;
        }

    }

    @Override
    public boolean registrate(User user) throws Exception {
        log.debug("enter in registrate");
        this.add(user);
        log.debug("end in registrate, registr was correct");
        return true;
    }
    public User add(User user){
        log.debug("enter in add()");
        em.getTransaction().begin();
        User userFromDB = em.merge(user);
        em.getTransaction().commit();
        log.debug("end in add()");
        return userFromDB;
    }

    public User getByName(String login){
        TypedQuery<User> namedQuery = em.createNamedQuery("User.getByLogin", User.class);
        return namedQuery.setParameter("login", login).getSingleResult();
    }

    public List<User> getAllUsers(){
        TypedQuery<User> namedQuery = em.createNamedQuery("User.getAll", User.class);
        return namedQuery.getResultList();
    }


}
