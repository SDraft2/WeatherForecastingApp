package com.example.myapp;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class ParserFinedust {
    String areaStr[] = {"seoul", "busan", "daegu", "incheon", "gwangju", "daejeon", "ulsan", "gyeonggi", "gangwon", "chungbuk", "chungnam","chungbuk","jeonbuk","jeonnam","gyeongbuk","gyeongnam", "jeju", "sejong"};


    HashMap<String, Integer> dustList = new HashMap<String, Integer>();

    boolean isAreaTag = false;
    boolean isLoadComplete = false;

    public ParserFinedust(final String url)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //uri에 저장된 사이트에 접속
                    URL xmlURL = new URL(url);

                    //xml데이터를 읽어서 inpuitstream에 저장
                    InputStream in = xmlURL.openStream();

                    //XmlPullParser를 사용하기 위해서
                    //xml문서를 이벤트를 이용해서 데이터를 추출해주는 객체
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(in, "UTF-8");

                    //네임스페이스 사용여부
                    //factory.setNamespaceAware(true);
                    //이벤트 저장할 변수선언
                    int eventType = xpp.getEventType();

                    String tagName="";
                    String text;

                    //xml의 데이터의 끝까지 돌면서 원하는 데이터를  얻어옴
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        switch(eventType)
                        {
                            case XmlPullParser.START_TAG:
                                tagName = xpp.getName();
                                break;

                            case XmlPullParser.TEXT:
                                for (String tag : areaStr) {
                                    if (tag.equals(tagName)) {
                                        isAreaTag = true;
                                        break;
                                    }
                                }
                                int dustValue = 0;
                                try {
                                    dustValue = Integer.parseInt(xpp.getText());
                                }
                                catch(Exception e) {
                                    isAreaTag = false;
                                }
                                if (isAreaTag) {

                                    dustList.put(tagName, dustValue);
                                    System.out.println(tagName + dustValue);

                                }
                                break;

                            case XmlPullParser.END_TAG:
                                isAreaTag = false;
                                break;
                        }
                        eventType = xpp.next(); //다음 이벤트 타입
                    }
                    isLoadComplete = true;
                    for (String area : areaStr)
                    {
                        if(dustList.get(area) == null)
                        {
                            dustList.put(area, 0);
                            System.out.println(area);
                        }
                    }
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

}
