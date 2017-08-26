package com.ant.antlife.antlife;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static Activity mainactivity;
    private static final int REQUEST_CODE_PERMISSION = 2;
    //private onKeyBackPressedListener mOnKeyBackPressedListener;
    private SharedPreferences settings;
    DrawerLayout drawerLayout;
    String gps_permission;
    String id;
    /** 구글 맵 관련**/
    //GoogleMap googleMap;
    //Marker m1,m2,m3,m4,m5,m6,m7;
    //ClusterManager<House> mClusterManager;
    ListView listview;
    Button menu_btn1,menu_btn2,menu_btn3,menu_btn4;
    Button post_btn,good_btn,bad_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainactivity = MainActivity.this;

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        settings = getSharedPreferences("KEY", 0);
        id = settings.getString("ID", "").toString();

        /** status바(최상단 바) **/
        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id != 0 && getResources().getBoolean(id)) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.tab_frame, new TabMain()).commit();

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


    }
    public void openHamberger(){
        drawerLayout.openDrawer(Gravity.LEFT);
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
    /*
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
                PlaceDialog placeDialog;
                placeDialog = new PlaceDialog(MainActivity.this,cluster.getPosition().toString());
                placeDialog.setCanceledOnTouchOutside(true);
                placeDialog.show();
                return false;
            }
        });
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
            */

}
