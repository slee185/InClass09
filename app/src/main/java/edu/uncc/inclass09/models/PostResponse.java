package edu.uncc.inclass09.models;

import java.util.ArrayList;

public class PostResponse {

    private ArrayList<Post> posts;

    public PostResponse(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }
}
