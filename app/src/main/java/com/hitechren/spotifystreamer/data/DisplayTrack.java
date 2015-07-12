package com.hitechren.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;


public class DisplayTrack implements Parcelable{
    public static final Parcelable.Creator<DisplayTrack> CREATOR = new Parcelable.Creator<DisplayTrack>() {
        public DisplayTrack createFromParcel(Parcel source) {
            return new DisplayTrack(source);
        }
        public DisplayTrack[] newArray(int size) {
            return new DisplayTrack[size];
        }
    };

    public String trackName;
    public String trackId;
    public String albumImageUrl;
    public String albumName;


        public DisplayTrack (Track track){
            this.trackName = track.name;
            this.trackId = track.id;
            this.albumName = track.album.name;
            if (!track.album.images.isEmpty()) {
                this.albumImageUrl = track.album.images.get(0).url;
            } else {
                this.albumImageUrl = "";
            }
        }

        public DisplayTrack (Parcel in){
            this.trackName = in.readString();
            this.trackId = in.readString();
            this.albumName = in.readString();
            this.albumImageUrl = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.trackName);
            dest.writeString(this.trackId);
            dest.writeString(this.albumName);
            dest.writeString(this.albumImageUrl);

        }
}

