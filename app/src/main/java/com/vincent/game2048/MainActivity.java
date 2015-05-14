package com.vincent.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvScore.setTextSize(30);
        tvScore.setText("0");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addScore(int s) {
        score+=s;
        showScore();
    }
    public void clearScore() {
        score = 0;
        showScore();
    }
    public void showScore() {
        tvScore.setText(score+"");
    }
    public static MainActivity getMainActivity() {return mainActivity;}
    private static MainActivity mainActivity = null;
    private TextView tvScore;
    private int score = 0;
}
