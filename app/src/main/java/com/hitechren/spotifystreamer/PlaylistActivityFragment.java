package com.hitechren.spotifystreamer;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hitechren.spotifystreamer.data.DisplayArtist;
import com.hitechren.spotifystreamer.data.DisplayTrack;
import com.hitechren.spotifystreamer.data.PlaylistAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaylistActivityFragment extends Fragment {

    private PlaylistAdapter mPlaylistAdapter;
    private String mArtistID;
    private String mArtistName;
    private String mArtistImg;

    private final Map<String, Object> LOCALE = Collections.unmodifiableMap(
            new HashMap<String, Object>() {{put("country", Locale.getDefault().getCountry());
            }});

    public PlaylistActivityFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("currentResults", mPlaylistAdapter.getItems());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("displayArtist")) {
            DisplayArtist artist = intent.getParcelableExtra("displayArtist");
            this.mArtistName = artist.artistName;
            this.mArtistID = artist.artistId;
            this.mArtistImg = artist.artistImageUrl;


        }

        ((TextView) rootView.findViewById(R.id.textview_artist)).setText(this.mArtistName);
        Picasso.with(getActivity()).load(mArtistImg)
                .into((ImageView) rootView.findViewById(R.id.imageview_artist));
        if(savedInstanceState != null){

            ArrayList<DisplayTrack> tempArray;
            tempArray = savedInstanceState.getParcelableArrayList("currentResults");
            mPlaylistAdapter =
                    new PlaylistAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_track, // The name of the layout ID.
                            tempArray);

        } else {
            mPlaylistAdapter =
                    new PlaylistAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_track, // The name of the layout ID.
                            new ArrayList<DisplayTrack>());
            FetchTopTracksTask topTracksTask = new FetchTopTracksTask();
            topTracksTask.execute(this.mArtistID);
        }


        ListView listView = (ListView) rootView.findViewById(R.id.listview_tracks);
        listView.setAdapter(mPlaylistAdapter);




        //executeSearch("Elvis");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DisplayTrack track = mPlaylistAdapter.getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("mArtistName", artist.name);
//                bundle.putString("mArtistID", artist.id);
//                if (artist.images.size() != 0){
//                    bundle.putString("mArtistImg", artist.images.get(0).url);
//                }
//
//                Intent intent = new Intent(getActivity(), PlaylistActivity.class)
//                        .putExtra("artistBundle", bundle);
//                startActivity(intent);
            }
        });


        return rootView;
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            return spotify.getArtistTopTrack(params[0], LOCALE);
        }

        @Override
        protected void onPostExecute(Tracks result) {
            if(result != null){
                mPlaylistAdapter.clear();
                for(int i =0; i < result.tracks.size(); i++){
                    mPlaylistAdapter.add(new DisplayTrack(result.tracks.get(i)));
                }
            }
        }
    }
}
