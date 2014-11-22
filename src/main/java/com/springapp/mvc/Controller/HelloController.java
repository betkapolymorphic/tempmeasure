package com.springapp.mvc.Controller;

import com.springapp.mvc.DAO.MeasurmentDAO;
import com.springapp.mvc.DAO.MeasurmentDaoImpl;
import com.springapp.mvc.DAO.SampleRecordDao;
import com.springapp.mvc.DAO.SampleRecordImpl;
import com.springapp.mvc.Model.Measurment;
import com.springapp.mvc.Model.SampleRecord;
import com.springapp.mvc.Util.SampleRecordParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class HelloController {
    MeasurmentDAO measurmentDAO = new MeasurmentDaoImpl();
    SampleRecordDao sampleRecordDao =new SampleRecordImpl();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}
    @RequestMapping(method = RequestMethod.GET,value = "/main")
    public String mainPanel(ModelMap modelMap)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        List<Measurment> measurments = measurmentDAO.getMeasurments(name);
        modelMap.addAttribute("name",name);
        modelMap.addAttribute("measurments",measurments);
        return "main";
    }

    @RequestMapping(value = "/measure/{measureId}", method = RequestMethod.GET)
    public String showMeasure(@PathVariable(value = "measureId") String measureId,ModelMap modelMap) {
        Measurment measurment = measurmentDAO.getMeasurment(SecurityContextHolder.getContext().getAuthentication().getName(),
                Integer.parseInt(measureId));
            modelMap.addAttribute("measureObj",measurment);
        return "measure";
    }


    @RequestMapping(value = "/api/getMeasurePeriod",method = RequestMethod.GET)
    @ResponseBody
    String getMeasurePeriod(@RequestParam("date1")String date1,
                      @RequestParam("date2")String date2){
        try {
            TreeSet<SampleRecord> records = sampleRecordDao.getRecords(dateFormat.parse(date1),
                    dateFormat.parse(date2)
                    , SecurityContextHolder.getContext().getAuthentication().getName());
            JSONObject jsonObject = new JSONObject();

            Set<Measurment> measurments = new TreeSet<Measurment>();


            JSONArray array = new JSONArray();
            for(SampleRecord sr : records){
                JSONObject sampleJSON = new JSONObject();
                sampleJSON.put("humidity",sr.getHumidity());
                sampleJSON.put("temperature",sr.getTemperature());
                sampleJSON.put("date",sr.getSample_time());
                measurments.add(sr.getMeasurment());
                array.put(sampleJSON);
            }


            String idMeasurment = "";
            String positions = "";
            for(Measurment m : measurments){
                idMeasurment+=m.getIdMeasurment()+",";
                positions+=m.getPosition()+",";
            }
            jsonObject.put("idMesurment",idMeasurment);
            jsonObject.put("Date",date1+" - "+date2);
            jsonObject.put("Position",positions);
            jsonObject.put("samples",array);
            return jsonObject.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "/getMeasure",method = RequestMethod.GET)
    @ResponseBody
    String getMeasure(@RequestParam("id")String id,
                      @RequestParam("compression")String compression){
        Measurment m = measurmentDAO.getMeasurment("admin", Integer.parseInt(id));
        if(m==null){
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idMesurment",m.getIdMeasurment());
            jsonObject.put("Date",m.getDate());
            jsonObject.put("Position",m.getPosition());
            JSONArray array = new JSONArray();
            int size = m.getSamples().size();
            int count = 0;
           int compresLevel = (int) Math.floor((double) size / Integer.parseInt(compression));
            float curTemp = 0;
            float curHum = 0;
            Set<SampleRecord> samples = new TreeSet<SampleRecord>();
             for(SampleRecord sr : m.getSamples()){
                samples.add(sr);
             }
            for(SampleRecord sr : samples){
                JSONObject sampleJSON = new JSONObject();
                sampleJSON.put("humidity",sr.getHumidity());
                sampleJSON.put("temperature",sr.getTemperature());
                sampleJSON.put("date",sr.getSample_time());
                array.put(sampleJSON);
            }
            jsonObject.put("samples",array);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
        MeasurmentDAO measurmentDAO = new MeasurmentDaoImpl();
        String name = "admin"; //get logged in username
        List<Measurment> measurments = measurmentDAO.getMeasurments(name);

    }


    @RequestMapping(value = "/removeMeasure", method = RequestMethod.POST)
    @ResponseBody
    String removeMeasurm(@RequestParam("id") String id) {
        Measurment measurment = measurmentDAO.getMeasurment(SecurityContextHolder.getContext().getAuthentication().getName(),
                Integer.parseInt(id));
        if(measurment==null){
            return "error bad id measurm!";
        }else{
            measurmentDAO.delete(measurment);
            return "success";
        }

    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    String uploadFileHandler(@RequestParam("position") String position,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("date") String date,
                             @RequestParam("frequency") String frequency) {

        if (!file.isEmpty()) {
            try {
                Date measureDate = dateFormat.parse(date);
                byte[] bytes = file.getBytes();
                Set<SampleRecord> parse = SampleRecordParser.parse(bytes,measureDate,Float.parseFloat(frequency));

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();
                Measurment measurment = new Measurment();
                measurment.setSamples(parse);

                measurment.setDate(measureDate );
                measurment.setUsername(username);
                measurment.setPosition(position);
                measurmentDAO.save(measurment,true);


                return "Success";
            } catch (Exception e) {
                return "Error"+e.getMessage();
                //new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);

            }
        } else {
            return "Error :(";
        }
    }

}