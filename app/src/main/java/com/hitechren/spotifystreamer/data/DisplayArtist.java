package com.hitechren.spotifystreamer.data;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;


public class DisplayArtist implements Parcelable {
    public static final Parcelable.Creator<DisplayArtist> CREATOR = new Parcelable.Creator<DisplayArtist>() {
        public DisplayArtist createFromParcel(Parcel source) {
            return new DisplayArtist(source);
        }
        public DisplayArtist[] newArray(int size) {
            return new DisplayArtist[size];
        }
    };

    public String artistName;
    public String artistImageUrl;
    public String artistId;

    public DisplayArtist (Artist artist){
        this.artistName = artist.name;
        this.artistId = artist.id;
        if (!artist.images.isEmpty()) {
            this.artistImageUrl = artist.images.get(0).url;
        } else {
            this.artistImageUrl = "";
        }
    }

    public DisplayArtist (Parcel in){
        this.artistName = in.readString();
        this.artistImageUrl = in.readString();
        this.artistId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artistName);
        dest.writeString(this.artistImageUrl);
        dest.writeString(this.artistId);
    }
}
