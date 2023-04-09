package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.myapp.MainActivity.requestQueue;

public class FragmentForecast extends Fragment {

   RecyclerView recyclerView;
   ForecastAdapter adapter;

   ParserForecast dataForecast;

    String baseDate;
    String baseTime = "0500";
    String jsonStr;

    SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forecast, container, false);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        recyclerView = v.findViewById(R.id.forecast_recyclerView);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new ForecastAdapter();

        recyclerView.setAdapter(adapter);

        return v;
    }

    public void ParseJson(int nx, int ny)
    {
        dateFormat = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA);
        baseDate = dateFormat.format(System.currentTimeMillis() - (5 * 60 * 60 * 1000));

        String url = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?serviceKey=" + MainActivity.SERVICE_KEY + "&base_date=" + baseDate + "&base_time=" + baseTime + "&nx=" + nx+ "&ny=" + ny+ "&numOfRows=1000&pageNo=1&_type=json";

        System.out.println(url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        jsonStr = response;
                        dataForecast = new ParserForecast(jsonStr);
                        SetView(dataForecast);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("에러 : " + error.getMessage() + "\n");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void SetView(ParserForecast item)
    {
        if(item != null)
        {
            adapter.addItem(item.getTomorrowData());
            adapter.addItem(item.getTwoDayAgoData());
            adapter.notifyDataSetChanged();
        }
    }
}
