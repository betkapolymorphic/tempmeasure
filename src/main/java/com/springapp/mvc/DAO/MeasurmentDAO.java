package com.springapp.mvc.DAO;

import com.springapp.mvc.Model.Measurment;

import java.util.List;

/**
 * Created by Beta on 11/21/14.
 */
public interface MeasurmentDAO {
    public List<Measurment> getMeasurments(String username);
    public void save(Measurment m,boolean newValues);
    public void delete(Measurment m);

    Measurment getMeasurment(String name, int s);
}
