package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MeditateActivity extends AppCompatActivity {

    TextView timer;
    Button five,ten,fifteen;
    Switch switchbutton;
    int time;
    CountDownTimer countdown;
    MediaPlayer mp;
    int c=1,identifier;
    ConstraintLayout cl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditate);

        timer=findViewById(R.id.timer);
        five=findViewById(R.id.five);
        ten=findViewById(R.id.ten);
        fifteen=findViewById(R.id.fifteen);
        switchbutton=findViewById(R.id.switchbutton);
        mp = MediaPlayer.create(this,R.raw.nature_pink_noise_delta_waves);
        cl=findViewById(R.id.conslayout);

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= 5*60*1000;
                Toast.makeText(MeditateActivity.this,"Five minutes Selected!",Toast.LENGTH_SHORT).show();
            }
        });

         ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= 10*60*1000;
                Toast.makeText(MeditateActivity.this,"Ten minutes Selected!",Toast.LENGTH_SHORT).show();
            }
        });

        fifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= 15*60*1000;
                Toast.makeText(MeditateActivity.this,"Fifteen minutes Selected!",Toast.LENGTH_SHORT).show();
            }
        });

        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    mp.seekTo(0);
                    mp.start();

                    countdown = new  CountDownTimer(time, 1000) {

                        public void onTick(long millisUntilFinished) {
                            timer.setText(getString(R.string.timertext,millisUntilFinished / 1000,"s"));
                           if((time-millisUntilFinished/1000)%10 == 0)
                            {
                               if(c<5)
                                {
                                  identifier=getResources().getIdentifier("a"+ c, "drawable","com.example.first");
                                   cl.setBackgroundResource(identifier);
                                    c++;
                                }
                                else {
                                   c = 0;
                                   identifier = getResources().getIdentifier("a" + c, "drawable", "com.example.first");
                                   cl.setBackgroundResource(identifier);
                               }
                            }
                        }

                        public void onFinish() {
                            timer.setText("Session completed!");
                            mp.pause();
                        }
                    }.start();
                }
                else {
                    timer.setText("Cancelled");
                    countdown.cancel();
                    mp.pause();
                }
            }
        });
    }

    public void onBackPressed() {
        mp.pause();
        super.onBackPressed();
    }
}