package com.example.myapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class DustMapConverter {
    static final String TYPE_PM10 = "PM10";
    static final String TYPE_PM25 = "PM25";

    String type;

    AssetManager am;
    InputStream is;
    Bitmap bm;
    Bitmap emptyBm;
    int mWidth;
    int mHeight;

    HashMap<String, Integer> dustList = new HashMap<String, Integer>();

    public DustMapConverter(Context context, HashMap<String, Integer> dustList, String type)
    {
        this.dustList = dustList;
        this.type = type;

        am = context.getResources().getAssets();
        System.out.println(am);
        try
        {
            is = am.open("krm.png");

            bm = BitmapFactory.decodeStream(is);

            int[] colors = createColors();
            emptyBm = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            is.close();

            int pixel;
            int a;
            int r;
            int g;
            int b;
            for (int x = 0; x < bm.getWidth(); x++) {
                for (int y = 0; y < bm.getHeight(); y++) {
                    pixel = bm.getPixel(x,y);

                    r = Color.red(pixel);
                    b = Color.blue(pixel);
                    a = Color.alpha(pixel);
                    g = Color.green(pixel);

                /*
                ulsan 0
                busan 1
                daegu 2
                gyeungnam 3
                gyeongbuk4
                    <color name="gwangju">#FF05FF</color>
                    <color name="jeonnam">#FF06FF</color>
                    <color name="jeonbuk">#FF07FF</color>
                    <color name="daejun">#FF08FF</color>
                    <color name="sejong">#FF09FF</color>
                    <color name="chungnam">#FF10FF</color>
                    <color name="chungbuk">#FF11FF</color>
                    <color name="gangwon">#FF12FF</color>
                    <color name="seoul">#FF13FF</color>
                    <color name="incheon">#FF14FF</color>
                    <color name="gyeonggi">#FF15FF</color>
                    <color name="jeju">#FF16FF</color>
                    <color name="northKorea">#FF17FF</color>
                            */
                    if(r == 255 && b == 255)
                    {
                        try{
                            switch(g)
                            {
                                case 0:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("ulsan")));
                                    break;

                                case 1:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("busan")));
                                    break;

                                case 2:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("daegu")));
                                    break;

                                case 3:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("gyeongnam")));
                                    break;
                                case 4:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("gyeongbuk")));
                                    break;
                                case 5:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("gwangju")));
                                    break;
                                case 6:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("jeonnam")));
                                    break;
                                case 7:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("jeonbuk")));
                                    break;
                                case 8:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("daejeon")));
                                    break;
                                case 9:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("sejong")));
                                    break;
                                case 10:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("chungnam")));
                                    break;
                                case 11:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("chungbuk")));
                                    break;
                                case 12:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("gangwon")));
                                    break;
                                case 13:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("seoul")));
                                    break;
                                case 14:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("incheon")));
                                    break;
                                case 15:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("gyeonggi")));
                                    break;
                                case 16:
                                    emptyBm.setPixel(x, y, dustStateColor(dustList.get("jeju")));
                                    break;
                                case 17:
                                    emptyBm.setPixel(x, y, Color.argb(a, r, g, b));
                                default:
                                    emptyBm.setPixel(x, y, Color.argb(a, r, 255, b));
                            }
                        }
                        catch(Exception e){
                            emptyBm.setPixel(x, y, Color.rgb(150, 250, 250));
                        }
                    }
                    else
                        emptyBm.setPixel(x, y, Color.argb(a, 0, 0, 0));

                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap()
    {
        if(emptyBm != null)
            return emptyBm;

        return null;
    }

    private int[] createColors(){
        int[] colors = null;

        mWidth = bm.getWidth();
        mHeight = bm.getHeight();

        colors = new int[mWidth * mHeight];
        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                colors[y * mWidth + x] = bm.getPixel(x, y);
            }
        }

        return colors;
    }

    public int dustStateColor(int value)
    {
        if(type.equals(TYPE_PM10))
        {
            if(value <= 30)
                return MainActivity.DUST_COLOR_GOOD;
            else if(value > 30 && value <= 90)
                return MainActivity.DUST_COLOR_NORMAL;
            else if(value > 90 && value <= 150)
                return MainActivity.DUST_COLOR_BAD;
            else if(value > 150)
                return MainActivity.DUST_COLOR_TOOBAD;
            else
                return MainActivity.DUST_COLOR_ERROR;
        }
        else if(type.equals(TYPE_PM25))
        {
            if(value <= 15)
                return MainActivity.DUST_COLOR_GOOD;
            else if(value > 15 && value <= 35)
                return MainActivity.DUST_COLOR_NORMAL;
            else if(value > 35 && value <= 75)
                return MainActivity.DUST_COLOR_BAD;
            else if(value > 75)
                return MainActivity.DUST_COLOR_TOOBAD;
            else
                return MainActivity.DUST_COLOR_ERROR;
        }
        return 0;
    }
}
