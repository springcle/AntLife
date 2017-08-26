package com.ant.antlife.antlife;

import android.Manifest;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_PERMISSION = 2;
    //private onKeyBackPressedListener mOnKeyBackPressedListener;
    private SharedPreferences settings;
    String gps_permission;
    String ID;

    /** 구글 맵 관련**/
    GoogleMap googleMap;
    Marker m1,m2,m3,m4,m5,m6,m7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("KEY", 0);
        ID = settings.getString("ID","").toString();
        gps_permission = settings.getString("gps_permission","false").toString();
        /** 최초 실행 시 위치 권한 확인**/
        if (gps_permission == "false"){
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                    // If any permission above not allowed by user, this condition will execute every time, else your else part will work
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("gps_permission", "true");
                }
                else {
                    if (gps_permission == "false") {
                        //Toast.makeText(getApplicationContext(), "위치정보 사용 권한에 동의해주시고,\n다시 시도 해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /** back키 overriding
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    @Override
    public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBack();
        }
    }
    */
    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    public void init_marker(){
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions moMan = new MarkerOptions();
        moMan.title("1");
        moMan.snippet("1");
        moMan.position(latLng);
        moMan.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1));
        mMarkerMan=mGoogleMap.addMarker(moMan);
    }
}
