package manjinder.sch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Button logout,start,stop;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);

//        mAuth=FirebaseAuth.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        logout=(Button)findViewById(R.id.logout);

        start=(Button)findViewById(R.id.Start);
        stop=(Button)findViewById(R.id.Stop);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                 DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(user_id);
                current_user_db.removeValue();

                flag=0;

                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(DriverMap.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                onLocationChanged(mLastLocation);
//                LatLng latLng=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//                String user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(user_id);
//                Map mPushdata=new HashMap();
//                mPushdata.put("location",latLng);
//                current_user_db.setValue(mPushdata);

//                Log.d("Value", String.valueOf(mPushdata));
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversAvailable");
////                GeoFire geoFire=new GeoFire(ref);
////                geoFire.removeLocation(userId);
                ref.child(userId).removeValue();
                flag=0;
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.2f));
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));

        if (flag == 1) {
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("DriversAvailable").child(user_id);
            Map mPushdata = new HashMap();
            mPushdata.put("location", latLng);
            current_user_db.setValue(mPushdata);
            Log.d("Value", String.valueOf(mPushdata));
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversAvailable");
//        GeoFire geoFire=new GeoFire(ref);
//        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));
//        Log.d( "onLocationChanged: ", String.valueOf(latLng));
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversAvailable");
////        GeoFire geoFire=new GeoFire(ref);
////        geoFire.removeLocation(userId);
//
//        ref.child(userId).removeValue();
//    }
}
