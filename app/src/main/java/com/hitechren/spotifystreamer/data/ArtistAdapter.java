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

public class ArtistAdapter  extends ArrayAdapter<DisplayArtist>{
    Context context;
    int layoutResourceId;
    ArrayList<DisplayArtist> data = null;

    public ArtistAdapter(Context context, int resource, ArrayList<DisplayArtist> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }

    public ArrayList<DisplayArtist> getItems(){
        return this.data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArtistHolder placeHolder;

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

        DisplayArtist rowArtist = data.get(position);
        if(!rowArtist.artistImageUrl.equals("")){
            Picasso.with(context).load(rowArtist.artistImageUrl).error(R.drawable.silhouette_person)
                    .into(placeHolder.imageview_artist);
        } else {
            Picasso.with(context).load(R.drawable.silhouette_person)
                    .into(placeHolder.imageview_artist);
        }
        placeHolder.textview_artist.setText(rowArtist.artistName);

        return convertView;
    }

    static class ArtistHolder {
        ImageView imageview_artist;
        TextView textview_artist;
    }
}
