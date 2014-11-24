package com.springapp.mvc.DAO;

import com.springapp.mvc.Model.SampleRecord;
import org.hibernate.Session;

import java.util.Date;
import java.util.TreeSet;

/**
 * Created by Beta on 11/21/14.
 */
public interface SampleRecordDao {
    void save(SampleRecord sampleRecord,Session session);
    TreeSet<SampleRecord> getRecords(Date d1,Date d2,String username);
    TreeSet<SampleRecord> getAllRecords(String username);
}
