package io.github.pollinators.honeycomb.data.models;

import android.net.Uri;

/**
 * Created by ted on 11/29/14.
 */
public class Image extends AbstractModel {

    private Uri uri;
    private String title;
    private long timestamp;
    private long photographerUserId;
    private Float longitude;
    private Float latitude;

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Long getPhotographerUserId() {
        return photographerUserId;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPhotographerUserId(Long photographerUserId) {
        this.photographerUserId = photographerUserId;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}
