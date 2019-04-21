package app.com.shubham.moviemanager.models;

import java.io.Serializable;

public class Movies implements Serializable {


    private String id;
    private String title;
    private String overview;
    private float  voteAvarage;
    private float  voteCount;
    private String posterPath;
    private String backdropPath;



    public Movies(String id, String title, String overview, float voteAvarage, float voteCount, String posterPath, String backdropPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.voteAvarage = voteAvarage;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAvarage() {
        return voteAvarage;
    }

    public void setVoteAvarage(float voteAvarage) {
        this.voteAvarage = voteAvarage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(float voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return String.format("http://image.tmdb.org/t/p/w342%s",posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

}
