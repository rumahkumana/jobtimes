package com.jobtimes.android;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.jobdispatcher.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jobtimes.android.MainActivity.mAuth;
import static com.jobtimes.android.MainActivity.mDatabasehandler;
import static com.jobtimes.android.MainActivity.mGoogleSignInClient;

/**
 * Created by rumahkumana on 22/02/18.
 */

public class SearchActivity extends AppCompatActivity {
    public List<JobPost> jobPostList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JobPostAdapter mAdapter;


    private void prepareJobPostData() throws JSONException {
        String currentDisplayName = mAuth.getCurrentUser().getDisplayName();

        for (int i = 0; i < 10; i++) {
            JSONObject value = new JSONObject();
            value.put("Hello", "I'm " + currentDisplayName);

            mDatabasehandler.writeNewJobPost(currentDisplayName, value);
        }

        mDatabasehandler.getReference().child("/JobPost/"
                +mAuth.getCurrentUser().getDisplayName()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // storing string resources into Array
                ArrayList<HashMap<String, String>> posts = new ArrayList<HashMap<String, String>>() {
                };
                JobPost jobPost;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    JSONObject value = (JSONObject) snapshot.getValue();

                    try {
                        jobPost = new JobPost(key, "Hello", value.getString("Hello"));
                        Log.d("Database", jobPost.toString() + key + value);
                        jobPostList.add(jobPost);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Database cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new JobPostAdapter(jobPostList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        try {
            prepareJobPostData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

    /*

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            mDatabaseHandler.writeNewJobPost("Post Key " + i, "Post ke-" + i);
        }

        mDatabaseHandler.getReference().child("/JobPost/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // storing string resources into Array
                ArrayList<HashMap<String, String>> posts = new ArrayList<HashMap<String, String>>() {
                };

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    String value = snapshot.getValue(String.class);
                    HashMap<String, String> entry = new HashMap<>();

                    entry.put(key, value);
                    posts.add(entry);
                }

                // Store the array of string you got from the database
                // Binding Array to ListAdapter
                setListAdapter(new ArrayAdapter<HashMap<String, String>>(SearchActivity.this, R.layout.list_item, R.id.label, (List<HashMap<String, String>>) posts));
                ListView lv = getListView();

                // listening to single list item on click
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // selected item
                        String extra = ((TextView) view).getText().toString();
                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), SingleListItem.class);
                        // sending data to new activity
                        i.putExtra("extra", extra);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Database cancelled", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    */
