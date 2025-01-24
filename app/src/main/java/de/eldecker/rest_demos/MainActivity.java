package de.eldecker.rest_demos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;

import de.eldecker.rest_demos.activities.BildActivity;
import de.eldecker.rest_demos.activities.DatumUndZeitActivity;
import de.eldecker.rest_demos.activities.ZufallsnamenActivity;


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

        Intent intent = new Intent(this, ZufallsnamenActivity.class);
        startActivity(intent);
    }


    public void onBildHolen(View view) {

        Intent intent = new Intent(this, BildActivity.class);
        startActivity(intent);
    }

}