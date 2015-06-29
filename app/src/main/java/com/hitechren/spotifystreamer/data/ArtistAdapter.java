package com.hitechren.spotifystreamer.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by benjamin.fonner on 6/29/2015.
 */
public class ArtistAdapter  extends ArrayAdapter<Artist>{
    public ArtistAdapter(Context context, int resource, Artist[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
