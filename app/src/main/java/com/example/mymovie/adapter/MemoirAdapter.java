package com.example.mymovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.R;
import com.example.mymovie.model.MemoirList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoirAdapter extends RecyclerView.Adapter <MemoirAdapter.MemoirViewHolder> {

    public class  MemoirViewHolder extends RecyclerView.ViewHolder {
        public ImageView post;
        public TextView title, releaseDate, watchDate, code, comment;
        public RatingBar ratingBar;

        public MemoirViewHolder(View itemView){
            super(itemView);
            post = itemView.findViewById(R.id.image_movie_memoir);
            title = itemView.findViewById(R.id.title_movie_memoir);
            releaseDate = itemView.findViewById(R.id.release_movie_memoir);
            watchDate = itemView.findViewById(R.id.watch_movie_memoir);
            code = itemView.findViewById(R.id.code_movie_memoir);
            comment = itemView.findViewById(R.id.comment_movie_memoir);
            ratingBar = itemView.findViewById(R.id.rating_movie_memoir);
        }
    }

    private List<MemoirList> memoirLists;

    public int getItemCount(){return memoirLists.size();}

    public MemoirAdapter(List<MemoirList> memoir){
        memoirLists = memoir;
    }

    public void addMemoir(List<MemoirList> memoir){
        memoirLists = memoir;
        notifyDataSetChanged();
    }

    public MemoirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View memoirView = inflater.inflate(R.layout.list_view3, parent, false);
        MemoirViewHolder viewHolder = new MemoirViewHolder(memoirView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull MemoirViewHolder viewHolder, int position){
        final MemoirList memoirList = memoirLists.get(position);
        ImageView imageView = viewHolder.post;
        Picasso.get()
                .load(memoirList.getPost_path())
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(imageView);
        TextView tvTitle = viewHolder.title;
        tvTitle.setText(memoirList.getTitle());
        TextView tvRelease = viewHolder.releaseDate;
        tvRelease.setText(memoirList.getReleaseDate());
        TextView tvWatchDate = viewHolder.watchDate;
        tvWatchDate.setText(memoirList.getWatchDate());
        TextView tvCode = viewHolder.code;
        tvCode.setText(memoirList.getCode());
        TextView tvComment = viewHolder.comment;
        tvComment.setText(memoirList.getComment());
        RatingBar ratingBar = viewHolder.ratingBar;
        float rateR = Float.valueOf(memoirList.getRate());
        ratingBar.setRating(rateR);
    }

}
