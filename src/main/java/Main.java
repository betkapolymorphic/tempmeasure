import com.springapp.mvc.Model.Measurment;
import com.springapp.mvc.Model.SampleRecord;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Map;

/**
 * Created by Beta on 11/21/14.
 */
public class Main {
    int q;
    public static void main(String[] args) {
        Measurment measurment = new Measurment();
        SampleRecord sampleRecord1 = new SampleRecord();
        sampleRecord1.setIdsample(1);
        SampleRecord sampleRecord2 = new SampleRecord();
        sampleRecord2.setIdsample(3);
        SampleRecord sampleRecord3 = new SampleRecord();
        sampleRecord3.setIdsample(2);
        measurment.getSamples().add(sampleRecord1);
        measurment.getSamples().add(sampleRecord2);
        measurment.getSamples().add(sampleRecord3);
        int p = 1;
    }
}
