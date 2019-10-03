package com.haitong.letternavviewdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.haitong.letternavview.view.LetterNavView;
import com.haitong.letternavviewdemo.adapter.CityListAdapter;
import com.haitong.letternavviewdemo.db.DBHelper;
import com.haitong.letternavviewdemo.db.DatabaseHelper;
import com.haitong.letternavviewdemo.entity.City;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityAll extends AppCompatActivity implements LetterNavView.LetterChangeListener {

    private ListView listView;
    private LetterNavView letterNavView;

    private List<City> allCities = new ArrayList<>();
    private List<City> hotCities = new ArrayList<>();
    private List<String> historyCities = new ArrayList<>();
    private List<City> citiesData;
    private Map<String, Integer> letterIndex = new HashMap<>();
    private CityListAdapter cityListAdapter;

    private DatabaseHelper databaseHelper;

    private boolean isOverlayReady;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView = findViewById(R.id.city_container);
        letterNavView = findViewById(R.id.letter_container);

        letterNavView.setNavTopTextList(Arrays.asList("定位", "热门", "最近", "全部"));
        letterNavView.setNavBottomTextList(Arrays.asList("#"));

        databaseHelper = new DatabaseHelper(this);
        handler = new Handler();
        setupActionBar();

        initCity();
        initHotCity();
        initHistoryCity();
        setupView();
//        initOverlay();
    }

    private void initCity() {
        City city = new City("定位", "0"); // 当前定位城市
        allCities.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCities.add(city);
        city = new City("热门", "2"); // 热门城市
        allCities.add(city);
        city = new City("全部", "3"); // 全部城市
        allCities.add(city);
        citiesData = getCityList();
//        citiesData = getCityList();
//        citiesData = new ArrayList<>();
//        allcie
        allCities.addAll(citiesData);
    }

    /**
     * 热门城市
     */
    public void initHotCity() {
        City city = new City("北京", "2");
        hotCities.add(city);
        city = new City("上海", "2");
        hotCities.add(city);
        city = new City("广州", "2");
        hotCities.add(city);
        city = new City("深圳", "2");
        hotCities.add(city);
        city = new City("武汉", "2");
        hotCities.add(city);
        city = new City("天津", "2");
        hotCities.add(city);
        city = new City("西安", "2");
        hotCities.add(city);
        city = new City("南京", "2");
        hotCities.add(city);
        city = new City("杭州", "2");
        hotCities.add(city);
        city = new City("成都", "2");
        hotCities.add(city);
        city = new City("重庆", "2");
        hotCities.add(city);
    }

    private void initHistoryCity() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recent_city order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            historyCities.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    public void addHistoryCity(String name) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recent_city where name = '" + name + "'", null);
        if (cursor.getCount() > 0) {
            db.delete("recent_city", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recent_city(name, date) values('" + name + "', " + System.currentTimeMillis() + ")");
        db.close();
    }


    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;
    }


    /**
     * a-z排序
     */
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    private void setupView() {
        letterNavView.setLetterChangeListener(this);

        cityListAdapter = new CityListAdapter(this, allCities, hotCities, historyCities, letterIndex);
        listView.setAdapter(cityListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivityAll.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActionBar() {
    }

    @Override
    public void onTouchingLetterChanged(int index, String s) {
        if (letterIndex.get(s) != null) {
            int position = letterIndex.get(s);
            listView.setSelection(position);
        }
    }
}
