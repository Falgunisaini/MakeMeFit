package com.example.first;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class WaterActivity extends AppCompatActivity {
    public int wcount=0,goal,goalval;
    TextView numberofglasses;
    Button button_add;
    Button button_sub;
    String stringVal;
    ImageView goaltick;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("user");
    DataSnapshot dataSnapshot;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        numberofglasses = findViewById(R.id.numberofglasses);
        button_sub = findViewById(R.id.button_sub);
        button_add = findViewById(R.id.button_add);
        goaltick = findViewById(R.id.tick);

        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wcount == 0) {
                    wcount = 0;
                    Toast.makeText(WaterActivity.this, "Your current score is " + wcount + ".", Toast.LENGTH_SHORT).show();
                } else {
                    wcount--;
                    Toast.makeText(WaterActivity.this, "Your current score is " + wcount +".", Toast.LENGTH_SHORT).show();
                }
                wcount=goal;
                if (wcount >= 8) {
                    Toast.makeText(WaterActivity.this, "Daily water intake goal achieved!! Keep it up!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WaterActivity.this, "Your current score is " + wcount + ".", Toast.LENGTH_SHORT).show();
                    goaltick.setVisibility(View.VISIBLE);
                } else {
                    goaltick.setVisibility(View.INVISIBLE);
                }
                HashMap<String, Object> water = new HashMap<>();
                ref.updateChildren(water);
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wcount++;
                wcount=goal;
                Toast.makeText(WaterActivity.this, "Your current score is " + goal + ".", Toast.LENGTH_SHORT).show();
                if (wcount >= 8) {
                    Toast.makeText(WaterActivity.this, "Daily water intake goal achieved!! Keep it up!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WaterActivity.this, "Your current score is " + wcount + ".", Toast.LENGTH_SHORT).show();
                    goaltick.setVisibility(View.VISIBLE);
                } else {
                    goaltick.setVisibility(View.INVISIBLE);
                }
                HashMap<String, Object> water = new HashMap<>();
                ref.updateChildren(water);
            }
        });

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stringVal = child.getKey();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());
                    HashMap<String, Object> water = new HashMap<>();
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    water.put("water/" + formattedDate, wcount);
                    ref.updateChildren(water);

                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref = ref.child("water");
                    DataSnapshot sleepSnap = child.child("water");
                    Iterable<DataSnapshot> sleepChildren = sleepSnap.getChildren();
                    for (DataSnapshot dates : sleepChildren) {
                        String key = dates.getKey();
                        if (key.equals(formattedDate)) {
                            goalval = (Integer.parseInt(dates.getValue(String.class)));
                            goalval=wcount;
                            Toast.makeText(WaterActivity.this, "Your current score is" + goalval, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                    Toast.makeText(WaterActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
