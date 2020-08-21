package com.example.mymovie.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.R;
import com.example.mymovie.UserInfo;
import com.example.mymovie.adapter.MemoirAdapter;
import com.example.mymovie.adapter.RecyclerViewAdapterWatchlist;
import com.example.mymovie.model.MemoirList;
import com.example.mymovie.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Memoir extends Fragment {

    private String pid, post_path, title, watchDate, releaseDate, rate, comment, code;
    private List<MemoirList> memoirLists = new ArrayList<>();
    private MemoirAdapter adapter;
    private NetworkConnection networkConnection = new NetworkConnection();

    final GetUserMemoir getUserMemoir = new GetUserMemoir();

    public Memoir(){

    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_memoir, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_movie_memoir);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MemoirAdapter(memoirLists);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        pid = UserInfo.getPreID();
        getUserMemoir.execute(pid);

        return view;

    }

    private class GetUserMemoir extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params){
            String keyword = params[0];
            return networkConnection.getUserMemoir(keyword);
        }
        @Override
        protected void onPostExecute(JSONArray returnMessage){
            JSONArray returnJA = returnMessage;
            JSONObject tempJB = new JSONObject();
            int a = 0;
            for (a = 0; a < returnJA.length(); a++){
                try {
                    tempJB = returnJA.getJSONObject(a);
                    title = tempJB.getString("moviename");
                    watchDate = tempJB.getString("watchdate").substring(0,10);
                    code = tempJB.getJSONObject("cid").getString("cpostcode");
                    comment = tempJB.getString("comment");
                    rate = tempJB.getString("pstar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final GetOnlineInfo getOnlineInfo = new GetOnlineInfo();
                getOnlineInfo.execute(title);
                saveData(post_path, title, releaseDate, watchDate, code, comment ,rate);
            }
        }
    }

    private class GetOnlineInfo extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground (String...params){
            String keyword = params[0];
            return networkConnection.search(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            String tempPath;
            try {
                JSONObject returnJO = returnMessage.getJSONArray("results").getJSONObject(0);
                releaseDate = returnJO.getString("release_date");
                tempPath = returnJO.getString("poster_path");
                post_path = "https://image.tmdb.org/t/p/original" + tempPath;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData(String post_path, String title, String releaseDate, String watchDate, String code, String comment, String rate){
        MemoirList listResult = new MemoirList(post_path, title, releaseDate, watchDate, code, comment, rate);
        memoirLists.add(listResult);
        adapter.addMemoir(memoirLists);
    }
}
