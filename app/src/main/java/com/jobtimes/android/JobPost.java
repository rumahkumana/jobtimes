package com.jobtimes.android;

import android.renderscript.Sampler;

import com.firebase.jobdispatcher.Job;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rumahkumana on 27/02/18.
 */

public class JobPost {
    public String mUsername, mMessage, mTitle;

    public JobPost() {
    }

    public JobPost(String username, String message, String title) {
        this.mUsername= username;
        this.mMessage = message;
        this.mTitle = title;
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

    public String getTitle() {
        return mTitle;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
