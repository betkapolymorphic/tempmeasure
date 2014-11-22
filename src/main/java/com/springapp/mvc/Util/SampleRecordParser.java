package com.springapp.mvc.Util;

/**
 * Created by Beta on 11/21/14.
 */
import com.springapp.mvc.Model.SampleRecord;
import org.omg.CORBA.portable.InputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SampleRecordParser {
    private static final float CONST_TIME_SECONDS =  60*60;//1 hour
    public static Set<SampleRecord> parse(byte[] b,Date beginMeasure,float frequency){
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(beginMeasure); // sets calendar time/date

        ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
        Scanner sc = new Scanner(inputStream,"UTF-8");
        Set<SampleRecord> records = new HashSet<SampleRecord>();
        Pattern pattern = Pattern.compile("(Current humidity = )(.*?)(%.*?temperature = )(.*?)(C)");


        float curHum = 0;
        float curTemp = 0;
        float timeCounter = 0;
        int counter= 0;
        while (sc.hasNext()){
            String s1 = sc.nextLine();
            timeCounter+=frequency;
            if(!s1.contains("Error")){

                Matcher m = pattern.matcher(s1);
                if(m.matches()){
                    counter++;
                    //records.add(new SampleRecord(Integer.parseInt(m.group(4)),
                    //        Integer.parseInt(m.group(2))));
                    curTemp += Float.parseFloat(m.group(4));
                    curHum += Float.parseFloat(m.group(2));
                }
            }

            if(timeCounter>=CONST_TIME_SECONDS){

                records.add(new SampleRecord(curTemp/counter,curHum/counter,cal.getTime()));
                cal.add(Calendar.HOUR,1);
                cal.setTime(cal.getTime());
                curHum = 0;
                curTemp = 0;
                counter = 0;
                timeCounter = 0;
            }
        }
        if(counter!=0){
            records.add(new SampleRecord(curTemp/counter,curHum/counter,cal.getTime()));
            cal.add(Calendar.HOUR,1);
            cal.setTime(cal.getTime());
        }
        return records;
    }


}
