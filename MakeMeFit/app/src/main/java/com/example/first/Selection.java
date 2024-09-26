package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Selection extends AppCompatActivity {

    Button profile,meditate,update,history,stats,sleep,water,change,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        profile=findViewById(R.id.profile);
        meditate=findViewById(R.id.meditate);
        update=findViewById(R.id.update);
        history=findViewById(R.id.history);
        stats=findViewById(R.id.stats);
        sleep=findViewById(R.id.sleep);
        water=findViewById(R.id.water);
        change=findViewById(R.id.changepass);
        logout = findViewById(R.id.logout);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, Homepage.class));
            }
            });

        meditate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, MeditateActivity.class));
            }
            });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, UpdateProfile.class));
            }
            });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, HistoryActivity.class));
            }
            });


        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, StepStatisticsActivity.class));
            }
            });


        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, SleepActivity.class));
            }
            });


         water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, WaterActivity.class));
            }
            });

         change.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(Selection.this,ResetPasswordActivity.class));
             }
         });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Selection.this, MainActivity2.class));
            }
        });
    }
}
