package com.example.myapp;
/*
 * build.gradle에서 imolementation 'com.android.volley:volley:1.1.0' 추가함
 * 제약조건에서 android.permission.internet추가
 * */
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int DUST_COLOR_GOOD = Color.rgb(150, 250, 250);
    static final int DUST_COLOR_NORMAL = Color.rgb(50, 200, 50);
    static final int DUST_COLOR_BAD = Color.rgb(250, 150, 100);
    static final int DUST_COLOR_TOOBAD =Color.rgb(250, 0, 0);
    static final int DUST_COLOR_ERROR =Color.rgb(0, 0, 0);

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    public static String SERVICE_KEY = "";
    static RequestQueue requestQueue;
    LocationManager locMgr;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    FragmentRealtime fragRealtime;
    FragmentForecast fragForecast;
    FragmentFinedust fragFinedust;
    FragmentFinedust fragUltraFinedust;


    /*
    * lon, lat은 각각 경도 위도
    * */



    int nx = 60;
    int ny = 127;



    TextView addrTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        LoadGPS();
    }

    public void Init()
    {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        fragRealtime = new FragmentRealtime();
        fragForecast = new FragmentForecast();
        fragFinedust = new FragmentFinedust("PM10");

        fragUltraFinedust = new FragmentFinedust("PM25");

        pagerAdapter.addItem(fragRealtime);
        pagerAdapter.addItem(fragForecast);
        pagerAdapter.addItem(fragFinedust);
        pagerAdapter.addItem(fragUltraFinedust);

        addrTxt = findViewById(R.id.addrTxt);
        pager = findViewById(R.id.pager);

        pager.setOffscreenPageLimit(4);
        pager.setAdapter(pagerAdapter);

    }

    public void LoadGPS()
    {
        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            GPSListener gpsListener = new GPSListener();

            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    60 * 1000, // 통지사이의 최소 시간간격 (miliSecond)
                    50, // 통지사이의 최소 변경거리 (m)
                     gpsListener);

            Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(loc == null)
                loc = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(loc == null)
                return;

            gpsListener.setLocation(loc);

            Geocoder geocoder = new Geocoder(this);
            try{
                List<Address> locList = geocoder.getFromLocation(
                        gpsListener.getLatitude(),
                        gpsListener.getLongitude(),
                        3
                );
                addrTxt.setText(locList.get(0).getAddressLine(0));


                KsmGrid ksmGrid = new KsmGrid(KsmGrid.TO_GRID, gpsListener.getLatitude(), gpsListener.getLongitude());

                nx = (int)ksmGrid.getX();
                ny = (int)ksmGrid.getY();

                fragForecast.ParseJson(nx,ny);
                fragRealtime.ParseJson(nx,ny);
                fragRealtime.LoadFinedustXML(locList.get(0).getAdminArea(), locList.get(0).getSubLocality()); //도시이름과 구
            }
            catch(Exception e){ e.printStackTrace();}

            System.out.print("경도 : " + gpsListener.getLatitude() + "위도 : " + gpsListener.getLongitude());
        } catch(SecurityException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {;
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.runFinalization();
            System.exit(0);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position)
            {
                case 0:
                    return "실시간 날씨 ";
                case 1:
                    return "동네예보";
                case 2:
                    return "전국 미세먼지";
                case 3:
                    return "전국 초 미세먼지";
                default:
                    return position + "";
            }
        }
    }

}
