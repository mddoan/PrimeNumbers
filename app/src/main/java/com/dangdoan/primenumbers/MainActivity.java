package com.dangdoan.primenumbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment();
    }

    private void loadFragment(){
        MainFragment frag = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContent, frag).commit();
    }
}
