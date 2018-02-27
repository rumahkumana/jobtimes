package com.jobtimes.android;

/**
 * Created by rumahkumana on 27/02/18.
 */

public class JobPost {
    private String mUsername, mMessage;

    public JobPost() {
    }

    public JobPost(String username, String message) {
        this.mUsername= username;
        this.mMessage = message;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
