package com.example.myapp;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class ParserBoroughFinedust {
    String areaStr[] = {"서울광역시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "경기도", "강원도", "충청북도", "충청남도","전라북도","전라남도","경상북도","경상남도", "제주특별시", "세종특별시"};
    String castAreaStr[] = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남","전북","전남","경북","경남", "제주", "세종"};
    String nowTown;
    int pm10Value;
    int pm25Value;

    String url = "";

    HashMap<String, Integer> dustList = new HashMap<String, Integer>();

    boolean isLoadComplete = false;
    String dateFormat;

    public ParserBoroughFinedust(String town, final String borough)
    {
        dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:00", Locale.KOREA).format(System.currentTimeMillis() - (30 * 60 * 1000));

        for(int i=0 ; i<areaStr.length ; i++)
        {
            if(areaStr[i].equals(town))
            {
                nowTown = castAreaStr[i];
                break;
            }
        }
        url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=" + MainActivity.SERVICE_KEY + "&numOfRows=100&pageNo=1&sidoName=" + nowTown + "&searchCondition=DAILY";
        System.out.println(url + "borough" + borough + " - " + dateFormat);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL xmlURL = new URL(url);

                    InputStream in = xmlURL.openStream();

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(in, "UTF-8");

                    int eventType = xpp.getEventType();

                    String tagName="";
                    String text;

                    while(eventType != XmlPullParser.END_DOCUMENT){
                        switch(eventType)
                        {
                            case XmlPullParser.START_TAG:
                                tagName = xpp.getName();
                                break;
                            case XmlPullParser.TEXT:
                                if(tagName.equals("dataTime") && xpp.getText().equals(dateFormat))
                                {
                                    System.out.println(xpp.getName() + " -=- " +  xpp.getText());
                                    xpp.next();
                                    xpp.next();
                                    xpp.next();
                                    xpp.next();

                                    if(xpp.getText().equals(borough))
                                    {
                                        int event = 0;

                                        while(!("pm10Value".equals(xpp.getName()) && event==XmlPullParser.START_TAG))
                                            event = xpp.next();

                                        xpp.next();

                                        pm10Value = Integer.parseInt(xpp.getText());

                                        while(!("pm25Value".equals(xpp.getName()) && event==XmlPullParser.START_TAG))
                                            event = xpp.next();

                                        xpp.next();

                                        pm25Value = Integer.parseInt(xpp.getText());
                                    }
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                        }
                        eventType = xpp.next();
                    }
                    isLoadComplete = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("NewsApp" + "예외발생 :"+e.getMessage());
                }
            }
        }).start();
    }

    public HashMap<String, Integer> getHashMap()
    {
        return dustList;
    }

    public boolean IsLoadComplete()
    {
        return isLoadComplete;
    }

    public int getPm10Value()
    {
        return pm10Value;
    }
    public int getPm25Value()
    {
        return pm25Value;
    }

}
