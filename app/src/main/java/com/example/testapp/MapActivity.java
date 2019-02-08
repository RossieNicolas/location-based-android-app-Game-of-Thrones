package com.example.testapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

import cz.mendelu.busItWeek.library.BeaconTask;
import cz.mendelu.busItWeek.library.ChoicePuzzle;
import cz.mendelu.busItWeek.library.CodeTask;
import cz.mendelu.busItWeek.library.GPSTask;
import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.Puzzle;
import cz.mendelu.busItWeek.library.SimplePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;
import cz.mendelu.busItWeek.library.beacons.BeaconDefinition;
import cz.mendelu.busItWeek.library.beacons.BeaconUtil;
import cz.mendelu.busItWeek.library.map.MapUtil;
import cz.mendelu.busItWeek.library.qrcode.QRCodeUtil;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationComponent locationComponent;
    private StoryLine storyLine;
    private Task currentask;
    private Marker currentMarker;
    //private BeaconUtil beaconUtil;
    //private ImageButton qrCodeButton;
    //private CardView beaconScanningCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoibmljb2xhc3Jvc3NpZSIsImEiOiJjanJzeTc4cjgwb3AwNDNudmp4YWZrOTM3In0.aeamPVRBkst8TJtvUanYZg");
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        storyLine = StoryLine.open(this, BusITWeekDatabaseHelper.class);
        currentask = storyLine.currentTask();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setAllGesturesEnabled(true);
        initialiseListeners();
        updateMarkers();
    }

    private void initialiseListeners() {
        if (currentask != null) {
            if (currentask instanceof GPSTask) {
                initialiseLocationComponent();
                initialiseLocationengine();

            }

        }
    }

    private void removeListeners() {

        if (mapboxMap != null && locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
    }

    private void updateMarkers() {
        if (currentask != null && mapboxMap != null) {
            if (currentMarker != null) {
                mapboxMap.removeMarker(currentMarker);
            }
            currentMarker = mapboxMap.addMarker(createTaskMarker(this, currentask));
        }
    }

    private void runPuzzleActivity(Puzzle puzzle) {
        if (puzzle instanceof SimplePuzzle) {
            if (currentask.getName().equals("1")) {
                startActivity(new Intent(this, SimplePuzzleActivity.class));
            } else if (currentask.getName().equals("2")) {
                startActivity(new Intent(this, SimplePuzzleActivity2.class));
            }

        }

        if (puzzle instanceof ChoicePuzzle) {
            if (currentask.getName().equals("4")) {
                startActivity(new Intent(this, ChoicePuzzleActivity.class));
            } else if (currentask.getName().equals("5")) {
                startActivity(new Intent(this, ChoicePuzzleActivity2.class));
            }
        }
        if (puzzle instanceof ImageSelectPuzzle) {
            if (currentask.getName().equals("3")) {
                startActivity(new Intent(this, ImagePuzzleActivity.class));
            } else if (currentask.getName().equals("verdict")) {
                startActivity(new Intent(this, FinalVerdictActivity.class));
            }
            startActivity(new Intent(this, ImagePuzzleActivity.class));
        }
    }

    private MarkerOptions createTaskMarker(Context context, Task task) {
        int color = R.color.colorGPS;
        if (task instanceof BeaconTask) {
            color = R.color.colorBeacon;
        }
        if (task instanceof CodeTask) {
            color = R.color.colorQR;
        }
        return new MarkerOptions()
                .position(new LatLng(task.getLatitude(), task.getLongitude()))
                .icon(MapUtil.createColoredCircleMarker(context, task.getName(), color));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        currentask = storyLine.currentTask();
        if (currentask == null) {
            startActivity(new Intent(this, FinnishActivity.class));
            finish();
        } else {
            initialiseListeners();
            updateMarkers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        removeListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*String qrCode = QRCodeUtil.onScanResult(this, requestCode, resultCode, data);
        if (qrCode != null) {
            if (qrCode.equals(((CodeTask) currentask).getQR())) {
                runPuzzleActivity(currentask.getPuzzle());
            }
        }*/
    }

    private void initialiseLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            if (mapboxMap != null) {
                locationComponent = mapboxMap.getLocationComponent();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationComponent.activateLocationComponent(this);
                locationComponent.setLocationComponentEnabled(true);

            }
        }else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @SuppressLint("MissingPermission")
    private void initialiseLocationengine(){
        if (mapboxMap != null && PermissionsManager.areLocationPermissionsGranted(this)){
            locationEngine = new LocationEngineProvider(this)
                    .obtainBestLocationEngineAvailable();
            locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
            locationEngine.setInterval(1000);
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
            locationEngine.activate();
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentask != null){
            if (currentask instanceof GPSTask){
                double radius = ((GPSTask)currentask).getRadius();
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                LatLng tasklocation = new LatLng(currentask.getLatitude(), currentask.getLongitude());
                if (userLocation.distanceTo(tasklocation)< radius){
                    runPuzzleActivity(currentask.getPuzzle());
                }
            }
        }
    }
}
