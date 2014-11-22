package com.springapp.mvc.Model;

/**
 * Created by Beta on 11/21/14.
 */
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "measurment")
public class Measurment  implements Comparable<Measurment> {
    public Measurment() {
    }

    public Measurment(String userName, String position, Date date) {
        this.userName = userName;
        this.position = position;
        this.date = date;
    }

    private Set<SampleRecord> samples = new TreeSet<SampleRecord>();
    private int idMeasurment;
    private Date date;
    private String position;
    private String userName;
    @Column(name="username")
    public String getUsername() {
        return userName;
    }


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "measurment")
    public Set<SampleRecord> getSamples() {
        return samples;
    }


    @Id
    @Column(name="idMeasurment")
    @GeneratedValue(strategy = GenerationType.AUTO)

    public int getIdMeasurment() {
        return idMeasurment;
    }
    @Column(name="Date")

    public Date getDate() {
        return date;
    }
    @Column(name="Position")
    public String getPosition() {
        return position;
    }

    public void setIdMeasurment(int idMeasument) {
        this.idMeasurment = idMeasument;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSamples(Set<SampleRecord> samples) {
        this.samples = samples;
    }


    public void setPosition(String position) {
        this.position = position;
    }
    public void setUsername(String userName) {
        this.userName = userName;
    }
    @Transient
    public int getSizeSamples(){
        return this.getSamples().size();
    }
    public void setSizeSamples(int q){

    }


    @Override
    public int compareTo(Measurment o) {
        return this.getIdMeasurment() - o.getIdMeasurment();
    }
}
