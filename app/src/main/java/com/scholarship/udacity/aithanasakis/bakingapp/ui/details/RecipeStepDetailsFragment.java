package com.scholarship.udacity.aithanasakis.bakingapp.ui.details;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 3piCerberus on 03/05/2018.
 */

public class RecipeStepDetailsFragment extends Fragment {
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    Unbinder unbinder;
    private RecipeDetailsActivity parent;
    private Recipe selectedRecipe;
    private Step selectedStep;
    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (RecipeDetailsActivity) this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewgroup = inflater.inflate(R.layout.step_details_fragment, container, false);

        unbinder = ButterKnife.bind(this, viewgroup);
        return viewgroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedRecipe = parent.getSelectedRecipe();
        selectedStep = selectedRecipe.getSteps().get(parent.getSelectedStepNumber());
        if (selectedStep.getVideoURL()!=null){
            initExoPlayer(Uri.parse(selectedStep.getVideoURL()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseExoPlayer();
        unbinder.unbind();
    }

    private void initExoPlayer(Uri video){
        if (exoPlayer == null){
            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            exoPlayerView.setPlayer(exoPlayer);
            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(video);
            // Prepare the player with the source.
            exoPlayer.prepare(videoSource);

            // onRestore
            if (mCurrentPosition != 0)
                exoPlayer.seekTo(mCurrentPosition);

            exoPlayer.setPlayWhenReady(mPlayWhenReady);
            exoPlayerView.setVisibility(View.VISIBLE);

        }
    }
    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
