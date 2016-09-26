package com.webchat.crud;

import com.webchat.model.User;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class UserService {
    public EntityManager em = Persistence.createEntityManagerFactory("WebChat")
                                            .createEntityManager();

    public User add(User user){

        em.getTransaction().begin();
        User userFromDB = em.merge(user);
        em.getTransaction().commit();
        return userFromDB;
    }

    public User getById(long id){
        return em.find(User.class, id);
    }

    public User getByName(String login){
        TypedQuery<User> namedQuery = em.createNamedQuery("User.getByLogin", User.class);
        return namedQuery.setParameter("login", login).getSingleResult();
    }

    public List<User> getAll(){
        TypedQuery<User> namedQuery = em.createNamedQuery("User.getAll", User.class);
        return namedQuery.getResultList();
    }
}
