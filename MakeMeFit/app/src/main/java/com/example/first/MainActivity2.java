package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main2);
        Button buttonOne = findViewById(R.id.login1);

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(LoginActivityIntent);
            }
        });
        Button buttonTwo = findViewById(R.id.reg);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegActivityIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(RegActivityIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

}