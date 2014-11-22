package com.springapp.mvc.DAO;

import com.springapp.mvc.Model.Measurment;
import com.springapp.mvc.Model.SampleRecord;
import com.springapp.mvc.Util.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import  java.util.List;
import java.util.Date;
import java.util.Set;

/**
 * Created by Beta on 11/21/14.
 */
public class MeasurmentDaoImpl implements MeasurmentDAO {
    public static void main(String[] args) {
        new MeasurmentDaoImpl().getMeasurment("admin",4);

    }
    public List<Measurment> getMeasurments(String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Criteria cr = session.createCriteria(Measurment.class);
        cr.add(Restrictions.like("username",username));

        List list = cr.list();
        session.getTransaction().commit();
        return list;
    }
    @Override
    public  Measurment getMeasurment(String username,int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Criteria cr = session.createCriteria(Measurment.class);
        cr.add(Restrictions.like("username",username));
        cr.add(Restrictions.like("idMeasurment",id));

        List list = cr.list();
        session.getTransaction().commit();
        if(list.size()==0){
            return null;
        }
        return (Measurment) list.get(0);
    }

    @Override
    public void save(Measurment m,boolean newRecords) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(m);
        Set<SampleRecord> samples = m.getSamples();
        for(SampleRecord s : samples){
            s.setMeasurment(m);
            if(newRecords){
                session.save(s);
            }
        }
       /// session.save(m);
        session.getTransaction().commit();
    }

    @Override
    public void delete(Measurment m) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query q = session.createQuery("delete Measurment where id = "+m.getIdMeasurment());
        q.executeUpdate();
       // session.delete(m);
        for(SampleRecord sr : m.getSamples()){
            session.delete(sr);
        }
        session.getTransaction().commit();
    }
}
