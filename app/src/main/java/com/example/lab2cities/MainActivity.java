package com.example.lab2cities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    PlaceAdapter adapter;
    private List<Place> placeList = new ArrayList<>();
    private List<Place> currentList = new ArrayList<>();
    Toolbar toolbar;
    Spinner spinner;
    TextView searchText;
    EditText filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.spinner);
        searchText = findViewById(R.id.searchTextView);
        filterText = findViewById(R.id.searchText);

        DataLoader.loadDataList(datatList -> {
            // Обновляем RecyclerView из основного потока
            runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new PlaceAdapter(this, datatList);
                placeList.clear();
                placeList.addAll(datatList);
                recyclerView.setAdapter(adapter);
                // Установка слушателя кликов
                adapter.setOnItemClickListener(place -> {
                    Intent i = new Intent(this, PlaceDetailsActivity.class);
                    i.putExtra("Idx", place.getPlace_id());
                    startActivity(i);
                });
            });
        });

        // Инициализируем меню
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Справочник");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Инфлейтим меню; добавляем элементы в панель действий, если она присутствует.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_favorite){
            currentList = IntStream.range(0, placeList.size()).filter(i -> placeList.get(i).isIs_favorite()).mapToObj(i -> placeList.get(i)).collect(Collectors.toList());
            adapter.updateList(currentList);
            toolbar.setSubtitle("Избранное");
            spinner.setVisibility(View.GONE);
            searchText.setVisibility(View.GONE);
            return true;
        }else if(id == R.id.item_all){
            adapter.updateList(placeList);
            toolbar.setSubtitle("");
            spinner.setVisibility(View.GONE);
            searchText.setVisibility(View.GONE);
            filterText.setVisibility(View.GONE);
            return true;
        } else if(id == R.id.item_city){
            spinner.setVisibility(View.VISIBLE);
            searchText.setVisibility(View.VISIBLE);
            filterText.setVisibility(View.GONE);
            ArrayAdapter<String> adapterSpinner = getStringCitiesAdapter();
            // Устанавливаем адаптер для Spinner
            spinner.setAdapter(adapterSpinner);
            searchText.setText("Выбор города");
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int postion, long arg3) {
                    String city = parent.getItemAtPosition(postion).toString();
                    currentList = IntStream.range(0, placeList.size()).filter(i -> Objects.equals(placeList.get(i).getCity(), city)).mapToObj(i -> placeList.get(i)).collect(Collectors.toList());
                    adapter.updateList(currentList);
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            return true;
        }else if(id == R.id.item_category){
            filterText.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            searchText.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapterSpinner = getStringCategoriesAdapter();
            // Устанавливаем адаптер для Spinner
            spinner.setAdapter(adapterSpinner);
            searchText.setText("Выбор категории");
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int postion, long arg3) {
                    String category = parent.getItemAtPosition(postion).toString();
                    currentList = IntStream.range(0, placeList.size()).filter(i -> Objects.equals(placeList.get(i).getCategory(), category)).mapToObj(i -> placeList.get(i)).collect(Collectors.toList());
                    adapter.updateList(currentList);
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            return true;
        }else if(id == R.id.item_filter){
            filterText.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            searchText.setVisibility(View.GONE);
            filterText.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s != "")
                            {
                                currentList = filterPlaces(placeList, s.toString());
                                adapter.updateList(currentList);
                            }
                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                        }
                    }
            );
            return true;
        }
        return false;
    }

    List<Place> filterPlaces(List<Place> places, String query) {
        List<Place> filteredPlaces = new ArrayList<>();

        for (Place place : places) {
            if (place.getCity().contains(query) ||
                    place.getCategory().contains(query) ||
                    place.getName().contains(query) ||
                    place.getDescription().contains(query) ||
                    place.getAddress().contains(query) ||
                    place.getPhone_number().contains(query) ||
                    place.getWebsite().contains(query)) {
                filteredPlaces.add(place);
            }
        }

        return filteredPlaces;
    }

    @NonNull
    private ArrayAdapter<String> getStringCitiesAdapter() {
        List<String> cities = new ArrayList<>();
        for (Place place : placeList)
            cities.add(place.getCity());
        Set<String> uniqueCities = new HashSet<>(cities);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, new ArrayList<>(uniqueCities));
        // Устанавливаем внешний вид выпадающего списка
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapterSpinner;
    }

    @NonNull
    private ArrayAdapter<String> getStringCategoriesAdapter() {
        List<String> categories = new ArrayList<>();
        for (Place place : placeList)
            categories.add(place.getCategory());
        Set<String> uniqueCategories = new HashSet<>(categories);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, new ArrayList<>(uniqueCategories));
        // Устанавливаем внешний вид выпадающего списка
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapterSpinner;
    }

    @Override
    public void onResume(){
        super.onResume();
        DataLoader.loadDataList(datatList -> {
            // Обновляем RecyclerView из основного потока
            runOnUiThread(() -> {
                if(adapter == null)
                    return;
                if(currentList.isEmpty()) {
                    currentList.addAll(datatList);
                } else if(currentList.stream().allMatch(Place::isIs_favorite)){
                    List<Place> takenList = new ArrayList<>();
                    for (Place place : currentList) {
                        for (Place pl : datatList) {
                            if (pl.getPlace_id() == place.getPlace_id()) {
                                place.setIs_favorite(pl.isIs_favorite());
                                if(place.isIs_favorite())
                                    takenList.add(place);
                            }
                        }
                    }
                    currentList = takenList;
                }else{
                    currentList.clear();
                    currentList.addAll(datatList);
                }
                if(spinner.getVisibility() == View.VISIBLE) {
                    TextView textView = (TextView) spinner.getSelectedView();
                    String value = textView.getText().toString();
                    currentList = IntStream.range(0, placeList.size()).filter(i -> Objects.equals(placeList.get(i).getCity(), value) || Objects.equals(placeList.get(i).getCategory(), value)).mapToObj(i -> placeList.get(i)).collect(Collectors.toList());
                    adapter.updateList(currentList);
                }
                if(filterText.getVisibility() == View.VISIBLE){
                    currentList = filterPlaces(placeList, filterText.getText().toString());
                    adapter.updateList(currentList);
                }
                adapter.updateList(currentList);
            });
        });
    }
}