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

public class WaterHistoryActivity extends AppCompatActivity {

    String wateruserkey;
    DatabaseReference ref;
    TextView list_water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_history);

        list_water = findViewById(R.id.list_water);
        list_water.setMovementMethod(new ScrollingMovementMethod());

        ref = FirebaseDatabase.getInstance().getReference("user");

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    wateruserkey = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref=ref.child("water");

                    DataSnapshot waterSnap = child.child("water");
                    Iterable<DataSnapshot>  waterChildren = waterSnap.getChildren();

                    HashMap<String,Object> watermap= new HashMap<>();

                    for (DataSnapshot dates : waterChildren) {
                        watermap.put(dates.getKey(), dates.getValue(Integer.class));
                    }


                    Set keys;
                    keys = watermap.keySet();

                    Iterator i;
                    for (i = keys.iterator(); i.hasNext();) {
                        String key = (String) i.next();
                        Integer value = (Integer) watermap.get(key);
                        list_water.append(getString(R.string.keyvalue,key,value + "\n"));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WaterHistoryActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}