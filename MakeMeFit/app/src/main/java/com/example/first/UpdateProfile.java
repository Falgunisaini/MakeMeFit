package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UpdateProfile extends AppCompatActivity {
    Button up;
    EditText name,age,weight,height;
    String names,ages,weights,heights;
    Member mem;
    FirebaseDatabase database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        up = findViewById(R.id.updatebutton);

        name=findViewById(R.id.editTextTextPersonName);
        age=findViewById(R.id.ageid);
        weight=findViewById(R.id.editTextNumberDecimal2);
        height=findViewById(R.id.editTextNumberDecimal);

        ref = FirebaseDatabase.getInstance().getReference("user");



        //      Toast.makeText(Homepage.this, "HELOOO "+ userId+" !", Toast.LENGTH_SHORT).show();


        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float f;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //    Toast.makeText(Homepage.this,child.getKey().toString(),Toast.LENGTH_SHORT).show();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());

                    name.setText(child.child("username").getValue().toString());
                    weight.setText(child.child("weight").getValue().toString());
                    height.setText(child.child("height").getValue().toString());
                    age.setText(child.child("age").getValue().toString());

                    // Toast.makeText(Homepage.this,"BMI: "+child.child("bmi").getValue().toString(),Toast.LENGTH_SHORT).show();
                    //bmi.setText(getString(R.string.bmi_display,child.child("bmi").getValue().toString()));
                    //test.setText(getString(R.string.profile_display,child.child("username").getValue().toString()));
                    //    cg.setText(getString(R.string.cg_display,child.child("steps").getValue(),child.child("stepgoal").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mem=new Member();
                names = name.getText().toString();
                weights = weight.getText().toString();
                heights = height.getText().toString();
                ages = age.getText().toString();

                database = FirebaseDatabase.getInstance();
                ref = FirebaseDatabase.getInstance().getReference();

                if (ages.isEmpty()) {
                    age.setError("Please enter age");
                    age.requestFocus();
                }
                if (heights.isEmpty()) {
                    height.setError("Please enter height");
                    height.requestFocus();
                }
                if (weights.isEmpty()) {
                    weight.setError("Please enter weight");
                    weight.requestFocus();
                }

                ref = FirebaseDatabase.getInstance().getReference("user");
                ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //    Toast.makeText(Homepage.this,child.getKey().toString(),Toast.LENGTH_SHORT).show();

                            ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());
                            HashMap<String, Object> calorie = new HashMap<>();
                            Date c = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            Float newcal = Float.parseFloat(child.child("calories").getValue().toString());
                            newcal= newcal * Integer.parseInt(child.child("steps").child(formattedDate).getValue().toString());

                            calorie.put("currcal/" + formattedDate, newcal.toString());


                            mem.setWeight(weight.getText().toString());
                            mem.setHeight(height.getText().toString());
                            mem.setAge(age.getText().toString());
                            mem.setBmi();
                            mem.setCalories();
                            mem.setcurrcal(calorie);
                            mem.setGender(child.child("gender").getValue().toString());
                            mem.setStepgoal();

                            ref.updateChildren(calorie);

                            float newbmi=mem.getBmi();
                            int newgoal=mem.getStepgoal();

                            HashMap<String, Object> result = new HashMap<>();
                            result.put("username", name.getText().toString());
                            result.put("weight", weight.getText().toString());
                            result.put("height", height.getText().toString());
                            result.put("age", age.getText().toString());
                            result.put("bmi",newbmi);
                            result.put("stepgoal", newgoal);

                            ref.updateChildren(result);
                            Toast.makeText(UpdateProfile.this, "Profile updated!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateProfile.this,Homepage.class));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpdateProfile.this, "Failed to load!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}