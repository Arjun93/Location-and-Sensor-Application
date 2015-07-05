package com.example.arjuns.hw02;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by arjuns on 6/22/2015.
 */
public class Tab2Fragment extends Fragment {
    GoogleMap map;
    private static View view;
    String myAddress = null;
    Geocoder geocoder,myGeocoder;
    MapView myMapView;
    String myFirstAddress;
    LocationManager myLocationManager;
    LocationListener myLocationListener;
    Location myFirstLocation;
    LatLng myFirstLatLng;
    private static final int TIMEPERIOD = 1000;
    String myProvider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.maptab, container, false);

        myMapView = (MapView) view.findViewById(R.id.mapview);
        myMapView.onCreate(savedInstanceState);
        myMapView.onResume();
        map = myMapView.getMap();
        /*Get the first location using GPS Provider*/
        try {
            myLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
           // myFirstLocation = myLocationManager.getLastKnownLocation(myLocationManager.GPS_PROVIDER);
            myFirstLocation = myLocationManager.getLastKnownLocation(myLocationManager.NETWORK_PROVIDER);
        }
        catch(Exception e)
        {
            Log.i("EXCEPTION","Unable to get GPS Provider");
        }
        map.setMyLocationEnabled(true);

        /*If the application is unable to fetch the first location, use getBestProvider() */
        if (myFirstLocation == null) {
            Toast.makeText(getActivity().getApplicationContext(), "GPS not available", Toast.LENGTH_SHORT).show();
            Log.i("EXCEPTION","GPS not available");
            Criteria myCriteria = new Criteria();
            myProvider = myLocationManager.getBestProvider(myCriteria,true);
            myFirstLocation = myLocationManager.getLastKnownLocation(myProvider);
        }
        else {
            try {
                /*Geocoder object is created to fetch the address of the first location*/
                myFirstLatLng = new LatLng(myFirstLocation.getLatitude(), myFirstLocation.getLongitude());
                myGeocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                Address addresses = myGeocoder.getFromLocation(myFirstLocation.getLatitude(), myFirstLocation.getLongitude(), 1).get(0);
                myFirstAddress = addresses.getAddressLine(0) +", "+ addresses.getAddressLine(1) +" "+ addresses.getAddressLine(2);

                /*Placing the first marker*/
                MarkerOptions myFirstMarkerOptions = new MarkerOptions().position(myFirstLatLng).title(myFirstAddress);
                map.addMarker(myFirstMarkerOptions).showInfoWindow();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myFirstLatLng, 17));
                map.setMyLocationEnabled(true);
            }
            catch (IOException ioException) {
                Log.i("EXCEPTION","Location not available");
            }

            myLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    map.setMyLocationEnabled(true);
                    if (location != null) {
                        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        try {
                            Address addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                            myAddress = addresses.getAddressLine(0) +", "+ addresses.getAddressLine(1) +" "+ addresses.getAddressLine(2);
                            Log.i("LOCATION", myAddress+" LATITUDE: "+location.getLatitude()+" LONGITUDE: "+location.getLongitude()); //Print updated address in logcat during location change
                        }
                        catch (IOException ioException) {
                            Log.i("LOCATION", "Unable to get address! :(");
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if(myFirstLocation!=null && myProvider==null) {
                // Adding listener to location manager using GPS provider
                //myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIMEPERIOD, 0, myLocationListener);
                myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIMEPERIOD, 0, myLocationListener);
            }
            else
            {
                //Adding listener to location manager using getBestProvider()
                myLocationManager.requestLocationUpdates(myProvider, TIMEPERIOD, 0, myLocationListener);
            }
        }
        return view;
    }

    @Override
    public  void onPause()
    {
        if(myLocationManager!=null && myLocationListener!=null)
        {
            myLocationManager.removeUpdates(myLocationListener);
            super.onPause();
            myMapView.onPause();
        }
        super.onPause();
    }

/*    @Override
    public  void onDestroy()
    {
        myLocationManager.removeUpdates(myLocationListener);
        super.onDestroy();
        myMapView.onDestroy();
    }*/
}
