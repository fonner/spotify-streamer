package com.hitechren.spotifystreamer;

import android.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitechren.spotifystreamer.data.ArtistAdapter;
import com.hitechren.spotifystreamer.data.DisplayArtist;

import java.util.ArrayList;


import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import kaaes.spotify.webapi.android.models.ArtistsPager;


public class SearchFragment extends Fragment {

    ArtistAdapter mArtistAdapter;
    String mCurrentSearchItem;
    ArrayList<DisplayArtist> mArtistList;
    private static final String LOG_TAG = SearchFragment.class.getSimpleName();


    public SearchFragment() {
        // Required empty public constructor
    }

    public void executeSearch(String probe){
        FetchArtistsTask artistsTask = new FetchArtistsTask();
        mCurrentSearchItem = probe;
        artistsTask.execute(probe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("searchBox",
                ((EditText)getActivity().findViewById(R.id.edittext_search_artist)).getText().toString());
        outState.putParcelableArrayList("currentResults", mArtistAdapter.getItems());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchBox = (EditText)rootView.findViewById(R.id.edittext_search_artist);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = true;
                if(Utilities.isNetworkAvailable(getActivity())){
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                            || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                        executeSearch(v.getText().toString());
                        handled = false;
                    }
                } else {
                    Toast.makeText(getActivity(),
                            R.string.notification_no_network,
                            Toast.LENGTH_SHORT)
                            .show();
                }

                return handled;
            }
        });

        if(savedInstanceState != null){
            ((EditText) rootView.findViewById(R.id.edittext_search_artist))
                    .setText(savedInstanceState.getString("searchBox"));

            mArtistList = savedInstanceState.getParcelableArrayList("currentResults");
            mArtistAdapter =
                    new ArtistAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_artist, // The name of the layout ID.
                            mArtistList);

        } else {
            mArtistAdapter =
                    new ArtistAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_artist, // The name of the layout ID.
                            new ArrayList<DisplayArtist>());
        }


        GridView GridView = (GridView) rootView.findViewById(R.id.gridview_artists);
        GridView.setAdapter(mArtistAdapter);

        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Utilities.isNetworkAvailable(getActivity())){
                    DisplayArtist artist = mArtistAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), PlaylistActivity.class)
                            .putExtra("displayArtist", artist);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(),
                            R.string.notification_no_network,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return rootView;
    }
    public class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager>{

        @Override
        protected ArtistsPager doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            return spotify.searchArtists(params[0]);
        }

        @Override
        protected void onPostExecute(ArtistsPager result) {
            if(result.artists.items.size() != 0){
                mArtistAdapter.clear();
                for(int i =0; i < result.artists.items.size(); i++){
                    mArtistAdapter.add(new DisplayArtist(result.artists.items.get(i)));
                }
                mArtistList = mArtistAdapter.getItems();
            } else {
                Toast.makeText(getActivity(), R.string.notification_artist_no_results,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
