package com.example.mymovie.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.R;
import com.example.mymovie.adapter.RecyclerViewAdapter;
import com.example.mymovie.model.ListResult;
import com.example.mymovie.networkconnection.NetworkConnection;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MovieSearch extends Fragment {

    private EditText editText, idTv;
    private TextView tvTitle, tvReleaseDate;
    private Button btBack;
    private JSONObject resultsOne = null;
    private JSONArray jsonArray = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListResult> movies;
    private RecyclerViewAdapter adapter;


    NetworkConnection networkConnection = new NetworkConnection();
    final Search search = new Search();

    public MovieSearch(){

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.movie_search, container, false);
        editText = view.findViewById(R.id.et_moviesearch);
        idTv = view.findViewById(R.id.movie_id);
        btBack = view.findViewById(R.id.bt_moviesearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle = view.findViewById(R.id.title_movieview);
        tvReleaseDate = view.findViewById(R.id.release_date_movieview);
        movies = new ArrayList<ListResult>();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString().trim();
                if (!keyword.isEmpty()){
                    search.execute(keyword);
                }else {
                    editText.setText("Please enter a movie name!");
                    editText.setTextColor(Color.rgb(255, 0, 0));
                }
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(movies);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class Search extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params){
            String keyword = params[0];
            return networkConnection.search(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            try {
                jsonArray =  returnMessage.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!jsonArray.equals("[]")){
                int i = 0;
                for (i = 0; i < jsonArray.length(); i++)
                {
                    String id = null;
                    String title = null;
                    String releaseDate = null;
                    String url = "https://image.tmdb.org/t/p/original";
                    String post;
                    String final_path = null;
                    try {
                        resultsOne =  jsonArray.getJSONObject(i);
                        id = resultsOne.get("id").toString();
                        title = resultsOne.get("title").toString();
                        releaseDate = resultsOne.get("release_date").toString();
                        post = resultsOne.get("poster_path").toString();
                        final_path = url + post;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    saveData(id,final_path,title,releaseDate);
                }
            } else {
                editText.setText("Nothing found!");
                editText.setTextColor(Color.rgb(255, 0, 0));
            }
        }
    }

    private void saveData(String id, String post_path, String title, String releaseDate){
        ListResult listResult = new ListResult(id,post_path,title,releaseDate);
        movies.add(listResult);
        adapter.addMovie(movies);
    }
}


