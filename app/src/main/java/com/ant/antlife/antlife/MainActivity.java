package com.ant.antlife.antlife;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_PERMISSION = 2;
    //private onKeyBackPressedListener mOnKeyBackPressedListener;
    private SharedPreferences settings;
    String gps_permission;
    String id;
    /** 구글 맵 관련**/
    GoogleMap googleMap;
    //Marker m1,m2,m3,m4,m5,m6,m7;
    ClusterManager<House> mClusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("KEY", 0);
        id = settings.getString("ID", "").toString();
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        gps_permission = settings.getString("gps_permission", "false").toString();
        /** 최초 실행 시 위치 권한 확인**/
        if (gps_permission == "false") {
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
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
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
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
     @Override public void onBackPressed() {
     if (mOnKeyBackPressedListener != null) {
     mOnKeyBackPressedListener.onBack();
     }
     }
     */
    @Override
    public void onMapReady(final GoogleMap map) { //
        googleMap = map;
        mClusterManager = new ClusterManager<>(this, googleMap);

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

        Double init_lat = 37.56 ;
        Double init_lng = 126.97;
        Double cluster_lat = 37.5;
        Double cluster_lng = 127.97;
        int cnt = 1;
        int cluster_cnt = 1;

        for(int i = 0; i<10; i++){
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
        for(int i = 0; i<10; i++){
            cluster_lat_list.add(cluster_lat);
            cluster_lng_list.add(cluster_lng);
            cluster_place_name.add("Cluster Dot" + cnt);

            cluster_cnt++;
            cluster_lat += 0.05;
            cluster_lng += 0.05;
        }
        for(int i=0; i<10; i++){
            mClusterManager.addItem(new House(new LatLng(cluster_lat_list.get(i),cluster_lng_list.get(i)),cluster_place_name.get(i)));
            mClusterManager.addItem(new House(new LatLng(lat_list.get(i),lng_list.get(i)),place_name.get(i)));
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
                placeDialog = new PlaceDialog(MainActivity.this,marker.getTitle());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
                return false;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PlaceDialog placeDialog;
                placeDialog = new PlaceDialog(MainActivity.this,marker.getTitle());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
            }
        });
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<House>() {
            @Override
            public boolean onClusterClick(Cluster<House> cluster) {
                cluster.getItems();
                return false;
            }
        });
        /**
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });
        **/
    }
    /*
    private void setCluster(final GoogleMap googleMap) {
        final List<House> mPosi = (List<House>) mClusterManager;
        if(mPosi != null) {
            if (mPosi.size() > 0) {
                mClusterManager = new ClusterManager<House>(MainActivity.this, googleMap);
                googleMap.setOnCameraIdleListener(mClusterManager);
                googleMap.setOnMarkerClickListener(mClusterManager);

                LatLngBounds.Builder builder = LatLngBounds.builder();  // Bounds 모든 데이터를 맵 안으로 보여주게 하기 위한
                LatLngBounds bounds = addItems(builder, mPosi);         // 클러스터 Marker 추가
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, ZoomLevel));
                float zoom = googleMap.getCameraPosition().zoom - 0.5f;
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

                mClusterManager.setRenderer(new OwnIconRendered(mAppData, googleMap, mClusterManager));

                // 내용 클릭시
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                    }
                });

                // 클러스터 클릭시 펼치기
                mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PositionItem>() {
                    @Override
                    public boolean onClusterClick(Cluster<PositionItem> cluster) {
                        LatLngBounds.Builder builder_c = LatLngBounds.builder();
                        for (ClusterItem item : cluster.getItems()) {
                            builder_c.include(item.getPosition());
                        }
                        LatLngBounds bounds_c = builder_c.build();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds_c, ZoomLevel));
                        float zoom = googleMap.getCameraPosition().zoom - 0.5f;
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
                        return true;
                    }
                });
            }
        }
    }

    // 마커 추가
    private void addItems(List<PositionItem> mPosi) {
        for (PositionItem item : mPosi) {
            mClusterManager.addItem(item);
        }
    }

    // 마커 커스텀 class
    class CustomIconRenderer extends DefaultClusterRenderer<PositionItem> {
        public CustomIconRenderer(Context context, GoogleMap map, ClusterManager<PositionItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(PositionItem item, MarkerOptions markerOptions) {
            int id = R.drawable.rect_memo1;
            if(item.bg == 1) {                  // drawable 변경
            } else if (item.bg == 2) {
            } else if (item.bg == 3) {
            } else if (item.bg == 4) {
            }
            Drawable d = ContextCompat.getDrawable(getActivity(), id);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(d);

            markerOptions.icon(markerIcon);
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
**/
    class PlaceDialog extends Dialog {
        String place;
        TextView t1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.place_dialog);
            t1 = (TextView)findViewById(R.id.dialog_textview);
            t1.setText(place);
            // 마커 클릭시 호출되는 콜백 메서드
            Toast.makeText(getApplicationContext(), place, Toast.LENGTH_SHORT).show();
            //

        }

        public PlaceDialog(Context context, String place) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.place = place;
        }
    }
    public void init_marker(){
        /**
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions moMan = new MarkerOptions();
        moMan.title("1");
        moMan.snippet("1");
        moMan.position(latLng);
        moMan.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1));
        mMarkerMan=mGoogleMap.addMarker(moMan);**/
    }

}
