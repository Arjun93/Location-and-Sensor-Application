package com.example.arjuns.hw02;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by arjuns on 6/22/2015.
 */
public class Tab1Fragment extends Fragment {
    TextView myAndrewID, currentDate, myPhoneSensors;
    DateFormat dateFormat;
    Date date;
    SensorManager mySensorManager;
    List<Sensor> myPhoneListOfSensors;
    String result;
    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.sensortab,container,false);

        /*Referencing the UI components in sensortab.xml*/
        myAndrewID = (TextView)myView.findViewById(R.id.textView2);
        currentDate = (TextView)myView.findViewById(R.id.textView4);
        myPhoneSensors = (TextView)myView.findViewById(R.id.textView6);

        myAndrewID.setText(getText(R.string.my_andrew_id)); //Setting andrew id from strings.xml

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Specifying the format in which date and time must be printed
        date=new Date();
        currentDate.setText(dateFormat.format(date)); //Setting date to the textview.
        Log.i("DATE AND TIME ", dateFormat.format(date)); //Printing the date and time in logcat.

        mySensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        myPhoneListOfSensors = mySensorManager.getSensorList(Sensor.TYPE_ALL); //Obtain sensor list from phone

        result=new String(); //string for concatenating all the sensor names

        for(int i=0;i<myPhoneListOfSensors.size();i++) {
            result = result + myPhoneListOfSensors.get(i).getName()+"\n";
            myPhoneSensors.setText(result);
            Log.i("SENSORS ", myPhoneListOfSensors.get(i).getName()); //Printing each sensor name in logcat.
        }

        return myView;
    }
}
