package com.example.first;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SleepActivity extends AppCompatActivity {
    Button inc,dec;
    TextView sleephours;
    String stringval;
    Member mem;
    public int goalval;
    int hours;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        //sleephours = findViewById(R.id.textView13);
        dec =  findViewById(R.id.sleepdec);
        inc = findViewById(R.id.sleepinc);
        //goaltick = (ImageView) findViewById(R.id.tick);

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hours != 0) {
                    hours--;
                    Toast.makeText(SleepActivity.this,"Your current sleep hour(s) count is " + hours + ".",Toast.LENGTH_SHORT).show();
                }
                else if(hours==0)
                {
                    hours=0;
                    Toast.makeText(SleepActivity.this,"Your sleep goal is achieved for the day!! Keep it up!",Toast.LENGTH_SHORT).show();
                }
                HashMap<String, Object> result = new HashMap<>();
                ref.updateChildren(result);
            }
        });

        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours++;
                Toast.makeText(SleepActivity.this,"Your current sleep hour(s) count is " + hours + ".",Toast.LENGTH_SHORT).show();
                //stringval = Integer.toString(hours);
                //sleephours.setText(stringval);
                HashMap<String, Object> result = new HashMap<>();
                ref.updateChildren(result);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stringval = child.getKey();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    HashMap<String, Object> result = new HashMap<>();
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    result.put("sleep/" + formattedDate, hours);
                    ref.updateChildren(result);

                    stringval = child.getKey();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref = ref.child("sleep");
                    DataSnapshot sleepSnap = child.child("sleep");
                    Iterable<DataSnapshot> sleepChildren = sleepSnap.getChildren();
                    for (DataSnapshot dates : sleepChildren) {
                        String key = dates.getKey();
                        if (key.equals(formattedDate)) {
                            goalval = (Integer.parseInt(dates.getValue(String.class)));
                            goalval=hours;
                            Toast.makeText(SleepActivity.this,"Your current sleep hour(s) count is " + hours + ".",Toast.LENGTH_SHORT).show();
                        }
                   }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SleepActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}