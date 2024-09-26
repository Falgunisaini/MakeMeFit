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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    Button proceed;
    EditText pass0,pass1, pass2;
    String currpass,p1,p2,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        proceed=findViewById(R.id.proceed);
        pass1=findViewById(R.id.password1);
        pass2=findViewById(R.id.password2);
        pass0=findViewById(R.id.currentpass);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1=pass1.getText().toString();
                p2=pass2.getText().toString();
                currpass=pass0.getText().toString();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    email = user.getEmail();
                }

                AuthCredential credential = EmailAuthProvider.getCredential(email,currpass);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this,"Authentication successful!",Toast.LENGTH_SHORT).show();
                            if(p2.equals(p1)){

                                user.updatePassword(p1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ResetPasswordActivity.this,"Password Changed!",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(ResetPasswordActivity.this, "Password change unsuccessful!", Toast.LENGTH_SHORT).show();
                                            pass0.requestFocus();
                                        }
                                    }
                                });
                            }
                            else{
                                pass2.setError("Passwords do not match!");
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this,"Authentication unsuccessful!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



            }

        });


    }
}