package com.example.first;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class WaterActivity extends AppCompatActivity {

    public static int scount;
    TextView glasses;
    Button button_add;
    Button button_sub;
    ImageView goaltick;
    private String stringVal;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference ref= database.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        glasses = (TextView) findViewById(R.id.numberofglasses);
        button_sub = (Button) findViewById(R.id.button_sub);
        button_add = (Button) findViewById(R.id.button_add);
        goaltick = (ImageView) findViewById(R.id.tick);

        ref.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stringVal = child.getKey().toString();
                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey().toString());

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);

                    ref = FirebaseDatabase.getInstance().getReference("user").child(child.getKey());
                    ref=ref.child("water");

                    DataSnapshot stepSnap = child.child("water");
                    Iterable<DataSnapshot>  stepChildren = stepSnap.getChildren();

                    for (DataSnapshot dates : stepChildren) {
                        if(formattedDate.equals(dates.getKey())) {
                            glasses.setText(dates.getValue().toString());
                            scount=Integer.parseInt(dates.getValue().toString());
                        }
                        scount=Integer.parseInt(dates.getValue().toString());
                    }


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(WaterActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });

        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scount!=0)
                {
                    scount--;

                }
                glasses.setText(String.valueOf(scount));
                if(scount>=8)
                {Toast.makeText(WaterActivity.this,"Daily sleep hours goal achieved!",Toast.LENGTH_SHORT).show();
                    goaltick.setVisibility(View.VISIBLE);
                }
                else{
                    goaltick.setVisibility(View.INVISIBLE);
                }
                ref = FirebaseDatabase.getInstance().getReference("user").child(stringVal);
                HashMap<String, Object> result2 = new HashMap<>();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                result2.put("water/"+ formattedDate,scount);
                ref.updateChildren(result2);
            }
        });


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scount++;

                glasses.setText(String.valueOf(scount));
                if(scount>=8)
                {Toast.makeText(WaterActivity.this,"Daily sleep hours goal achieved!",Toast.LENGTH_SHORT).show();
                    goaltick.setVisibility(View.VISIBLE);
                }
                else{
                    goaltick.setVisibility(View.INVISIBLE);
                }
                ref = FirebaseDatabase.getInstance().getReference("user").child(stringVal);
                HashMap<String, Object> result2 = new HashMap<>();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                result2.put("water/"+ formattedDate,scount);
                ref.updateChildren(result2);

            }
        });




    }

}