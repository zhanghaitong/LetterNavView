package com.haitong.letternavviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.haitong.letternavview.view.LetterNavView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LetterNavView letterNavView = findViewById(R.id.letter_container);
        letterNavView.setOnTouchingLetterChangedListener(new LetterNavView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}