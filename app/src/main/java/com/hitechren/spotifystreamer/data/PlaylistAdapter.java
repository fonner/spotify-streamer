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

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class PlaylistAdapter extends ArrayAdapter<Track> {

    Context context;
    int layoutResourceId;
    ArrayList<Track> data = null;

    public PlaylistAdapter(Context context, int resource, ArrayList<Track> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrackHolder placeHolder = null;

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            placeHolder = new TrackHolder();
            placeHolder.imageview_album = (ImageView)convertView.findViewById(R.id.imageview_album);
            placeHolder.textview_title = (TextView)convertView.findViewById(R.id.textview_title);
            placeHolder.textview_album = (TextView)convertView.findViewById(R.id.textview_album);
            convertView.setTag(placeHolder);

        } else {
            placeHolder = (TrackHolder)convertView.getTag();
        }

        Track rowTrack = data.get(position);
        //If the album has images, find the smallest image, no smaller than 100px
        if(rowTrack.album.images.size() != 0){
            int sHeight = 0; //Smallest known height
            int imageID = 0;
            for(Image img : rowTrack.album.images){
                if (sHeight == 0 || (img.height < sHeight && img.height >100)){
                    sHeight = img.height;
                }
            }
            Picasso.with(context).load(rowTrack.album.images.get(imageID).url)
                    .into(placeHolder.imageview_album);
            placeHolder.imageview_album.setBackgroundColor(0x00000000);
        } else {
            placeHolder.imageview_album.setBackgroundColor(0xFFFF0000);
            placeHolder.imageview_album.setImageResource(android.R.color.transparent);
        }
        placeHolder.textview_title.setText(rowTrack.name);
        placeHolder.textview_album.setText(rowTrack.album.name);

        return convertView;
    }

    static class TrackHolder {
        ImageView imageview_album;
        TextView textview_title;
        TextView textview_album;
    }
}
