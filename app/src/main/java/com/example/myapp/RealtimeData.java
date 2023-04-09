package com.example.myapp;

public class RealtimeData {
    Double T1H;
    Double RN1;
    Double UUU;
    Double VVV;
    Double REH;
    int PTY;
    Double VEC;
    Double WSD;

    public Double getT1H() {
        return T1H;
    }

    public void setT1H(Double t1H) {
        T1H = t1H;
    }

    public Double getRN1() {
        return RN1;
    }

    public void setRN1(Double RN1) {
        this.RN1 = RN1;
    }

    public Double getUUU() {
        return UUU;
    }

    public void setUUU(Double UUU) {
        this.UUU = UUU;
    }

    public Double getVVV() {
        return VVV;
    }

    public void setVVV(Double VVV) {
        this.VVV = VVV;
    }

    public Double getREH() {
        return REH;
    }

    public void setREH(Double REH) {
        this.REH = REH;
    }

    public int getPTY() {
        return PTY;
    }

    public void setPTY(int PTY) {
        this.PTY = PTY;
    }

    public Double getVEC() {
        return VEC;
    }

    public void setVEC(Double VEC) {
        this.VEC = VEC;
    }

    public Double getWSD() {
        return WSD;
    }

    public void setWSD(Double WSD) {
        this.WSD = WSD;
    }

    public String getPrecipStr()
    {
        switch(PTY)
        {
            case 0:
                return "맑음";
            case 1:
                return "비";
            case 2:
                return "비/눈";
            case 3:
                return "눈";
            case 4:
                return "소나기";
            default:
                return "ERROR";
        }
    }
    public int getPrecipImg()
    {
        switch(PTY)
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
    public String getTptStr()
    {
        return "현재온도 " + T1H + "℃";
    }

    public String getWsdStr()
    {
        String str;

        if(WSD <= 1)
            str = "(매우 약함)";
        else if(WSD <= 4)
            str = "(약함)";
        else if(WSD <= 8)
            str = "(바람 다소)";
        else if(WSD <= 12)
            str = "(다소강함)";
        else if(WSD < 18)
            str = "(강함)";
        else if(WSD >= 18)
            str = "(매우강함)";
        else
            str = "ERROR";

        return WSD + "m/s " + str;
    }
}
