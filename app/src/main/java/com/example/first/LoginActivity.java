package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email,passwordEditText;
    String emails,passwords;
    FirebaseAuth fba;
    Button forgot,loginButton ;
    private FirebaseAuth.AuthStateListener fsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login);
        passwordEditText = findViewById(R.id.password);
        email = findViewById(R.id.email);
        fba=FirebaseAuth.getInstance();
        forgot=findViewById(R.id.forgotpass);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        fsl=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbu= fba.getCurrentUser();
                if(fbu!=null) {
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoginActivity.this,Selection.class);
                    startActivity(i);
                }
                else{
                   // Toast.makeText(LoginActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emails=email.getText().toString();
                passwords=passwordEditText.getText().toString();
                if(emails.isEmpty())
                {
                    email.setError("Please enter email");
                    email.requestFocus();
                }
                if(passwords.isEmpty())
                {
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                }


                if(!emails.isEmpty() && !passwords.isEmpty()){
                    fba.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(LoginActivity.this,"Login Unsuccessful! Please try again",Toast.LENGTH_SHORT).show();
                            else
                            {
                                Member.currmail=emails;
                                Member.currpass=passwords;
                                startActivity(new Intent(LoginActivity.this,Selection.class));}
                        }
                    });
                }
                else
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        fba.addAuthStateListener((fsl));
    }
}