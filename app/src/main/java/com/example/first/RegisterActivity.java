package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,age,weight,height;
    String names,emails,genders,ages,weights,heights,passwords;
    Member mem;
    private RadioButton radio;
    FirebaseAuth fba;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fba=FirebaseAuth.getInstance();

        final RadioGroup radioGroup=(RadioGroup)findViewById(R.id.genid);
        Button regButton = findViewById(R.id.inforegister);


        name= findViewById(R.id.editTextTextPersonName);
        email=findViewById(R.id.email);


        height = findViewById(R.id.editTextNumberDecimal);
        weight = findViewById(R.id.editTextNumberDecimal2);
        age=findViewById(R.id.ageid);


        db = FirebaseDatabase.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText passwordEditText = findViewById(R.id.password2);
                int radio_id = radioGroup.getCheckedRadioButtonId();
                radio= findViewById(radio_id);

                genders=radio.getText().toString();
                names=name.getText().toString();
                emails=email.getText().toString();
                passwords=passwordEditText.getText().toString();
                weights=weight.getText().toString();
                heights=height.getText().toString();
                ages=age.getText().toString();
                genders=radio.getText().toString();

                database=FirebaseDatabase.getInstance();
                ref= FirebaseDatabase.getInstance().getReference();

                if(emails.isEmpty())
                {
                    email.setError("Please enter email");
                    email.requestFocus();
                }
                if(names.isEmpty())
                {
                    email.setError("Please enter name");
                    email.requestFocus();
                }
                if(passwords.isEmpty())
                {
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                }
                if(ages.isEmpty())
                {
                    age.setError("Please enter age");
                    age.requestFocus();
                }
                if(heights.isEmpty())
                {
                    height.setError("Please enter height");
                    height.requestFocus();
                }
                if(weights.isEmpty())
                {
                    weight.setError("Please enter weight");
                    weight.requestFocus();
                }
                if(genders.isEmpty())
                {
                    radio.setError("Please enter gender");
                    radio.requestFocus();
                }
                if(!emails.isEmpty() && !passwords.isEmpty()){
                    fba.createUserWithEmailAndPassword(emails,passwords).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(RegisterActivity.this,"Sign up Unsuccessful! Please try again",Toast.LENGTH_SHORT).show();
                            else{
                                HashMap<String,Object> steps=new HashMap<>();
                                HashMap<String,Object> calmap=new HashMap<>();
                                Date c = Calendar.getInstance().getTime();

                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);
                                ref=FirebaseDatabase.getInstance().getReference("user");
                                mem = new Member();
                                mem.setUsername(names);
                                mem.setEmail(emails);
                                mem.setWeight(weights);
                                mem.setHeight(heights);
                                mem.setAge(ages);
                                mem.setGender(genders);
                                mem.setBmi();
                                mem.setStepgoal();
                                steps.put(formattedDate,"0");
                                calmap.put(formattedDate,"0");
                                mem.setsteps(steps);
                                mem.setCalories();
                                mem.setcurrcal(calmap);

                                String userId = ref.push().getKey();
                                ref.child(userId).setValue(mem);
                                startActivity(new Intent(RegisterActivity.this,Selection.class));
                                }
                        }
                    });
                }
                else
                 Toast.makeText(RegisterActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}