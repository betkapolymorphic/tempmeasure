package com.springapp.mvc.Model;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Beta on 11/21/14.
 */
@Entity
@Table(name = "sample_record")
public class SampleRecord implements Comparable<SampleRecord>,Cloneable {
    public SampleRecord() {
    }


    private int idsample;
    private Float humidity = null;
    private Float temperature = null;
    private Measurment measurment;
    private Date sample_time;


    public void setSample_time(Date sample_time) {
        this.sample_time = sample_time;
    }

    public SampleRecord(Float temperature, Float humidity,Date d) {
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
    public Float getHumidity() {
        return humidity;
    }

    @Column(name="temperature")
    public  Float getTemperature() {
        return temperature;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMeasurment")
    public Measurment getMeasurment() {
        return measurment;
    }

    public JSONObject toJson(){
        JSONObject sampleJSON = new JSONObject();
        try {
            sampleJSON.put("humidity",getHumidity()==null?"null":getHumidity());
            sampleJSON.put("temperature",getTemperature()==null?"null":getTemperature());
            sampleJSON.put("date",getSample_time());
            sampleJSON.put("timespan",getSample_time().getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sampleJSON;
    }


    public void setIdsample(int idsample) {
        this.idsample = idsample;
    }

    public void setMeasurment(Measurment measurment) {
        this.measurment = measurment;
    }

    public void setHumidity( Float humidity) {
        this.humidity = humidity;
    }

    public void setTemperature( Float temperature) {
        this.temperature = temperature;
    }


}
