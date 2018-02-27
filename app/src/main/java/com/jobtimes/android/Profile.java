package com.jobtimes.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.jobtimes.android.MainActivity.mAuth;
import static com.jobtimes.android.MainActivity.mDatabasehandler;
import static com.jobtimes.android.MainActivity.mGoogleSignInClient;

public class Profile extends AppCompatActivity implements View.OnClickListener, FetchAddressTask.OnTaskCompleted {
    private static final String LOCATION_TAG = "GET_LOCATION";
    private static final int REQUEST_LOCATION_PERMISSION = 0;
    TextView userDisplayname;
    TextView mLocationTextView;
    TextView mFindUs;
    FusedLocationProviderClient mFusedLocationClient;
    SearchView searchView;
    ImageView lamaran;
    ImageView melamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_STYLE_SWITCH, false);
        boolean useDarkTheme = switchPref;

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lamaran = (ImageView) findViewById(R.id.lamaran);
        melamar = (ImageView) findViewById(R.id.melamar);
        // Set up user display name
        Intent intent = getIntent();
        userDisplayname = findViewById(R.id.userDisplayName);
        userDisplayname.setText(intent.getStringExtra(MainActivity.EXTRA_DISPLAYNAME));

        // Set up view
        mLocationTextView = findViewById(R.id.geolocation);
        mFindUs = findViewById(R.id.find_us);

        // Set saveInstanceState
        if (savedInstanceState != null) {
            userDisplayname.setText(savedInstanceState.getString("userDisplayName"));
        }

        // Set up geolocation
        getLocation();

        //onclick listener
        melamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MelamarActivity.class);
                startActivity(intent);
            }
        });

        lamaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, LamaranActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userDisplayName", userDisplayname.getText().toString());
    }

    public void openWebsite(View view) {
        // Get the URL text.
        String url = "http://gitlab.informatika.org/IF3111-2018-JuniorDevelopers/android";
        // Parse the URI and create the intent.
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        // Find an activity to hand the intent and start that activity.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Profile.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Start the reverse geocode AsyncTask
                                new FetchAddressTask(Profile.this,
                                        Profile.this).execute(location);
                            } else {
                                mLocationTextView.setText(R.string.no_location);
                            }
                        }


                    });
        }
        mLocationTextView.setText(getString(R.string.location_text,
                getString(R.string.loading),
                System.currentTimeMillis()));
        searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(Profile.this, SearchActivity.class);
                intent.putExtra("Query", s);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void googleSignOut() {
        // Firebase sign out
        mDatabasehandler.deleteFrom("Online");
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        Toast.makeText(this, "Sign Out Succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, this.getClass());
        switch (item.getItemId()) {
            case R.id.settings: {
                intent = new Intent(this, SettingsActivity.class);
                break;
            }
            case R.id.alarm: {
                intent = new Intent(this, AlarmActivity.class);
                break;
            }
            case R.id.sign_out: {
                googleSignOut();
                intent = new Intent(this, MainActivity.class);
                break;
            }
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTaskCompleted(String result) {
        mLocationTextView.setText(getString(R.string.location_text,
                result, System.currentTimeMillis()));
    }
}
