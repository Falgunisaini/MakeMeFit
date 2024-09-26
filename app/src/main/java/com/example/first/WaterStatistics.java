package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaterStatistics extends AppCompatActivity {

    String wateruserkey,key;
    DatabaseReference ref;
    int sum = 0,max=0,c=0,weekly=0,weekcount=7;
    TextView tot,avg,highest,week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_statistics);
        sum=0; max=0; weekly=0;weekcount=7;

        tot= findViewById(R.id.watertot);
        avg= findViewById(R.id.wateravg);
        week= findViewById(R.id.weekavgwater);
        highest= findViewById(R.id.maxwater);

        ref = FirebaseDatabase.getInstance().getReference("user");

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    wateruserkey = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref = ref.child("water");

                    DataSnapshot stepSnap = child.child("water");
                    Iterable<DataSnapshot> stepChildren = stepSnap.getChildren();


                    for (DataSnapshot dates : stepChildren) {
                        sum += dates.getValue(Integer.class);

                        if (max < dates.getValue(Integer.class)) {
                            max = dates.getValue(Integer.class);
                            key = dates.getKey();
                        }
                        if (c < 8) {
                            weekly += dates.getValue(Integer.class);
                            c++;
                        }
                    }

                    if (c < 7)
                        weekcount = c;

                    tot.setText(getString(R.string.totalwaterintake, sum));
                    highest.setText(getString(R.string.maxwaterintake, max, key));
                    avg.setText(getString(R.string.averagewater, sum / c));
                    week.setText(getString(R.string.averagewaterweekly, weekly / weekcount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaterStatistics.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}