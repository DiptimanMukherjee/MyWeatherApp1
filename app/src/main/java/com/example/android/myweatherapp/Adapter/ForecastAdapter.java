package com.example.android.myweatherapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.myweatherapp.Model.TemperatureModel;
import com.example.android.myweatherapp.R;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final String TAG = "ForecastAdapter";

    List<TemperatureModel> forecastList;
    private Context context;

    public ForecastAdapter(List<TemperatureModel> list) {
        this.forecastList = list;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        context = parent.getContext();
        return new ForecastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        TemperatureModel content = forecastList.get(position);
        holder.day.setText(content.getDay());
        Glide.with(context).load(content.getWeatherIcon()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).placeholder(R.drawable.ic_launcher_background).dontAnimate()).into(holder.image);
        holder.name.setText(content.getWeatherName());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView day;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            day = itemView.findViewById(R.id.day);
        }
    }
}
