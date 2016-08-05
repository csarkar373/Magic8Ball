package com.westhillcs.magic8ball;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Magic8BallActivity extends AppCompatActivity {

    Button b_predict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic8_ball);
        final Magic8Ball my8ball = new Magic8Ball(this);

        b_predict = (Button)findViewById(R.id.predict_b);
        b_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my8ball.makePrediction();
            }
        });


    }
}
