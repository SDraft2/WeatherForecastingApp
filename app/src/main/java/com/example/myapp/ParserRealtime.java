package com.example.myapp;

import org.json.JSONArray;
import org.json.JSONObject;

/*

T1H     기온          ℃      Double
RN1     1시간 강수량  mm
UUU     동서바람성분  m/s     동(+표기), 서(-표기)
VVV     남북바람성분  m/s     북(+표기), 남(-표기)
REH     습도          %
PTY     강수형태      코드값   없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
VEC     풍향          0
WSD     풍속          1

 */
public class ParserRealtime {
    RealtimeData realtimeData;

    public ParserRealtime(String jsonStr)
    {
        realtimeData = new RealtimeData();

        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray items = root.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
            for(int i=0 ; i < 9 ; i++)
            {
                JSONObject item = items.getJSONObject(i);
                String str = item.getString("category");

                switch(str)
                {
                    case "T1H":
                        realtimeData.setT1H(item.getDouble("obsrValue"));
                        break;

                    case "RN1":
                        realtimeData.setRN1(item.getDouble("obsrValue"));
                        break;

                    case "UUU":
                        realtimeData.setUUU(item.getDouble("obsrValue"));
                        break;

                    case "VVV":
                        realtimeData.setVVV(item.getDouble("obsrValue"));
                        break;

                    case "REH":
                        realtimeData.setREH(item.getDouble("obsrValue"));
                        break;

                    case "PTY":
                        realtimeData.setPTY(item.getInt("obsrValue"));
                        break;

                    case "VEC":
                        realtimeData.setVEC(item.getDouble("obsrValue"));
                        break;

                    case "WSD":
                        realtimeData.setWSD(item.getDouble("obsrValue"));
                        break;
                }
            }
        }
        catch(Exception e)
        {

        }
    }

    public RealtimeData getRealtimeData()
    {
        return realtimeData;
    }

}
