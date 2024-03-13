package com.example.lab2cities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class PlaceDetailsActivity extends AppCompatActivity {
    private TextView placeNameTextView;
    private TextView placeDescriptionTextView;
    private TextView placeAddressTextView;
    private TextView placePhoneNumberTextView;
    private TextView placeWebsiteTextView;
    private TextView placeCoordinatesTextView;
    private TextView favoriteTextView;

    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int idx = extras.getInt("Idx");
        DataLoader.loadData(idx, place -> {
            runOnUiThread(() -> {
                // Получение данных о месте из Intent
                int placeID = place.getPlace_id();
                String city = place.getCity();
                String category = place.getCategory();
                String name = place.getName();
                String description = place.getDescription();
                String address = place.getAddress();
                String phoneNumber = place.getPhone_number();
                String website = place.getWebsite();
                double latitude = place.getLatitude();
                double longitude = place.getLongitude();
                isFavorite = place.isIs_favorite();

                // Настройка отображения данных на экране
                placeNameTextView = findViewById(R.id.placeNameTextView);
                placeDescriptionTextView = findViewById(R.id.placeDescriptionTextView);
                placeAddressTextView = findViewById(R.id.placeAddressTextView);
                placePhoneNumberTextView = findViewById(R.id.placePhoneNumberTextView);
                placeWebsiteTextView = findViewById(R.id.placeWebsiteTextView);
                placeCoordinatesTextView = findViewById(R.id.placeCoordinatesTextView);
                favoriteTextView = findViewById(R.id.favoriteTextView);

                placeNameTextView.setText(name);
                placeDescriptionTextView.setText(description);
                placeAddressTextView.setText(address);
                placePhoneNumberTextView.setText(phoneNumber);
                placeWebsiteTextView.setText(website);
                placeCoordinatesTextView.setText("Широта: " + latitude + ", долгота: " + longitude);

                // Установка звездочки вместо isFavorite (здесь вы можете использовать вашу логику)
                updateFavoriteStatus(isFavorite);

                // Установка обработчика нажатия на звездочку
                favoriteTextView.setOnClickListener(v -> {
                    isFavorite = !isFavorite; // инвертировать состояние избранного
                    place.setIs_favorite(isFavorite);
                    DataLoader.updateData(place);
                    updateFavoriteStatus(isFavorite);
                    // Показать сообщение о состоянии избранного
                    String message = isFavorite ? "Добавлено в избранное" : "Удалено из избранного";
                    Toast.makeText(PlaceDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                });

                Toolbar toolbar = findViewById(R.id.toolbar_details);
                toolbar.setTitle(city);
                toolbar.setSubtitle(category);
                setSupportActionBar(toolbar);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(v -> {
                    onBackPressed();
                    finish();
                });
            });
        });
    }

    // Метод для обновления отображения звездочки
    private void updateFavoriteStatus(boolean isFavorite) {
        String favoriteStatus = isFavorite ? "★" : "☆";
        favoriteTextView.setText(favoriteStatus);
    }
}