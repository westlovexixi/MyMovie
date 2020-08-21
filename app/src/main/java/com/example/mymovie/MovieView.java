package com.example.mymovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovie.entity.Movie;
import com.example.mymovie.fragment.Memoir;
import com.example.mymovie.fragment.WatchList;
import com.example.mymovie.networkconnection.NetworkConnection;
import com.example.mymovie.viewmodel.MovieViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieView extends AppCompatActivity {
    private String id;
    private int identifier = 0;
    private ImageView imageView;
    private TextView tvTitle, tvDirector, tvCast, tvRelease, tvCountry, tvSummary, tvgenre;
    private RatingBar ratingBar;
    private Button btBack, btWatch, btMemoir;
    private NetworkConnection networkConnection = new NetworkConnection();
    private JSONObject jsonObject, countryJsonObject, genreJsonObject, castJsonObject, crewJsonObject;
    private JSONArray countryJsonArray, genreJsonArray, castJsonArray, crewJsonArray;
    final MovieSearch movieSearch = new MovieSearch();
    private Memoir memoirFragment = new Memoir();
    final Credits credits = new Credits();
    private WatchList myFragment = new WatchList();
    MovieViewModel movieViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view);


        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initalizeVars(getApplication());
        imageView = findViewById(R.id.iv_post);
        tvTitle = findViewById(R.id.title_movieview);
        tvDirector = findViewById(R.id.director_movieview);
        tvCast = findViewById(R.id.cast_movieview);
        tvRelease = findViewById(R.id.release_date_movieview);
        tvCountry = findViewById(R.id.country_movieview);
        tvSummary = findViewById(R.id.summary_movieview);
        tvgenre = findViewById(R.id.genre_movieview);
        ratingBar = findViewById(R.id.rating_movieview);
        btBack = findViewById(R.id.back_movieview);
        btWatch = findViewById(R.id.addWatch);
        btMemoir = findViewById(R.id.addMemoir);


        Intent intent = getIntent();
        id = intent.getStringExtra("movieid");
        identifier = Integer.parseInt(intent.getStringExtra("identifier"));
        movieSearch.execute(id);


        if (identifier == 1){
            btWatch.setEnabled(false);
        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieView.this.finish();
            }
        });

        btWatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String title = tvTitle.getText().toString();
                String releaseDate = tvRelease.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                String addDate = simpleDateFormat.format(date);
                Movie movie = new Movie(id, title, releaseDate,addDate);
                movieViewModel.insert(movie);
                Toast.makeText(MovieView.this,"Add successful", Toast.LENGTH_LONG).show();
                return;
            }
        });

        btMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = tvTitle.getText().toString();
                String releaseDate = tvRelease.getText().toString();
                Intent intent = new Intent(MovieView.this, AddToMemoir.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("release", releaseDate);
                startActivity(intent);
            }
        });
    }

    private class MovieSearch extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params){
            String keyword = params[0];
            return networkConnection.MovieSearch(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            jsonObject = returnMessage;
            //post
            String post_path = "";
            String url = "https://image.tmdb.org/t/p/original";
            try {
                post_path = jsonObject.get("poster_path").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String final_path = url + post_path;
            Picasso.get()
                    .load(final_path)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 296)
                    .centerInside()
                    .into(imageView);
            //title
            String title = "";
            try {
                title = jsonObject.get("title").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvTitle.setText(title);
            //genre
            String genre = "";
            String genreTemp = "";
            try {
                genreJsonArray = jsonObject.getJSONArray("genres");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int i ;
            for (i = 0; i< genreJsonArray.length(); i++){
                try {
                    genreJsonObject = genreJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    genreTemp = genreJsonObject.get("name").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (i == genreJsonArray.length() - 1){
                    genre = genre + genreTemp;
                } else if (i != genreJsonArray.length() - 1 ){
                    genre = genre + genreTemp + "/";
                }
            }
            tvgenre.setText(genre);
            //release_date
            String release_date = "";
            try {
                release_date = jsonObject.get("release_date").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvRelease.setText(release_date);
            //country
            String country = "";
            String countryTemp = "";
            try {
                countryJsonArray = jsonObject.getJSONArray("production_countries");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i = 0;
            for (i = 0; i < countryJsonArray.length(); i++) {
                try {
                    countryJsonObject = countryJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    countryTemp = countryJsonObject.get("name").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                country = country + countryTemp + "/";
            }
            country = country.substring(0, country.length() - 1);
            tvCountry.setText(country);
            //summary
            String summary = "";
            try {
                summary = jsonObject.get("overview").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvSummary.setText(summary);
            //rating
            String rate = "";
            int rateR = 0;
            try {
                rate = jsonObject.get("vote_average").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Number num = Float.parseFloat(rate) * 10;
            rateR = num.intValue();
            if (rateR >= 0 && rateR <= 9){
                ratingBar.setProgress(0);
            }
            if (rateR >= 10 && rateR <= 18){
                ratingBar.setProgress(1);
            }
            if (rateR >= 19 && rateR <= 27){
                ratingBar.setProgress(2);
            }
            if (rateR >= 28 && rateR <= 36){
                ratingBar.setProgress(3);
            }
            if (rateR >= 37 && rateR <= 45){
                ratingBar.setProgress(4);
            }
            if (rateR >= 46 && rateR <= 54){
                ratingBar.setProgress(5);
            }
            if (rateR >= 55 && rateR <= 63){
                ratingBar.setProgress(6);
            }
            if (rateR >= 64 && rateR <= 72){
                ratingBar.setProgress(7);
            }
            if (rateR >= 72 && rateR <= 81){
                ratingBar.setProgress(8);
            }
            if (rateR >= 82 && rateR <= 90){
                ratingBar.setProgress(9);
            }
            if (rateR >= 91 && rateR <= 100){
                ratingBar.setProgress(10);
            }
            credits.execute(id);
        }
    }

    private class Credits extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params){
            String keyword = params[0];
            return networkConnection.MovieCredits(keyword);
        }
        @Override
        protected void onPostExecute(JSONObject returnMessage){
            jsonObject = returnMessage;
            try {
                castJsonArray = jsonObject.getJSONArray("cast");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                crewJsonArray = jsonObject.getJSONArray("crew");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //top3 cast
            String cast = "", tempCast = "";
            int i;
            for (i = 0; i < 3; i++){
                try {
                    castJsonObject = castJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    tempCast = castJsonObject.get("name").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cast = cast + tempCast + "/";
                if (i == 2){
                    cast = cast.substring(0, cast.length()-1);
                }
                tempCast = "";
            }
            tvCast.setText(cast);
            //director
            String director = "", job = "";
            i = 0;
            while (!job.equals("Director")){
                try {
                    crewJsonObject = crewJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    job = crewJsonObject.get("job").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    director = crewJsonObject.get("name").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i++;
            }
            tvDirector.setText(director);
        }
    }


}
