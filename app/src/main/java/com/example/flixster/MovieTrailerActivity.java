package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String TAG = "MovieTrailerActivity";
    private ActivityMovieTrailerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing trailer for '%s'", movie.getTitle()));

        // temporary test video id -- TODO replace with movie trailer video id

        // get videoID from API
        AsyncHttpClient client = new AsyncHttpClient();
        String movie_url = String.format("https://api.themoviedb.org/3/movie/%d/videos?api_key=%s",
                movie.getId(), getString(R.string.movie_api_key));
        client.get(movie_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    String videoId = results.getJSONObject(0).getString("key");
                    Log.i(TAG, "Video ID: " + videoId);
                    initializeYoutube(videoId);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure: " + response);
            }
        });
    }

    private void initializeYoutube(String videoId) {
        // resolve player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) binding.player;

        // initialize with API key
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}