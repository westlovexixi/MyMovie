package com.example.mymovie.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.MovieView;
import com.example.mymovie.R;
import com.example.mymovie.model.ListResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView post;
        public TextView id;
        public TextView title;
        public TextView releaseDate;



        public ViewHolder(View itemView){
            super(itemView);
            post = itemView.findViewById(R.id.iv_post);
            id = itemView.findViewById(R.id.movie_id);
            title = itemView.findViewById(R.id.movie_title);
            releaseDate = itemView.findViewById(R.id.release_date);
        }
    }

    private List<ListResult> listResults;

    public int getItemCount(){
        return listResults.size();
    }


    public RecyclerViewAdapter(List<ListResult> movie){
        listResults = movie;
    }

    public void addMovie(List<ListResult> movie){
        listResults = movie;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder viewHolder, int position){
        final ListResult movie = listResults.get(position);
        ImageView imageView = viewHolder.post;
        Picasso.get()
                .load(movie.getPost_path())
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(imageView);
        TextView tvID = viewHolder.id;
        tvID.setText(movie.getId());
        TextView tvTitle = viewHolder.title;
        tvTitle.setText(movie.getTitle());
        tvTitle.setTextColor(Color.rgb(255,140,0));
        tvTitle.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        TextView tvRelease = viewHolder.releaseDate;
        tvRelease.setText(movie.getRelease_date());
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieView.class);
                intent.putExtra("movieid", movie.getId());
                intent.putExtra("identifier", "88");
                v.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });
    }
}
