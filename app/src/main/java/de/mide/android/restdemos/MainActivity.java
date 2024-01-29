package de.mide.android.restdemos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.mide.android.restdemos.datumzeit.DatumUndZeitActivity;

/**
 * Activity mit Hauptmenü der App.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonDatumUndZeitHolen(View view) {

        Intent intent = new Intent(this, DatumUndZeitActivity.class);
        startActivity(intent);
    }

    public void onZufallspersonenHolen(View view) {

    }

    public void onBildHolen(View view) {

    }

}