package com.prem.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prem.android.popularmovies.Models.Movies;
import com.prem.android.popularmovies.R;
import com.prem.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Prem on 19-07-2017.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movies> mMovieList;
    Context mContext;

    public MovieAdapter(Context context){
        this.mContext = context;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.image_in_gridview);
        }

    }

    // Called when RecyclerView instantiates a new ViewHolder instance.
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_raw;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    //Called when RecyclerView wants to populate the view with data from our model,so that user can see it
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movies currentMovie = mMovieList.get(position);
        String posterPathReturned = currentMovie.getPoster();


        String picassoImageUrl = NetworkUtils.buildPicassoUrl(posterPathReturned);
        Picasso.with(holder.mMovieImageView.getContext()).load(picassoImageUrl).into(holder.mMovieImageView);
    }

    //no of items in our data source
    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        } else {
            return mMovieList.size();
        }
    }

    public void setMovieList(ArrayList<Movies> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }
}
