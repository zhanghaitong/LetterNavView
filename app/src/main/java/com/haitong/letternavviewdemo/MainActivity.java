package com.haitong.letternavviewdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.haitong.letternavview.view.LetterNavView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LetterNavView letterNavView = findViewById(R.id.letter_container);
        letterNavView.setNavTopTextList(Arrays.asList("定位", "热门"));
        letterNavView.setNavBottomTextList(Arrays.asList("#"));
        letterNavView.setLetterChangeListener((index, text) -> {

        });
    }
}