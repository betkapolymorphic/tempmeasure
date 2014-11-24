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
/*
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(allSaplesByPeriod.get(0).getSample_time()); //
        Date count_time = cal.getTime();
        SampleRecord curSampleRecord = allSaplesByPeriod.get(0);
        int listCounter = 0;

        while(count_time.compareTo(allSaplesByPeriod.get(allSaplesByPeriod.size()-1).getSample_time()) <0 ){
            cal.add(Calendar.HOUR, (int) preriod);
            count_time = cal.getTime();
            if(count_time.compareTo(curSampleRecord.getSample_time()) < 0 ){
                allSaplesByPeriod.add(listCounter,new SampleRecord(0,0,count_time));
            }else{
                if(listCounter>=allSaplesByPeriod.size()){
                    break;
                }
                curSampleRecord = allSaplesByPeriod.get(listCounter);
            }
            listCounter++;
        }*/

@Controller
@RequestMapping("/")
public class HelloController {
    MeasurmentDAO measurmentDAO = new MeasurmentDaoImpl();
    SampleRecordDao sampleRecordDao =new SampleRecordImpl();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
    @RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        List<Measurment> measurments = measurmentDAO.getMeasurments(name);
        model.addAttribute("name",name);
        model.addAttribute("measurments",measurments);
		return "main";
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


    @RequestMapping(value = "/api/getMeasureAll",method = RequestMethod.GET)
    @ResponseBody
    String getAllMeasure(@RequestParam("periodHours")String period){
        float preriod = Float.parseFloat(period);
        List<MeasurmentDAO.SampleRecordAverage> allSaplesByPeriod
                = measurmentDAO.getAllSaplesByPeriod(SecurityContextHolder.getContext().getAuthentication().getName(), preriod);

        if(allSaplesByPeriod.size()==0){
            return "";
        }
        Collections.sort(allSaplesByPeriod,new Comparator<MeasurmentDAO.SampleRecordAverage>() {
            @Override
            public int compare(MeasurmentDAO.SampleRecordAverage o1, MeasurmentDAO.SampleRecordAverage o2) {
                return o1.mid.compareTo(o2.mid);
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(allSaplesByPeriod.get(0).mid.getSample_time());
        List<MeasurmentDAO.SampleRecordAverage> averageList = new ArrayList<MeasurmentDAO.SampleRecordAverage>();
        int index = 0;
        while(index<allSaplesByPeriod.size()){
            if(calendar.getTime().before(allSaplesByPeriod.get(index).mid.getSample_time())){
                averageList.add(new MeasurmentDAO.SampleRecordAverage(null,new SampleRecord(null,null,calendar.getTime()),null));
            }else{
                averageList.add(allSaplesByPeriod.get(index++));
            }
            calendar.add(Calendar.HOUR, (int) preriod);
        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("pointInterval",3600 * 1000 * (int)preriod);

            jsonObject.put("name","Temperature ");
            jsonObject.put("titleX","Temperature Rate ");
            jsonObject.put("title","All sample records period : "+ period);
            jsonObject.put("minRange",3600 * 1000 * (int)preriod*allSaplesByPeriod.size());
            JSONArray jsonArray = new JSONArray();

            int i=0;
            for(MeasurmentDAO.SampleRecordAverage sampleRecord: averageList){
                if(++i>=21){
                    int p=1;
                }
                JSONObject jObject = sampleRecord.toJson();
                //jObject.put("day",sampleRecord.getSample_time().getDay());
                //jObject.put("month",sampleRecord.getSample_time().getMonth());
                //jObject.put("year",1900 + sampleRecord.getSample_time().getYear());
                //jObject.put("timespan",sampleRecord.getSample_time().getTime());

                jsonArray.put(jObject);
            }
            jsonObject.put("samples",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return jsonObject.toString();
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
    //@ResponseBody
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


                return "success";
            } catch (Exception e) {
                return "error";
                //new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);

            }
        } else {
            return "error";
        }
    }

}