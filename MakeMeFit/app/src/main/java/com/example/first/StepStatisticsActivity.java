package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class StepStatisticsActivity extends AppCompatActivity {

    String stepuserkey,key;
    DatabaseReference ref;
    int sum = 0,max=0,c=0,weeklysteps=0,weekcount=7;
    TextView tot,avg,highest,week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_statistics);


        sum=0; max=0; weeklysteps=0;weekcount=7;
        tot = findViewById(R.id.tot);
        avg = findViewById(R.id.avg);
        week = findViewById(R.id.weekavgsteps);
        highest = findViewById(R.id.higheststeps);

        ref = FirebaseDatabase.getInstance().getReference("user");

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stepuserkey = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref = ref.child("steps");

                    DataSnapshot stepSnap = child.child("steps");
                    Iterable<DataSnapshot> stepChildren = stepSnap.getChildren();


                    for (DataSnapshot dates : stepChildren) {
                        sum += (Integer.valueOf(dates.getValue(String.class)));

                        if (max < Integer.valueOf(dates.getValue(String.class))) {
                            max = Integer.valueOf(dates.getValue(String.class));
                            key = dates.getKey();
                        }
                        if (c < 8) {
                            weeklysteps += (Integer.valueOf(dates.getValue(String.class)));
                            c++;
                        }
                    }

                    if (c < 7)
                        weekcount = c;


                    tot.setText(getString(R.string.totalsteps, sum));
                    highest.setText(getString(R.string.higheststeps, max, key));
                    avg.setText(getString(R.string.averagesteps, sum / c));
                    week.setText(getString(R.string.averagestepsweekly, weeklysteps / weekcount));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StepStatisticsActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}