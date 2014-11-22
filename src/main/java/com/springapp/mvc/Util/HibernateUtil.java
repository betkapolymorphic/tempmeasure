package com.springapp.mvc.Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Created by Beta on 8/2/14.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private HibernateUtil(){}


    public static SessionFactory getSessionFactory() {
        if(sessionFactory==null) {

            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml").buildSessionFactory();
        }
        return sessionFactory;
    }
}
