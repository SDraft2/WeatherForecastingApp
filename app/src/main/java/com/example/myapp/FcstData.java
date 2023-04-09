package com.example.myapp;

public class FcstData {
    public final static int AM = 0;
    public final static int PM = 1;

    private String date;

    private int[] SKY = new int[2];
    private int[] PTY = new int[2];
    private int[] WSD = new int[2];

    private double TMN;
    private double TMX;;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSKY(int state, int value) {
        return SKY[state];
    }

    public void setSKY(int state, int value) {
        SKY[state] = value;
    }

    public int getPTY(int state, int value) {
        return PTY[state];
    }

    public void setPTY(int state, int value) {
        PTY[state] = value;
    }

    public int getWSD(int state, int value) {
        return WSD[state];
    }

    public void setWSD(int state, int value) {
        WSD[state] = value;
    }

    public Double getTMN() {
        return TMN;
    }

    public void setTMN(Double TMN) {
        this.TMN = TMN;
    }

    public Double getTMX() {
        return TMX;
    }

    public void setTMX(Double TMX) {
        this.TMX = TMX;
    }

    public String getSkyStr(int state)
    {
        switch(SKY[state])
        {
            case 1:
                return "맑음";
            case 2:
                return "구름조금";
            case 3:
                return "구름많음";
            case 4:
                return "흐림";
            default:
                return "ERROR";
        }
    }
    public int getSkyImg(int state)
    {
        switch(SKY[state])
        {
            case 1:
                return R.drawable.ic_sun;
            case 2:
                return R.drawable.ic_cloudfew;
            case 3:
                return R.drawable.ic_cloudbroken;
            case 4:
                return R.drawable.ic_cloudy;
            default:
                return R.drawable.ic_sun;
        }
    }

    public String getPtyStr(int state)
    {
        switch(PTY[state])
        {
            case 0:
                return "맑음";
            case 1:
                return "비";
            case 2:
                return "진눈깨비";
            case 3:
                return "눈";
            case 4:
                return "소나기";
            default:
                return "ERROR";
        }
    }
    public int getPtyImg(int state)
    {
        switch(PTY[state])
        {
            case 0:
                return R.drawable.ic_sun;
            case 1:
                return R.drawable.ic_rain;
            case 2:
                return R.drawable.ic_sleet;
            case 3:
                return R.drawable.ic_snow;
            case 4:
                return R.drawable.ic_shower;
            default:
                return 0;
        }
    }

    public String getWsdStr(int state)
    {
        String str;

        if(WSD[state] <= 1)
            str = "매우 약함";
        else if(WSD[state] <= 4)
            str = "약함";
        else if(WSD[state] <= 8)
            str = "바람 다소";
        else if(WSD[state] <= 12)
            str = "다소강함";
        else if(WSD[state] < 18)
            str = "강함";
        else if(WSD[state] >= 18)
            str = "매우강함";
        else
            str = "ERROR";

        return WSD[state]+"m/s\n" + str;
    }
}
