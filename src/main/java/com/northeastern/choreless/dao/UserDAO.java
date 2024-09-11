package com.northeastern.choreless.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.northeastern.choreless.model.User;
import com.northeastern.choreless.util.HibernateUtil;

import jakarta.transaction.Transactional;

@Repository
public class UserDAO {
	@Autowired
    private SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();;

    @Transactional
    public User save(User user) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.merge(user);
            return user;
        } catch (Exception e) {
            throw e; // Optionally, you can handle the exception more gracefully
        }
    }
}
