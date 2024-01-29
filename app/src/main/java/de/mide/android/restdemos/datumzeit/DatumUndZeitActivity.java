package de.mide.android.restdemos.datumzeit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.mide.android.restdemos.R;

/**
 * Activity, um Datum+Uhrzeit von REST-API abzufragen.
 */
public class DatumUndZeitActivity extends AppCompatActivity {

    public static final String TAG4LOGGING = "DatumZeitVonWebAPI";

    /** Button mit dem der Web-Request gestartet wird. */
    protected Button _startButton = null;

    /**
     * TextView zur Anzeige des Ergebnisses des Web-Requests (also Datum + Uhrzeit),
     * auch zur Anzeige von Fehlermeldungen.
     */
    protected TextView _ergebnisTextView = null;

    /**
     * Lifecycle-Methode: Layout für UI laden und Referenzen auf UI-Elemente holen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datumzeit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.activity_titel_datumzeit);

        _startButton      = findViewById( R.id.starteWebRequestButton );
        _ergebnisTextView = findViewById( R.id.ergebnisTextView       );

        // vertikales Scrolling ermöglichen
        _ergebnisTextView.setMovementMethod( new ScrollingMovementMethod() );
    }


    /**
     * Event-Handler-Methode für Button zum Absetzen des
     * HTTP-Requests an den REST-Service.
     *
     * @param view Button, der Event ausgelöst hat.
     */
    public void onStartButtonBetaetigt(View view) {

        _startButton.setEnabled(false); // Button deaktivieren während ein HTTP-Request läuft

        _ergebnisTextView.setText("Starte HTTP-Request ...");


        // Hintergrund-Thread mit HTTP-Request starten
        MeinHintergrundThread mht = new MeinHintergrundThread();
        mht.start();
    }

    /**
     * In dieser Methode wird der HTTP-Request zur Web-API durchgeführt.
     * Achtung: Diese Methode darf nicht im Main-Thread ausgeführt werden,
     * weil ein Internet-Zugriff länger dauern kann (mehrere Sekunden oder Minuten),
     * so dass die App wegen <i>"Application Not Responding" (ANR)</i> ggf.
     * vom Nutzer abgebrochen würde.
     *
     * @return String mit JSON-Dokument, das als Antwort zurückgeliefert wurde.
     *
     * @throw IOException  Ein-/Ausgabefehler
     */
    protected String holeDatenVonWebAPI() throws IOException {

        URL url                                = null;
        HttpURLConnection conn                 = null;
        String            httpErgebnisDokument = "";


        // eigene Implementierung von http://www.jsontest.com/#date (weil http://time.jsontest.com kein httpS unterstützt)
        // Beispiel für Antwort: https://gist.github.com/MDecker-MobileComputing/369b9b6574edab68cd4dcfaf8d42e47b
        url  = new URL("https://el-decker.de/rest/DatumUndZeit.php");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // Eigentlich nicht nötig, weil "GET" Default-Wert ist.

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

            String errorMessage = "HTTP-Fehler: " + conn.getResponseMessage();
            throw new IOException( errorMessage );

        } else {

            InputStream is        = conn.getInputStream();
            InputStreamReader ris = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(ris);

            // JSON-Dokument zeilenweise einlesen
            String zeile = "";
            while ( (zeile = reader.readLine()) != null) {

                httpErgebnisDokument += zeile;
            }
        }

        Log.i(TAG4LOGGING, "JSON-String erhalten: " + httpErgebnisDokument);

        return httpErgebnisDokument;
    }

    protected String parseJSON(String jsonString) throws JSONException {

        if (jsonString == null || jsonString.trim().length() == 0) {

            return "Leeres JSON-Objekt von Web-API erhalten.";
        }

        // eigentliches Parsen der JSON-Datei
        JSONObject jsonObject = new JSONObject( jsonString );

        // Zwei Attribute abfragen
        String zeitString  = jsonObject.getString( "zeit" );
        String datumString = jsonObject.getString( "datum" );

        // String für Ausgabe auf UI zusammenbauen
        return "Zeit: "       + zeitString  +
                "\n\nDatum: " + datumString;
    }

    /* *************************** */
    /* *** Start innere Klasse *** */
    /* *************************** */

    /**
     * Zugriff auf Web-API (Internet-Zugriff) wird in
     * eigenen Thread ausgelagert, damit der Main-Thread
     * nicht blockiert wird.
     */
    protected class MeinHintergrundThread extends Thread {

        /**
         * Der Inhalt in der überschriebenen <i>run()</i>-Methode
         * wird in einem Hintergrund-Thread ausgeführt.
         */
        @Override
        public void run() {

            try {

                String jsonDocument = holeDatenVonWebAPI();

                String ergString = parseJSON(jsonDocument);

                ergbnisDarstellen( "Ergebnis von Web-Request:\n\n" + ergString);
            }
            catch (Exception ex) {

                ergbnisDarstellen( "Exception aufgetreten:\n\n" + ex.getMessage() );
            }
        }


        /**
         * Methode um Ergebnis-String in TextView darzustellen. Da
         * es sich hierbei um einen UI-Zugriff handelt, müssen
         * wir mit der <i>post()</i>-Methode dafür sorgen, dass die
         * UI-Zugriffe aus dem Main-Thread heraus durchgeführt werden.
         * Der Start-Button wird auch wieder aktiviert.
         *
         * @param ergebnisStr Nachricht, die in TextView-Element dargestellt werden soll.
         */
        protected void ergbnisDarstellen(String ergebnisStr) {

            final String finalString = ergebnisStr;

            _startButton.post( new Runnable() { // wir könnten auch die post()-Methode des TextView-Elements verwenden
                @Override
                public void run() {

                    _startButton.setEnabled(true);
                    _ergebnisTextView.setText(finalString);
                }
            });

        }

    };

    /* *************************** */
    /* *** Ende innere Klasse  *** */
    /* *************************** */


}