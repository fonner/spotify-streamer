package com.hitechren.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaylistActivityFragment extends Fragment {

    Artist mArtist;

    public PlaylistActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("artistBundle")) {
            Bundle bundle = intent.getBundleExtra("artistBundle");
            ((TextView) rootView.findViewById(R.id.textview_artist)).setText(bundle.getString("artistName"));
            Picasso.with(getActivity()).load(bundle.getString("artistImg"))
                    .into((ImageView) rootView.findViewById(R.id.imageview_artist));
        }


        return rootView;
    }
}
