package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HistoryActivity extends AppCompatActivity {

    Button stephistory,sleephistory,waterhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        stephistory=findViewById(R.id.stephistory);
        waterhistory=findViewById(R.id.waterhistory);
        sleephistory=findViewById(R.id.sleephistory);

        stephistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this,StepHistoryActivity.class));
            }
        });

        waterhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, MainActivity2.class));
            }
        });

        sleephistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, SleepHistoryActivity.class));
            }
        });
    }
}