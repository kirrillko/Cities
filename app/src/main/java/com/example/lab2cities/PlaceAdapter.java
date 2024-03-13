package com.example.lab2cities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private Context context;
    private List<Place> placeList;
    private OnItemClickListener listener;

    // Интерфейс для обработки кликов
    public interface OnItemClickListener {
        void onItemClick(Place place);
    }

    // Метод для установки слушателя
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Конструктор
    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.bind(place);
    }

    public void updateList(List<Place> places) {
        placeList.clear();
        placeList.addAll(places);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    // Внутренний класс ViewHolder
    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView cityTextView;
        TextView categoryTextView;
        TextView nameTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);

            // Устанавливаем обработчик кликов
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(placeList.get(position));
                    }
                }
            });
        }

        // Привязываем данные к View
        public void bind(Place place) {
            cityTextView.setText(place.getCity());
            categoryTextView.setText(place.getCategory());
            nameTextView.setText(place.getName());
        }
    }
}