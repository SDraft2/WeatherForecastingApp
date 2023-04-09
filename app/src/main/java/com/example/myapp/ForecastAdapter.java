package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    ArrayList<FcstData> items = new ArrayList<FcstData>();

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_forecast, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FcstData item = items.get(position);
        viewHolder.SetItem(item);
    }

    @Override
    public int getItemCount() {
        System.out.println(items.size());
        return items.size();
    }

    public void addItem(FcstData item)
    {
        items.add(item);
    }
    public void addItem(int idx, FcstData item)
    {
        items.add(idx, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTxt;

        ImageView ptyAMImg;
        ImageView ptyPMImg;
        ImageView skyAMImg;
        ImageView skyPMImg;

        TextView ptyAMTxt;
        TextView ptyPMTxt;
        TextView skyAMTxt;
        TextView skyPMTxt;
        TextView wsdAMTxt;
        TextView wsdPMTxt;

        TextView tprMinTxt;
        TextView tprMaxxt;



        public ViewHolder(View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.foreList_dateTxt);

            ptyAMImg = itemView.findViewById(R.id.foreList_ptyAMImg);
            ptyPMImg = itemView.findViewById(R.id.foreList_ptyPMImg);
            skyAMImg =  itemView.findViewById(R.id.foreList_skyAMImg);
            skyPMImg =  itemView.findViewById(R.id.foreList_skyPMImg);

            ptyAMTxt = itemView.findViewById(R.id.foreList_ptyAMTxt);
            ptyPMTxt = itemView.findViewById(R.id.foreList_ptyPMTxt);
            skyAMTxt = itemView.findViewById(R.id.foreList_skyAMTxt);
            skyPMTxt = itemView.findViewById(R.id.foreList_skyPMTxt);
            wsdAMTxt = itemView.findViewById(R.id.foreList_wsdAMTxt);
            wsdPMTxt = itemView.findViewById(R.id.foreList_wsdPMTxt);

            tprMinTxt = itemView.findViewById(R.id.foreList_tprMinTxt);
            tprMaxxt = itemView.findViewById(R.id.foreList_tprMaxTxt);
        }

        public void SetItem(FcstData item)
        {
            dateTxt.setText(item.getDate());

            ptyAMImg.setImageResource(item.getPtyImg(FcstData.AM));
            ptyPMImg.setImageResource(item.getPtyImg(FcstData.PM));
            skyAMImg.setImageResource(item.getSkyImg(FcstData.AM));
            skyPMImg.setImageResource(item.getSkyImg(FcstData.PM));

            ptyAMTxt.setText(item.getPtyStr(FcstData.AM));
            ptyPMTxt.setText(item.getPtyStr(FcstData.PM));
            skyAMTxt.setText(item.getSkyStr(FcstData.AM));
            skyPMTxt.setText(item.getSkyStr(FcstData.PM));
            wsdAMTxt.setText(item.getWsdStr(FcstData.AM));
            wsdPMTxt.setText(item.getWsdStr(FcstData.PM));
            tprMinTxt.setText(item.getTMN()+"℃");
            tprMaxxt.setText(item.getTMX()+"℃");
        }
    }
}
