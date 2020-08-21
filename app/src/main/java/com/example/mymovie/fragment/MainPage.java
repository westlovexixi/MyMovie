package com.example.mymovie.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mymovie.HomePage;
import com.example.mymovie.MovieView;
import com.example.mymovie.R;
import com.example.mymovie.UserInfo;
import com.example.mymovie.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainPage extends Fragment {

    private String post_id ,pid;
    private ImageView imageView;
    private NetworkConnection networkConnection = new NetworkConnection();
    final HomeImage homeImage = new HomeImage();
    final GetTopFive getTopFive = new GetTopFive();
    List<HashMap<String, String>> unitListArray;
    SimpleAdapter myListAdapter;
    ListView unitList;

    HashMap<String,String> map = new HashMap<String,String>();
    String[] colHEAD = new String[] {"ID", "Title", "ReleaseDate", "Rate"};
    int[] dataCell = new int[] {R.id.id_homepage,R.id.title_homepage,R.id.release_date_homepage, R.id.rate_homepage};

    public MainPage() { // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        imageView = view.findViewById(R.id.image_home);

        unitList = view.findViewById(R.id.listView_homepage);
        unitListArray = new ArrayList<HashMap<String, String>>();



        homeImage.execute();

        pid = UserInfo.getPreID();

        getTopFive.execute(pid);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieView.class);
                intent.putExtra("movieid", post_id);
                intent.putExtra("identifier", "66");
                startActivity(intent);
            }
        });

        return view;
    }

    private class HomeImage extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            return networkConnection.getHomeImage();
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage) {
            JSONObject postJsonObject = returnMessage;
            String post_path = "";
            String url = "https://image.tmdb.org/t/p/original";
            try {
                post_path = postJsonObject.get("poster_path").toString();
                post_id = postJsonObject.get("id").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String final_path = url + post_path;
            Picasso.get()
                    .load(final_path)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(300, 444)
                    .centerInside()
                    .into(imageView);
        }
    }


    private class GetTopFive extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground (String...params){
            String keyword = params[0];
            return networkConnection.getTopMovies(keyword);
        }
        @Override
        protected void onPostExecute (JSONArray returnMessage){
            JSONArray returnJsonArray = returnMessage;
            String movieName = "", rate = "", releaseDate = "";
            int a = 0;
            for (a = 0; a < returnJsonArray.length(); a++){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = returnJsonArray.getJSONObject(a);
                    movieName = jsonObject.getString("Moviename");
                    releaseDate = jsonObject.getString("ReleaseYear");
                    rate = jsonObject.getString("Rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String no = String.valueOf(a + 1);
                String month = releaseDate.substring(0,7);
                String year = releaseDate.substring(releaseDate.length()-4);
                releaseDate = year + " " + month;
                map = new HashMap<>();
                map.put("ID", no);
                map.put("Title", movieName);
                map.put("ReleaseDate", releaseDate);
                map.put("Rate", rate);
                unitListArray.add(map);
            }
            myListAdapter = new SimpleAdapter(getContext(),unitListArray,R.layout.list_view2,colHEAD,dataCell);
            unitList.setAdapter(myListAdapter);
        }
    }
}
