package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    String emailid;
    DatabaseReference ref;
    Button proceed;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ref = FirebaseDatabase.getInstance().getReference("user");


        proceed=findViewById(R.id.proceed);
        email=findViewById(R.id.email);



        auth = FirebaseAuth.getInstance();
        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                emailid=email.getText().toString();
                if(!emailid.isEmpty() ){
                auth.sendPasswordResetEmail(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this,"Email sent! Check your email id for instructions",Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(ForgotPasswordActivity.this,"Email failed to send",Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                else{
                    email.setError("Provide valid email id");
                    email.requestFocus();
                }

            }

});


}
}