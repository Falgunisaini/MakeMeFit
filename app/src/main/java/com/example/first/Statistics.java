package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Statistics extends AppCompatActivity {

    Button stepstats,sleepstats,waterstats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        stepstats = findViewById(R.id.stepstatsclick);
        sleepstats= findViewById(R.id.Sleepstatsclick);
        waterstats= findViewById(R.id.waterstatsclick);

        stepstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Statistics.this,StepStatisticsActivity.class));
            }
        });

        waterstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Statistics.this, WaterStatistics.class));
            }
        });

        sleepstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Statistics.this, SleepStatistics.class));
            }
        });
    }
}