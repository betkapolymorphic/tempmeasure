package com.springapp.mvc.DAO;

import com.springapp.mvc.Model.Measurment;
import com.springapp.mvc.Model.SampleRecord;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Beta on 11/21/14.
 */
public interface MeasurmentDAO {
    public static class SampleRecordAverage{
        public SampleRecordAverage(SampleRecord min, SampleRecord mid, SampleRecord max) {
            this.min = min;
            this.mid = mid;
            this.max = max;

        }

        public SampleRecord min;
        public SampleRecord mid;
        public SampleRecord max;
        public JSONObject toJson(){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("min",min == null ? "" :min.toJson());
                jsonObject.put("max",max == null? "":max.toJson());
                jsonObject.put("mid",mid.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

    }
    public List<Measurment> getMeasurments(String username);
    public void save(Measurment m,boolean newValues);
    public void delete(Measurment m);

    List<SampleRecordAverage> getAllSaplesByPeriod(String username,float periodHours);
    Measurment getMeasurment(String name, int idMeasure);
}
