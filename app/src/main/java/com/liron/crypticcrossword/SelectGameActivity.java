package com.liron.crypticcrossword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SelectGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
    }

    public void setGame(View view) {
        Intent intent = getIntent();
        intent.setClass(this, MainActivity.class);
        intent.putExtra(getString(R.string.gameType), (String) view.getTag());
        startActivity(intent);
    }
}
