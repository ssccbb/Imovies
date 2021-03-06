package com.sung.imovies.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sung on 2017/11/6.
 */

public class MoviesItem implements Serializable {
    public int position;
    public String vote_count;
    public String id;
    public String video;
    public String vote_average;
    public String title;
    public String popularity;
    public String poster_path;
    public String original_language;
    public String original_title;
    public int[] genre_ids;
    public String backdrop_path;
    public String adult;
    public String overview;
    public String release_date;

    public MoviesItem() {
    }

    public MoviesItem(JSONObject object) {
        vote_count = object.optString("vote_count");
        id = object.optString("id");
        video = object.optString("video");
        vote_average = object.optString("vote_average");
        title = object.optString("title");
        popularity = object.optString("popularity");
        poster_path = object.optString("poster_path");
        original_language = object.optString("original_language");
        original_title = object.optString("original_title");
        genre_ids = string2intarr(object.optString("genre_ids"));
        backdrop_path = object.optString("backdrop_path");
        adult = object.optString("adult");
        overview = object.optString("overview");
        release_date = object.optString("release_date");
    }

    private int[] string2intarr(String int_arr){
        // TODO: 2017/11/7 方法待补充 string 转 int[]
        int[] ids = {};
        return ids;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
