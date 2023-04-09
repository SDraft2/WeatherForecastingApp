package com.example.myapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.myapp.MainActivity.requestQueue;

public class FragmentRealtime extends Fragment {
    ParserBoroughFinedust boroughFinedust;
    ParserRealtime realtimeDataParser;
    RealtimeData realtimeData;

    ImageView skyImg;
    TextView skyTxt;
    TextView wsdTxt;
    TextView tprTxt;
    TextView pm10Txt;
    TextView pm25Txt;

    ProgressBar pm10Bar;
    ProgressBar pm25Bar;

    String baseDate;
    String baseTime = "0500";
    String jsonStr;


    SimpleDateFormat dateFormat;


    public FragmentRealtime(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_realtime, container, false);
        tprTxt = (TextView)v.findViewById(R.id.realtime_tprTxt);
        skyTxt = (TextView)v.findViewById(R.id.realtime_ptyTxt);
        wsdTxt = (TextView)v.findViewById(R.id.realtime_wsdTxt);

        skyImg = (ImageView)v.findViewById(R.id.realtime_skyImg);
        pm10Txt = (TextView)v.findViewById(R.id.realtime_finedustTxt);
        pm25Txt = (TextView)v.findViewById(R.id.realtime_ultraFinedustTxt);
        pm10Bar = (ProgressBar) v.findViewById(R.id.realtime_finedustProgressBar);
        pm25Bar = (ProgressBar) v.findViewById(R.id.realtime_ultraFinedustProgressBar);

        ((ProgressBar)v.findViewById(R.id.realtime_finedustHintGoodBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_GOOD, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_finedustHintNormalBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_NORMAL, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_finedustHintBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_BAD, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_finedustHintTooBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_TOOBAD, PorterDuff.Mode.SRC_IN);

        ((ProgressBar)v.findViewById(R.id.realtime_ultraFinedustHintGoodBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_GOOD, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_ultraFinedustHintNormalBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_NORMAL, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_ultraFinedustHintBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_BAD, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.realtime_ultraFinedustHintTooBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_TOOBAD, PorterDuff.Mode.SRC_IN);
/*
        pm10Bar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        pm25Bar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
*/
        pm25Bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        return v;
    }

    public void ParseJson(int nx, int ny)
    {
        dateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA);
        baseDate = dateFormat.format(System.currentTimeMillis());

        if(Integer.parseInt(new SimpleDateFormat( "mm", Locale.KOREA).format(System.currentTimeMillis())) < 30)
        {
            baseTime = new SimpleDateFormat ( "HH00", Locale.KOREA).format(System.currentTimeMillis() - (60 * 60 * 1000));
            baseDate = dateFormat.format(System.currentTimeMillis() - (60 * 60 * 1000));
        }
        else
        {
            baseTime = new SimpleDateFormat ( "HH00", Locale.KOREA).format(System.currentTimeMillis());
            baseDate = dateFormat.format(System.currentTimeMillis());
        }

        String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?serviceKey=" + MainActivity.SERVICE_KEY + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx=" + nx+ "&ny=" + ny+ "&numOfRows=10&pageNo=1&_type=json";

        System.out.println(url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        jsonStr = response;
                        realtimeDataParser = new ParserRealtime(jsonStr);
                        realtimeData = realtimeDataParser.getRealtimeData();
                        SetView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("에러 : " + error.getMessage() + "\n");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);

    }

    public void SetView()
    {
        tprTxt.setText(realtimeData.getTptStr());
        skyTxt.setText(realtimeData.getPrecipStr());
        skyImg.setImageResource(realtimeData.getPrecipImg());
        wsdTxt.setText(realtimeData.getWsdStr());
    }

    public void SetFinedustView()
    {
        int PM10 = boroughFinedust.getPm10Value();
        int PM25 = boroughFinedust.getPm25Value();

        pm10Txt.setText(PM10+"");
        pm25Txt.setText(PM25+"");

        int pm10Color;
        int pm25Color;

        if(PM10 <= 30)
            pm10Color = MainActivity.DUST_COLOR_GOOD;
        else if(PM10 > 30 && PM10 <= 90)
            pm10Color = MainActivity.DUST_COLOR_NORMAL;
        else if(PM10 > 90 && PM10 <= 150)
            pm10Color = MainActivity.DUST_COLOR_BAD;
        else if(PM10 > 150)
            pm10Color = MainActivity.DUST_COLOR_TOOBAD;
        else if(PM10 > 250)
        {
            pm10Color = MainActivity.DUST_COLOR_TOOBAD;
            pm10Bar.setMax(PM10);
        }
        else
            pm10Color = MainActivity.DUST_COLOR_ERROR;

        if(PM25 <= 15)
            pm25Color = MainActivity.DUST_COLOR_GOOD;
        else if(PM25 > 15 && PM25 <= 35)
            pm25Color =  MainActivity.DUST_COLOR_NORMAL;
        else if(PM25 > 35 && PM25 <= 75)
            pm25Color =  MainActivity.DUST_COLOR_BAD;
        else if(PM25 > 75)
            pm25Color =  MainActivity.DUST_COLOR_TOOBAD;
        else if(PM25 > 150)
        {
            pm25Color =  MainActivity.DUST_COLOR_TOOBAD;
            pm25Bar.setMax(PM25);
        }
        else
            pm25Color =  MainActivity.DUST_COLOR_ERROR;

        pm10Bar.setProgress(PM10);
        pm25Bar.setProgress(PM25);
/*
        pm10Txt.setTextColor(pm10Color);
        pm25Txt.setTextColor(pm25Color);*/
        pm10Bar.getProgressDrawable().setColorFilter(pm10Color, PorterDuff.Mode.SRC_IN);
        pm25Bar.getProgressDrawable().setColorFilter(pm25Color, PorterDuff.Mode.SRC_IN);
 }

    public void LoadFinedustXML(String town, String borough)
    {
        boroughFinedust = new ParserBoroughFinedust(town, borough);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        while(!boroughFinedust.IsLoadComplete())
                        {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        SetFinedustView();
                    }
                });
            }
        }).start();
    }

}
