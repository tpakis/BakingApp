package com.scholarship.udacity.aithanasakis.bakingapp.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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

import com.scholarship.udacity.aithanasakis.bakingapp.viewmodel.DetailsActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by 3piCerberus on 03/05/2018.
 */

public class RecipeStepDetailsFragment extends Fragment {
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    Unbinder unbinder;
    @BindView(R.id.step_number_textView)
    TextView stepNumberTextView;
    @BindView(R.id.description_textView)
    TextView descriptionTextView;
    @BindView(R.id.btn_prev)
    Button btnPrev;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.thumbnail_imageView)
    ImageView thumbnail;
    private DetailsActivityViewModel viewModel;
    private RecipeDetailsActivity parent;
    private Recipe selectedRecipe;
    private int selectedStepNumber;
    private long playerPosition = 0;
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
        playerPosition = viewModel.getPlayerPosition();
        mPlayWhenReady = viewModel.getPlayWhenReady();
        viewModel.getSelectedStepNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer selectedStep) {
                selectedStepNumber = selectedStep;
                setupUI(true);
                viewModel.setFragmentStepDetailsVisible(true);
            }
        });
        setupUI(false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(parent).get(DetailsActivityViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null && exoPlayer.getPlaybackState() != Player.STATE_ENDED) {
            viewModel.setPlayerPosition(exoPlayer.getCurrentPosition());
            viewModel.setPlayWhenReady(exoPlayer.getPlayWhenReady());
        } else {
            viewModel.setPlayerPosition(0L);
            viewModel.setPlayWhenReady(true);
        }
        releaseExoPlayer();
    }

    private void setupUI(boolean newVideoSource) {
        Step selectedStep = selectedRecipe.getSteps().get(selectedStepNumber);
        if (selectedStep.getVideoURL() != null && !selectedStep.getVideoURL().equals("")) {
            initExoPlayer(Uri.parse(selectedStep.getVideoURL()), newVideoSource);
        } else {
            exoPlayerView.setVisibility(View.GONE);
        }
        if ((selectedStep.getThumbnailURL() != null) && !selectedStep.getThumbnailURL().equals("")) {
            thumbnail.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .dontTransform()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(parent).load(selectedStep.getThumbnailURL()).apply(options)
                    .into(thumbnail);
        } else {
            thumbnail.setVisibility(View.GONE);
        }
        descriptionTextView.setText(selectedStep.getDescription());
        stepNumberTextView.setText(getString(R.string.step_number) + String.valueOf(selectedStepNumber + 1));
    }

    private void initExoPlayer(Uri video, boolean newVideoSource) {
        // Create a default TrackSelector
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(video);
        if (exoPlayer == null) {
            // Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            // Bind the player to the view.
            exoPlayerView.setPlayer(exoPlayer);
        } else {
            exoPlayer.stop();
            if (newVideoSource) {
                exoPlayer.seekTo(0L);
                playerPosition = 0;
            }
        }
        exoPlayer.prepare(videoSource);
        // onRestore
        if (playerPosition != 0)
            exoPlayer.seekTo(playerPosition);
        exoPlayer.setPlayWhenReady(mPlayWhenReady);
        exoPlayerView.setVisibility(View.VISIBLE);


    }

    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @OnClick({R.id.btn_prev, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_prev:
                parent.setSelectedStepNumber(selectedStepNumber - 1);
                break;
            case R.id.btn_next:
                parent.setSelectedStepNumber(selectedStepNumber + 1);
                break;
        }
    }


}
