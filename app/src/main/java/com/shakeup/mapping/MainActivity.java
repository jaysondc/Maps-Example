package com.shakeup.mapping;

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{

    public final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Add map fragment from here -https://developers.google.com/maps/documentation/android-api/map
        MapFragment mMapFragment = MapFragment.newInstance();
        // Specify that this activity will handle the map
        mMapFragment.getMapAsync(this);

        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_san_francisco) {
            // Add marker on San Francisco
            if(myMap != null){
                // Add marker
                myMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.7749,-122.4194))
                        .title(getResources().getString(R.string.marker_san_francisco)));
                // Move and zoom camera
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7749,-122.4194), 12));
            }


        } else if (id == R.id.nav_new_york) {
            // Center the map on New York
            if(myMap != null){
                // Add marker
                myMap.addMarker(new MarkerOptions()
                        .position(new LatLng(40.7128,-74.0059))
                        .title(getResources().getString(R.string.marker_new_york)));
                // Move and zoom camera
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7128,-74.0059), 12));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Maps permission was granted!
                } else {
                    // Maps permission was denied!
                    // Do nothing
                }
                return;
            }
        }
    }

    /****************************** MAP HANDLER ************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Get a global handler for the map
        myMap = googleMap;

        // Check that we were granted the maps permission. If not, request permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Enable location button
            googleMap.setMyLocationEnabled(true);

            // Get users current location
            LocationManager locationservice = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String myuser = locationservice.getBestProvider(criteria, false);
            Location location = locationservice.getLastKnownLocation(myuser);
            LatLng userlocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Move and zoom map to location
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 12));

        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
