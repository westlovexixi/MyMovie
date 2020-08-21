package com.example.mymovie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mymovie.R;
import com.example.mymovie.adapter.RecyclerViewAdapterWatchlist;
import com.example.mymovie.database.MovieDatabase;
import com.example.mymovie.entity.Movie;
import com.example.mymovie.viewmodel.MovieViewModel;

import java.util.List;


public class WatchList extends Fragment {

    MovieDatabase db = null;
    MovieViewModel movieViewModel;
    private List<Movie> movies;
    private RecyclerViewAdapterWatchlist adapter;
    private ImageView delete;

    public WatchList(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.watchlist, container, false);
        delete = view.findViewById(R.id.iv_item_delete);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_watchlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapterWatchlist(movies, this, getActivity().getApplication());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.initalizeVars(getActivity().getApplication());
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.addWatch(movies);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}
