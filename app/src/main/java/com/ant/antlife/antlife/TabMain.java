package com.ant.antlife.antlife;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by NAKNAK on 2017-08-27.
 */

public class TabMain extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback
        , GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    LinearLayout linearLayout;
    public static Activity mainactivity;
    LocationManager locationManager;
    GoogleApiClient mClient;
    private static final int REQUEST_CODE_PERMISSION = 2;
    //private onKeyBackPressedListener mOnKeyBackPressedListener;
    private SharedPreferences settings;
    DrawerLayout drawerLayout;
    String gps_permission;
    String id;
    /**
     * 구글 맵 관련
     **/
    GoogleMap googleMap;
    //Marker m1,m2,m3,m4,m5,m6,m7;
    ClusterManager<House> mClusterManager;
    ListView listview;
    Button menu_btn1, menu_btn2, menu_btn3, menu_btn4;
    Button post_btn, good_btn, bad_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        gps_permission = settings.getString("gps_permission", "false").toString();
        if (gps_permission == "false") {
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                    // If any permission above not allowed by user, this condition will execute every time, else your else part will work
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("gps_permission", "true");
                    editor.apply();
                } else {
                    if (gps_permission == "false") {
                        //Toast.makeText(getApplicationContext(), "위치정보 사용 권한에 동의해주시고,\n다시 시도 해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        */
        mClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

    }
        /*
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) getContext());
        */

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_main, container, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.main_container);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("map");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment, "map").commit();
            mapFragment.getMapAsync(this);
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest request = new LocationRequest();
        request.setInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, mListener);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        //placeAutocompleteAdapter.setGoogleApiClient(null);
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }
    };
    @Override
    public void onMapReady(GoogleMap map) { //
        googleMap = map;
        mClusterManager = new ClusterManager<>(getContext(), googleMap);

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));

        ArrayList<Double> lat_list = new ArrayList<Double>();
        ArrayList<Double> lng_list = new ArrayList<Double>();
        ArrayList<MarkerOptions> markerOptionses = new ArrayList<MarkerOptions>();
        ArrayList<String> place_name = new ArrayList<String>();

        ArrayList<Double> cluster_lat_list = new ArrayList<Double>();
        ArrayList<Double> cluster_lng_list = new ArrayList<Double>();
        ArrayList<MarkerOptions> cluster_markerOptionses = new ArrayList<MarkerOptions>();
        ArrayList<String> cluster_place_name = new ArrayList<String>();

        Double init_lat = 37.56;
        Double init_lng = 126.97;
        Double cluster_lat = 37.5;
        Double cluster_lng = 127.97;
        int cnt = 1;
        int cluster_cnt = 1;

        for (int i = 0; i < 10; i++) {
            lat_list.add(init_lat);
            lng_list.add(init_lng);
            place_name.add("Dot " + cnt);

            //init_lat += 0.1;
            init_lng += 0.1;
            cnt++;
        }
        /*
        for(int i=0; i<10; i++){
            markerOptionses.add(new MarkerOptions());
            markerOptionses.get(i).position(new LatLng(lat_list.get(i),lng_list.get(i))).title(place_name.get(i)).snippet(place_name.get(i));
            googleMap.addMarker(markerOptionses.get(i)).showInfoWindow();
        }
        */
        /** 군집 되어서 합쳐질 마커들 **/
        for (int i = 0; i < 10; i++) {
            cluster_lat_list.add(cluster_lat);
            cluster_lng_list.add(cluster_lng);
            cluster_place_name.add("Cluster Dot" + cnt);

            cluster_cnt++;
            cluster_lat += 0.05;
            cluster_lng += 0.05;
        }
        for (int i = 0; i < 10; i++) {
            mClusterManager.addItem(new House(new LatLng(cluster_lat_list.get(i), cluster_lng_list.get(i)), cluster_place_name.get(i)));
            mClusterManager.addItem(new House(new LatLng(lat_list.get(i), lng_list.get(i)), place_name.get(i)));
            //cluster_markerOptionses.add(new MarkerOptions());
            //cluster_markerOptionses.get(i).position(new LatLng(cluster_lat_list.get(i),cluster_lng_list.get(i))).title(cluster_place_name.get(i)).snippet(cluster_place_name.get(i));
            //googleMap.addMarker(cluster_markerOptionses.get(i)).showInfoWindow();
        }
        // 마커클릭 이벤트 처리
        // GoogleMap 에 마커클릭 이벤트 설정 가능.
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PlaceDialog placeDialog;
                placeDialog = new PlaceDialog(getContext(), marker.getTitle());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
                return false;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PlaceDialog placeDialog;
                placeDialog = new PlaceDialog(getContext(), marker.getTitle());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
            }
        });
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<House>() {
            @Override
            public boolean onClusterClick(Cluster<House> cluster) {
                PlaceDialog placeDialog;
                placeDialog = new PlaceDialog(getContext(), cluster.getPosition().toString());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
                return false;
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    class PlaceDialog extends Dialog {
        String place;
        //TextView t1;
        ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.place_dialog);
            //t1 = (TextView)findViewById(R.id.dialog_textview);
            //t1.setText(place);
            // 마커 클릭시 호출되는 콜백 메서드
            Toast.makeText(getApplicationContext(), place, Toast.LENGTH_SHORT).show();
            //

        }

        public PlaceDialog(Context context, String place) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.place = place;
        }
    }
}
