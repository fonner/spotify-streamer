package com.hitechren.spotifystreamer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hitechren.spotifystreamer.data.ArtistAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

public class SearchFragment extends Fragment {

    ArrayAdapter<Artist> mArtistAdapter;
    String mCurrentSearchItem;
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
        outState.putString("currentSearch", mCurrentSearchItem);
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
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    executeSearch(v.getText().toString());
                    handled = false;
                }
                return handled;
            }
        });

        if(savedInstanceState != null){
            ((EditText) rootView.findViewById(R.id.edittext_search_artist))
                    .setText(savedInstanceState.getString("searchBox"));
            executeSearch(savedInstanceState.getString("currentSearch"));
        }

        mArtistAdapter =
            new ArtistAdapter(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_artist, // The name of the layout ID.
                    new ArrayList<Artist>());


        GridView GridView = (GridView) rootView.findViewById(R.id.gridview_artists);
        GridView.setAdapter(mArtistAdapter);

        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mArtistAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("artistName", artist.name);
                bundle.putString("artistID", artist.id);
                if (artist.images.size() != 0){

                    bundle.putString("artistImg", artist.images.get(0).url);
                }

                Intent intent = new Intent(getActivity(), PlaylistActivity.class)
                        .putExtra("artistBundle", bundle);
                startActivity(intent);
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
                    mArtistAdapter.add(result.artists.items.get(i));
                }
            } else {
                Toast toast = new Toast(getActivity());
                toast.makeText(getActivity(), "Search returned no results.\nPlease Try again.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
