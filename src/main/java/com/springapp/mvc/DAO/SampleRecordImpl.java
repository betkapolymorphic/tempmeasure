package com.springapp.mvc.DAO;

import com.springapp.mvc.Model.Measurment;
import com.springapp.mvc.Model.SampleRecord;
import com.springapp.mvc.Util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Beta on 11/21/14.
 */
public class SampleRecordImpl implements SampleRecordDao{

    @Override
    public void save(SampleRecord sampleRecord, Session session) {
        session.save(sampleRecord);
    }



    @Override
    public TreeSet<SampleRecord> getRecords(Date d1, Date d2,String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        List<SampleRecord> list = session.createQuery("FROM  SampleRecord sr" +
                " where sr.measurment.username= :username and sample_time" +
                " between :start_date and :end_date")
                .setParameter("start_date", d1)
                .setParameter("username",username)
                .setParameter("end_date", d2).list();
        TreeSet ts = new TreeSet();
        for(SampleRecord sr : list){
            ts.add(sr);
        }
        return ts;
    }

    @Override
    public TreeSet<SampleRecord> getAllRecords(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        List<SampleRecord> list = session.createQuery("FROM SampleRecord sr where sr.measurment.username=:username  order by sample_time")
                .setParameter("username",username)
                .list();
        TreeSet ts = new TreeSet();
        for(SampleRecord sr : list){
            ts.add(sr);
        }
        return ts;
    }
}
