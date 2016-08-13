package com.majorprojectmodule.android.p2rsmusicstreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.majorprojectmodule.android.p2rsmusicstreamer.entity.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class NowPlayingFragment extends Fragment {

    MediaPlayer mediaPlayer;

    ImageView playPause;
    ImageView next;
    ImageView previous;
    TextView title;
    ImageView albumArt;

    Track selectedTrack;

    public NowPlayingFragment() {
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void togglePlayButton(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
        }
        else{
            mediaPlayer.start();
            playPause.setImageResource(R.drawable.ic_pause_black_36dp);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);

        title = (TextView) rootView.findViewById(R.id.nowplaying_title);

        albumArt = (ImageView) rootView.findViewById(R.id.album_art);

        playPause = (ImageView) rootView.findViewById(R.id.play_button);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayButton();
            }
        });

        Intent receivedIntent = getActivity().getIntent();

        if (receivedIntent != null && receivedIntent.hasExtra("selected_track")){

            selectedTrack = receivedIntent.getParcelableExtra("selected_track");

            title.setText(selectedTrack.getTitle());

            Picasso.with(getContext()).load(selectedTrack.getArtworkURL().replace("/300x300/","/174s/")).into(albumArt);

            Picasso.with(getContext()).load(R.drawable.ic_play_arrow_black_36dp).into(playPause);
        }
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.start();

                playPause.setImageResource(R.drawable.ic_pause_black_36dp);
            }
        });

        try {
            String url = selectedTrack.getStreamURL();

            Log.v("URL",selectedTrack.getStreamURL());

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setDataSource(url);

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
           e.printStackTrace();
        }




        return rootView;
    }

}
