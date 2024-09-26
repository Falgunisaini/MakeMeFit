package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Homepage extends AppCompatActivity {

    TextView bmi, cg,sc,cal;
    ProgressBar progress;
    SeekBar sb;
    DatabaseReference ref;
    TextView test;
    ImageView tick,rf;
    private double MagnitudePrevious = 0;
    Integer stepCount = 0,goldsteps=0,goal=0,tot=0;
    Integer currstep;
    String stepuserkey;
    int flaggoal=0;
    Float newcal= Float.valueOf(0),stepcal=Float.valueOf(0);
    float temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        bmi = findViewById(R.id.bmi);
        test = findViewById(R.id.testing);
        cg = findViewById(R.id.cg);
        test.setText(R.string.loading);
        bmi.setText(R.string.loading);
        progress = findViewById(R.id.steps);
        sb = findViewById(R.id.seekBar3);
        cal= findViewById(R.id.cal);
        sc=findViewById(R.id.autostep);
        ref = FirebaseDatabase.getInstance().getReference("user");
        tick=findViewById(R.id.goaltick);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];


                    double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta > 7.7) {
                        stepCount++;
                    }
                    sc.setText(stepCount.toString());

                    tot= goldsteps + stepCount;
                    cg.setText(getString(R.string.cg_display, tot.toString(), goal.toString()));
                    sb.setProgress(tot);
                    temp= tot * stepcal;
                    cal.setText(getString(R.string.cals,Float.toString(temp)));
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);



        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stepuserkey = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());
                    HashMap<String, Object> result = new HashMap<>();
                    HashMap<String, Object> calorie = new HashMap<>();

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);

                   if(!child.hasChild("steps/"+formattedDate))
                    { result.put("steps/" + formattedDate,"0");
                        ref.updateChildren(result);
                        calorie.put("currcal/" + formattedDate,"0" );
                        ref.updateChildren(calorie);
                    }

                 // Toast.makeText(Homepage.this,(child.child("steps").child(formattedDate).getValue().toString()),Toast.LENGTH_SHORT).show();
                    Integer newsteps=Integer.parseInt(child.child("steps").child(formattedDate).getValue().toString());

                    // Toast.makeText(Homepage.this,formattedDate,Toast.LENGTH_SHORT).show();
                    bmi.setText(getString(R.string.bmi_display, child.child("bmi").getValue().toString()));
                    test.setText(getString(R.string.profile_display, child.child("username").getValue().toString()));
                    goal=Integer.parseInt(child.child("stepgoal").getValue().toString());
                    goldsteps=Integer.parseInt(child.child("steps").child(formattedDate).getValue().toString());
                    stepcal=Float.parseFloat(child.child("calories").getValue().toString());


//                    Toast.makeText(Homepage.this,ServerValue.TIMESTAMP.toString(),Toast.LENGTH_SHORT).show();

                    if(stepCount!=0) {
                        int oldsteps = Integer.parseInt(child.child("steps").child(formattedDate).getValue().toString());
                        DecimalFormat deci = new DecimalFormat("#.###");
                        deci.format(child.child("calories").getValue());
                        newsteps = oldsteps + stepCount;
                        newcal = Float.parseFloat(child.child("calories").getValue().toString());
                        newcal = Float.parseFloat(child.child("calories").getValue().toString()) * newsteps;

                        sb.setProgress(Integer.parseInt(newsteps.toString()));

                        result.put("steps/" + formattedDate, newsteps.toString());
                        calorie.put("currcal/" + formattedDate, newcal.toString());
                        ref.updateChildren(result);
                        ref.updateChildren(calorie);
                        cal.setText(getString(R.string.cals,child.child("currcal").child(formattedDate).getValue().toString()));
                        stepCount=0;}

                    if(newsteps<Integer.parseInt(child.child("stepgoal").getValue().toString()))
                    {    sb.setProgress(Integer.parseInt((newsteps.toString())));
                         cg.setText(getString(R.string.cg_display, child.child("steps").child(formattedDate).getValue().toString(), child.child("stepgoal").getValue()));
                        cal.setText(getString(R.string.cals,child.child("currcal").child(formattedDate).getValue().toString()));
                         sb.setMax(Integer.parseInt(child.child("stepgoal").getValue().toString())); }
                    else
                    {
                        sb.setProgress(Integer.parseInt(child.child("stepgoal").getValue().toString()));
                        cg.setText(child.child("steps").child(formattedDate).getValue().toString());
                        cal.setText(getString(R.string.cals,child.child("currcal").child(formattedDate).getValue().toString()));

                        tick.setVisibility(View.VISIBLE);
                        if(flaggoal==0)
                        {Toast.makeText(Homepage.this,"Daily step goal achieved!! Keep it up!",Toast.LENGTH_SHORT).show();
                        flaggoal=1;}
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Homepage.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
        ref.child("steps").setValue(currstep).toString();

        rf=findViewById(R.id.refresh);
        rf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, Homepage.class));
            }
        });


    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
    }

}
/*

    public void newsteps() {
        ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());
                    currstep = Integer.parseInt(dataSnapshot.child("steps").getValue().toString());
                    currstep += stepCount;
                    stepCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Homepage.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
        ref.child("steps").setValue(currstep).toString();
    }
}
       /* stepref = FirebaseDatabase.getInstance().getReference("user/"+);
        stepref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stepref = FirebaseDatabase.getInstance().getReference("user").child(dataSnapshot.child("steps").getKey());
                currstep = Integer.parseInt(dataSnapshot.child("steps").getValue().toString());
                currstep += stepCount;
                stepCount = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        stepref.child("steps").setValue(currstep);

    }

}
















//       ref=FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
/*
        Query userQuery = ref.orderByChild("email").equalTo(Member.currmail.toString());
        Toast.makeText(Homepage.this, "Welcome "+ Member.currmail+" !", Toast.LENGTH_SHORT).show();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Toast.makeText(Homepage.this, "Welcome "+userSnapshot.child("username").getValue().toString()+" !", Toast.LENGTH_SHORT).show();
                    bmi.setText(userSnapshot.child("bmi").getValue().toString());
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });


  /*      DatabaseReference id= ref.child("0");

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passfromdb=snapshot.child(Member.currmail).child()
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

/*
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long points = dataSnapshot.child("user").getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });



        fba = FirebaseAuth.getInstance();
        fsl = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    emails = firebaseUser.getEmail();
                    bmi.setText(userId);
                    test.setText(emails);
                }
            }
        };




       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            names = user.getDisplayName();
            emails = user.getEmail();
            bmi.setText(names);
            test.setText(names);
            // Check if user's email is verified
            boolean email = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

*/

 /*   database=FirebaseDatabase.getInstance();
        ref=FirebaseDatabase.getInstance().getReference("user");
        names= snap.child("username").getValue().toString();
        bmi=findViewById(R.id.bmi);
        bmi.setText(names);*/

     /*   ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   String value = dataSnapshot.getValue(String.class);
                                                   bmi.setText(value);
                                               }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Homepage.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });*/

   /*     String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long user = dataSnapshot.child("user").getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }



        });





        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getUid();

        ref.child(userid).setValue(user);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String name=datas.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String userid= user.getUid();

        ref.child(userid).setValue(user);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    emails=datas.child("email").getValue().toString();
                    bmi.setText(userid);
                    test.setText(emails);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        */
    /*  currstep = Integer.parseInt(child.child("steps").getValue().toString());
                    if (currstep != stepCount + Integer.parseInt(child.child("steps").getValue().toString())) {
                        currstep += stepCount;
                        result.put("steps", currstep);
                        ref.updateChildren(result);
                        stepCount = 0;
                        currstep = Integer.parseInt(child.child("steps").getValue().toString());
                    }*/

