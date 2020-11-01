package com.example.judofeuilledecombats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        Button buttonNew = findViewById(R.id.buttonNew);
        buttonNew.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FileActivity.class)));
        /*
        Button buttonLoad = findViewById(R.id.buttonLoad);
        buttonLoad.setOnClickListener(v -> {
        });*/

    }
}