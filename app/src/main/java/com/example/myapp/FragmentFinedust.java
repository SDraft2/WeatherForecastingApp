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

import java.util.HashMap;

public class FragmentFinedust extends Fragment {

    String type;

    ImageView krMapImg;
    ParserFinedust dataFinedust;

    TextView ulsanTxt;
    TextView busanTxt;
    TextView daeguTxt;
    TextView gyeongnamTxt;
    TextView gyeongbukTxt;
    TextView gwangjuTxt;
    TextView jeonnamTxt;
    TextView jeonbukTxt;
    TextView daejeonTxt;
    TextView sejongTxt;
    TextView chungnamTxt;
    TextView chungbukTxt;
    TextView seoulTxt;
    TextView incheonTxt;
    TextView gyeonggiTxt;
    TextView gangwonTxt;
    TextView jejuTxt;

    HashMap<String, Integer> dustList = new HashMap<String, Integer>();

    public FragmentFinedust(String type)
    {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finedust, container, false);

        krMapImg = v.findViewById(R.id.finedust_koreaMap);

        ulsanTxt = v.findViewById(R.id.finedust_ulsanTxt);
        busanTxt = v.findViewById(R.id.finedust_busanTxt);
        daeguTxt = v.findViewById(R.id.finedust_daeguTxt);
        gyeongnamTxt = v.findViewById(R.id.finedust_gyeongnamTxt);
        gyeongbukTxt = v.findViewById(R.id.finedust_gyeongbukTxt);
        gwangjuTxt = v.findViewById(R.id.finedust_gwangjuTxt);
        jeonnamTxt = v.findViewById(R.id.finedust_jeonnamTxt);
        jeonbukTxt = v.findViewById(R.id.finedust_jeonbukTxt);
        daejeonTxt = v.findViewById(R.id.finedust_daejeonTxt);
        sejongTxt = v.findViewById(R.id.finedust_sejongTxt);
        chungnamTxt = v.findViewById(R.id.finedust_chungnamTxt);
        chungbukTxt = v.findViewById(R.id.finedust_chungbukTxt);
        seoulTxt = v.findViewById(R.id.finedust_seoulTxt);
        incheonTxt = v.findViewById(R.id.finedust_incheonTxt);
        gyeonggiTxt = v.findViewById(R.id.finedust_gyeonggiTxt);
        gangwonTxt = v.findViewById(R.id.finedust_gangwonTxt);
        jejuTxt = v.findViewById(R.id.finedust_jejuTxt);

        if(type.equals("PM25")) {
            ((TextView) v.findViewById(R.id.finedust_dustTypeTxt)).setText("초미세먼지");
            ((TextView) v.findViewById(R.id.finedust_hintGoodTxt)).setText("좋음 0~15");
            ((TextView) v.findViewById(R.id.finedust_hintNormalTxt)).setText("보통 ~35");
            ((TextView) v.findViewById(R.id.finedust_hintBadTxt)).setText("나쁨 ~75");
            ((TextView) v.findViewById(R.id.finedust_hintTooBadTxt)).setText("심각 ~76");
        }


        ((ProgressBar)v.findViewById(R.id.finedust_hintGoodBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_GOOD, PorterDuff.Mode.SRC_IN);
        ((ProgressBar)v.findViewById(R.id.finedust_hintNormalBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_NORMAL, PorterDuff.Mode.SRC_IN);;
        ((ProgressBar)v.findViewById(R.id.finedust_hintBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_BAD, PorterDuff.Mode.SRC_IN);;
        ((ProgressBar)v.findViewById(R.id.finedust_hintTooBadBar)).getProgressDrawable().setColorFilter(MainActivity.DUST_COLOR_TOOBAD, PorterDuff.Mode.SRC_IN);;



        LoadFinedustXml();
        return v;
    }

    public void LoadFinedustXml()
    {
        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?serviceKey=" + MainActivity.SERVICE_KEY + "&numOfRows=1&pageNo=1&itemCode=" + type + "&dataGubun=HOUR&searchCondition=WEEK";
        System.out.println(url);
        dataFinedust = new ParserFinedust(url);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        while(!dataFinedust.IsLoadComplete())
                        {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dustList = dataFinedust.getHashMap();
                        SetView();
                        SetImg();
                    }
                });
            }
        }).start();
    }

    public void SetImg()
    {
        DustMapConverter ic = new DustMapConverter(getContext(), dustList, type);
        krMapImg.setImageBitmap(ic.getBitmap());
    }
    public void SetView()
    {
        ulsanTxt.setText(dustList.get("ulsan") + "");
        busanTxt.setText(dustList.get("busan") + "");
        daeguTxt.setText(dustList.get("daegu") + "");
        gyeongnamTxt.setText(dustList.get("gyeongnam") + "");
        gyeongbukTxt.setText(dustList.get("gyeongbuk") + "");
        gwangjuTxt.setText(dustList.get("gwangju") + "");
        jeonnamTxt.setText(dustList.get("jeonnam") + "");
        jeonbukTxt.setText(dustList.get("jeonbuk") + "");
        daejeonTxt.setText(dustList.get("daejeon") + "");
        sejongTxt.setText(dustList.get("sejong") + "");
        chungnamTxt.setText(dustList.get("chungnam") + "");
        chungbukTxt.setText(dustList.get("chungbuk") + "");
        seoulTxt.setText(dustList.get("seoul") + "");
        incheonTxt.setText(dustList.get("incheon") + "");
        gyeonggiTxt.setText(dustList.get("gyeonggi") + "");
        gangwonTxt.setText(dustList.get("gangwon") + "");
        jejuTxt.setText(dustList.get("jeju") + "");
    }
}
