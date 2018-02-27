package com.jobtimes.android;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rumahkumana on 19/02/18.
 */

public class DatabaseHandler {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private int jobPostsCount;

    public DatabaseHandler() {

    }


    public int getJobPostsCount() {
        return jobPostsCount;
    }

    public HashMap<String, Object> getJobPost() {
        HashMap<String, Object> jobPosts = new HashMap<String, Object>();
        mDatabase.child("/JobPost/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> jobPosts = (HashMap<String, Object>) ((HashMap<String, Object>) dataSnapshot.getValue()).clone();
                Log.d("Database", jobPosts.toString());
                jobPostsCount = jobPosts.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Database cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        });

        return jobPosts;
    }

    /**
     * Menulis ke database
     *
     * @param Location Lokasi penyimpanan di database relatif terhadap root
     * @param Value    Objek yang akan disimpan di database
     */
    protected void writeTo(String Location, String Value) {
        mDatabase.child(Location).setValue(Value);
    }


    protected void writeTo(String Location, Object Value) {
        mDatabase.child(Location).setValue(Value);
    }

    /**
     * Menghapus dari database
     *
     * @param Location Reference child yang akan dihapus
     */
    protected void deleteFrom(String Location) {
        mDatabase.child(Location).setValue("");
    }

    public DatabaseReference getReference() {
        return mDatabase;
    }

    public void writeNewJobPost(String username, JSONObject message) {
        writeTo("/JobPost/" + username, message);
    }

    public void deletePost(String username) {
        deleteFrom("/JobPost/" + username);
    }
}
