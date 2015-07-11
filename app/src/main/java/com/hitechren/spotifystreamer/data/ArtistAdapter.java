package com.hitechren.spotifystreamer.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitechren.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by benjamin.fonner on 6/29/2015.
 */
public class ArtistAdapter  extends ArrayAdapter<Artist>{
    Context context;
    int layoutResourceId;
    ArrayList<Artist> data = null;

    public ArtistAdapter(Context context, int resource, ArrayList<Artist> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArtistHolder placeHolder = null;

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            placeHolder = new ArtistHolder();
            placeHolder.imageview_artist = (ImageView)convertView.findViewById(R.id.imageview_artist);
            placeHolder.textview_artist = (TextView)convertView.findViewById(R.id.textview_artist);

            convertView.setTag(placeHolder);

        } else {
            placeHolder = (ArtistHolder)convertView.getTag();
        }

        Artist rowArtist = data.get(position);
        if(rowArtist.images.size() != 0){
            Picasso.with(context).load(rowArtist.images.get(0).url).error(R.drawable.silhouette_person)
                    .into(placeHolder.imageview_artist);
        } else {
            Picasso.with(context).load(R.drawable.silhouette_person)
                    .into(placeHolder.imageview_artist);
        }
        placeHolder.textview_artist.setText(rowArtist.name);

        return convertView;
    }

    static class ArtistHolder {
        ImageView imageview_artist;
        TextView textview_artist;
    }
}
