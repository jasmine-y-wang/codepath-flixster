package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import org.parceler.Parcels;


public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MovieDetailsActivity";

    // movie to display
    Movie movie;

    private ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // resolve view objects
        setContentView(binding.getRoot());

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // set title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        // vote average is 0-10 so convert to 0-5
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage / 2.0f);

        Glide.with(this).load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .centerCrop()
                .transform(new RoundedCorners(50))
                .into(binding.ivBackdrop);

        binding.ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

    }

    public void openNewActivity() {
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        // serialize the movie using parceler, use simple name as key
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        // show activity
        startActivity(intent);
    }
}