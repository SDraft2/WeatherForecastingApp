package com.example.myapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ForecastView extends RecyclerView.ViewHolder  {
    TextView textView;
    TextView textView2;

    public ForecastView(View itemView, final ForecastAdapter ap) {
        super(itemView);

        textView = itemView.findViewById(R.id.textView);
        textView2 = itemView.findViewById(R.id.foreList_tprMinTxt);


    }

    public void setItem(ParserForecast item) {

    }
}
