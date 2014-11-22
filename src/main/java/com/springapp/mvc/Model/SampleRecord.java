package com.springapp.mvc.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Beta on 11/21/14.
 */
@Entity
@Table(name = "sample_record")
public class SampleRecord implements Comparable<SampleRecord> {
    public SampleRecord() {
    }


    private int idsample;
    private float humidity;
    private float temperature;
    private Measurment measurment;
    private Date sample_time;


    public void setSample_time(Date sample_time) {
        this.sample_time = sample_time;
    }

    public SampleRecord(float temperature, float humidity,Date d) {
        this.sample_time = d;
        this.temperature = temperature;
        this.humidity = humidity;
    }


    @Override
    public int compareTo(SampleRecord o){
       return this.getSample_time().compareTo(o.getSample_time());
    }
    @Id
    @Column(name="idsample")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getIdsample() {
        return idsample;
    }

    @Column(name="sample_time")
    public Date getSample_time() {
        return sample_time;
    }


    @Column(name="humidity")
    public float getHumidity() {
        return humidity;
    }
    @Column(name="temperature")
    public float getTemperature() {
        return temperature;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMeasurment")
    public Measurment getMeasurment() {
        return measurment;
    }


    public void setIdsample(int idsample) {
        this.idsample = idsample;
    }

    public void setMeasurment(Measurment measurment) {
        this.measurment = measurment;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }


}
