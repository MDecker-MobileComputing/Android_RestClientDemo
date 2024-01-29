package de.mide.android.restdemos.datumzeit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.mide.android.restdemos.R;

public class DatumUndZeitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datumzeit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.activity_titel_datumzeit);
    }

}