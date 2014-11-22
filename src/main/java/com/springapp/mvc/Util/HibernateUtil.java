package com.springapp.mvc.Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Beta on 8/2/14.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private HibernateUtil(){}


    public static SessionFactory getSessionFactory() throws MalformedURLException {
        if(sessionFactory==null) {

            sessionFactory = new Configuration()
                    .configure(new URL("http://colorchicken.esy.es/temp_measur/hibernate.cfg.xml")).buildSessionFactory();
        }
        return sessionFactory;
    }
}
