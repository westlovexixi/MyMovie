package com.example.mymovie.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mymovie.R;
import com.example.mymovie.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends Fragment implements OnMapReadyCallback {

    private NetworkConnection networkConnection = new NetworkConnection();
    private JSONObject jsonObject;
    private String stNo, stName, state, postcode, realAddress;
    private LatLng userLocation, cinemaLocation;
    final UserAddress userAddress = new UserAddress();
    final PutUser putUser = new PutUser();
    final GetCinema getCinema = new GetCinema();
    final PutCinema putCinema = new PutCinema();
    private GoogleMap googleMap;


    public Map(){

    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.map, container, false);
        Bundle bundle = getArguments();
        String pid = bundle.getString("id");
        int id = Integer.parseInt(pid);
        String test = String.valueOf(id);
        userAddress.execute(id);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private class UserAddress extends AsyncTask<Integer, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(Integer... params){
            int keyword = params[0];
            return networkConnection.getAddress(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            jsonObject = returnMessage;
            try {
                stNo = jsonObject.get("no").toString();
                stName = jsonObject.get("name").toString();
                state = jsonObject.get("state").toString();
                postcode = jsonObject.get("code").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            realAddress = stNo + " " +stName + ", " + state + " " + postcode + ", " + "Australia";
            putUser.execute(realAddress);
        }
    }

    private class PutUser extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params){
            String keyword = params[0];
            return networkConnection.getUserLocation(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            JSONObject location = returnMessage;
            String latitudeString= "";
            String longitudeString = "";
            try {
                latitudeString = location.get("lat").toString();
                longitudeString = location.get("lng").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);
            userLocation = new LatLng(latitude, longitude);
            getCinema.execute();

        }
    }

    private class GetCinema extends AsyncTask<String, Void, JSONArray>{
        @Override
        protected JSONArray doInBackground(String... params){
            return networkConnection.getCinema();
        }
        @Override
        protected void onPostExecute(JSONArray returnMessage){
            JSONArray jsonArray = returnMessage;
            JSONObject cinemaObject = new JSONObject();
            String name = "", code = "";
            java.util.Map<Integer, JSONObject> map;
            List<java.util.Map<Integer, JSONObject>> list = new ArrayList<>();
            int i = 0;
            for (i = 0; i <jsonArray.length(); i++){
                try {
                    cinemaObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map = new HashMap<>();
                map.put(i, cinemaObject);
                list.add(map);
            }
            putCinema.execute(list);

        }
    }

    private class PutCinema extends AsyncTask<List, Void, JSONArray>{
        @Override
        protected JSONArray doInBackground(List... params){
            List keyword = params[0];
            return networkConnection.getCinemaLocation(keyword);
        }
        @Override
        protected void onPostExecute(JSONArray returnMessage){
            JSONArray jsonArray = returnMessage;
            JSONObject temp = new JSONObject();
            String name = "";
            JSONObject locationJsonObject = new JSONObject();
            String latitudeString= "";
            String longitudeString = "";
            int i = 0;
            for (i = 0; i < jsonArray.length(); i++){
                try {
                    temp = jsonArray.getJSONObject(i);
                    name = temp.get("name").toString();
                    locationJsonObject = temp.getJSONObject("location");
                    latitudeString = locationJsonObject.get("lat").toString();
                    longitudeString = locationJsonObject.get("lng").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                cinemaLocation = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(cinemaLocation).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }
            googleMap.addMarker(new MarkerOptions().position(userLocation).title("My location"));
            float zoomLevel = (float) 10.0;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel));
        }
    }

}
