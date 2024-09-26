package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ScrollView;
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

public class StepHistoryActivity extends AppCompatActivity {

    String stepuserkey;
    DatabaseReference ref;
    TextView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_history);

        list= findViewById(R.id.list);
        list.setMovementMethod(new ScrollingMovementMethod());

        ref = FirebaseDatabase.getInstance().getReference("user");

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stepuserkey = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref=ref.child("steps");

                    DataSnapshot stepSnap = child.child("steps");
                    Iterable<DataSnapshot>  stepChildren = stepSnap.getChildren();

                    HashMap<String,Object> map= new HashMap<>();

                    for (DataSnapshot dates : stepChildren) {
                        map.put(dates.getKey(), dates.getValue(String.class));
                    }


                    Set keys;
                    keys = map.keySet();

                    Iterator i;
                    for (i = keys.iterator(); i.hasNext();) {
                        String key = (String) i.next();
                        String value = (String) map.get(key);
                        list.append(getString(R.string.keyvalue,key,value + "\n"));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StepHistoryActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}