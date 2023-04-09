package com.example.myapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/*
POP     강수확률      %
PTY     강수형태      0:없음, 1:비, 2:비/눈, 3:눈, 4:소나기
SKY     하늘상태      1:맑음, 2:구름조금, 3:구름많음, 4:흐림
REH     습도          %
T3H     3시간 기온            skyAMImg = itemView.findView            skyPMImg = itemView.findViewById(R.id.foreList_skyPMImg);

UUU     풍속(동서)
VVV     풍속(남북)
VEC     풍향
    TMN     최저기온 (06시)
    TMX     최대기온 (15시)
 */
    public class ParserForecast {

        SimpleDateFormat dateFormat;
    String AMformat = "0600";
    String PMformat = "1500";

    String tomorrowDate;
    String twoDayAgoDate;


    FcstData tomorrowData;
    FcstData twoDayAgoData;
    public ParserForecast(String jsonStr)
    {
        tomorrowData = new FcstData();
        twoDayAgoData = new FcstData();

        dateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA);
        tomorrowDate = dateFormat.format(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
        twoDayAgoDate = dateFormat.format(System.currentTimeMillis() + (48 * 60 * 60 * 1000));

        tomorrowData.setDate(new SimpleDateFormat ( "EEE\n(MM-dd)", Locale.KOREA).format(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
        twoDayAgoData.setDate(new SimpleDateFormat ( "EEE\n(MM-dd)", Locale.KOREA).format(System.currentTimeMillis() + (48 * 60 * 60 * 1000)));

        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray items = root.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
            System.out.println(items);
            for(int i=0 ; i < items.length() ; i++)
            {
                JSONObject item = items.getJSONObject(i);

                if(item.getString("fcstDate").equals(dateFormat.format(System.currentTimeMillis() + (72 * 60 * 60 * 1000))))
                    break;

                String category = item.getString("category");


                int state = getState(item.getString("fcstTime"));

                switch(category)
                {
                    case "TMN":
                        if(item.getString("fcstDate").equals(tomorrowDate))
                            tomorrowData.setTMN(item.getDouble("fcstValue"));
                        else
                            twoDayAgoData.setTMN(item.getDouble("fcstValue"));
                        break;

                    case "TMX":
                        if(item.getString("fcstDate").equals(tomorrowDate))
                            tomorrowData.setTMX(item.getDouble("fcstValue"));
                        else
                            twoDayAgoData.setTMX(item.getDouble("fcstValue"));
                        break;

                    case "SKY":
                        if(state == -1)
                            break;

                        System.out.println(item.getString("fcstDate")+item.getString("fcstTime") +"fcst"+ state+" -4 "+ item.getInt("fcstValue"));
                        if(item.getString("fcstDate").equals(tomorrowDate))
                            tomorrowData.setSKY(state, item.getInt("fcstValue"));
                        else
                            twoDayAgoData.setSKY(state, item.getInt("fcstValue"));
                        break;

                    case "PTY":
                        if(state == -1)
                            break;

                        if(item.getString("fcstDate").equals(tomorrowDate))
                            tomorrowData.setPTY(state, item.getInt("fcstValue"));
                        else
                            twoDayAgoData.setPTY(state, item.getInt("fcstValue"));
                        break;

                    case "WSD":
                        if(state == -1)
                            break;

                        if(item.getString("fcstDate").equals(tomorrowDate))
                            tomorrowData.setWSD(state, item.getInt("fcstValue"));
                        else
                            twoDayAgoData.setWSD(state, item.getInt("fcstValue"));
                        break;
                }
            }
        }
        catch(Exception e)
        {

        }
    }

    private int getState(String fcstTime)
    {
        int state = 0;
        if(fcstTime.equals(AMformat))
            state = FcstData.AM;
        else if(fcstTime.equals(PMformat))
            state = FcstData.PM;
        else
            return -1;
        return state;
    }



    public FcstData getTomorrowData() {
        return tomorrowData;
    }

    public void setTomorrowData(FcstData tomorrowData) {
        this.tomorrowData = tomorrowData;
    }

    public FcstData getTwoDayAgoData() {
        return twoDayAgoData;
    }

    public void setTwoDayAgoData(FcstData twoDayAgoData) {
        this.twoDayAgoData = twoDayAgoData;
    }


}
