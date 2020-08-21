package com.example.mymovie.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.MovieView;
import com.example.mymovie.R;
import com.example.mymovie.entity.Movie;
import com.example.mymovie.viewmodel.MovieViewModel;

import java.util.List;

public class RecyclerViewAdapterWatchlist extends RecyclerView.Adapter <RecyclerViewAdapterWatchlist.ViewHolder> {

    private MovieViewModel movieViewModel;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView id;
        public TextView title;
        public TextView releaseDate;
        public TextView addDate;
        public TextView viewMore;
        public ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);

            id = itemView.findViewById(R.id.id_watchlist);
            title = itemView.findViewById(R.id.title_watchlist);
            releaseDate = itemView.findViewById(R.id.release_date_watchlist);
            addDate = itemView.findViewById(R.id.add_date_watchlist);
            viewMore = itemView.findViewById(R.id.view_watchlist);
            imageView = itemView.findViewById(R.id.iv_item_delete);
        }
    }

    private List<Movie> movies;

    @Override
    public int getItemCount(){
        if(movies == null){
            return 0;
        }
        return movies.size();
    }

    public RecyclerViewAdapterWatchlist(List<Movie> movies, Fragment fragment, Application application){
        this.movies = movies;
        movieViewModel = new ViewModelProvider(fragment).get(MovieViewModel.class);
        movieViewModel.initalizeVars(application);
    }

    public void addWatch(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View unitsView = inflater.inflate(R.layout.rv_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(unitsView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerViewAdapterWatchlist.ViewHolder viewHolder, int position){
        final Movie movie = movies.get(position);
        TextView id = viewHolder.id;
        id.setText(movie.getMovieID());
        TextView tvTitle = viewHolder.title;
        tvTitle.setText(movie.getMovieTitle());
        TextView tvReleaseDate = viewHolder.releaseDate;
        tvReleaseDate.setText(movie.getReleaseDate());
        TextView tvAddDate = viewHolder.addDate;
        tvAddDate.setText(movie.getAddDate());
        ImageView imageView = viewHolder.imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.remove(movie);
                movieViewModel.delete(movie);
                notifyDataSetChanged();
            }
        });
        TextView viewMore = viewHolder.viewMore;
        viewMore.setTextColor(Color.rgb(255,140,0));
        viewMore.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieView.class);
                intent.putExtra("movieid", movie.getMovieID());
                intent.putExtra("identifier", "1");
                v.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });
    }
}
