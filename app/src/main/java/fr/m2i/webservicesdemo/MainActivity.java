package fr.m2i.webservicesdemo;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etCode;
    TextView etNomPays;
    ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void searchOne(View view) {

        etCode = findViewById(R.id.txtCode);
        etNomPays = findViewById(R.id.txtNom);

        String code = etCode.getText().toString();

        MonThread th = new MonThread();
        JSONObject json = null;
        System.out.println("LA reponse arrivé JSONObject");

        th.setAdr("http://demo@services.groupkt.com/country/get/iso2code/" + code);
        th.start(); // Démarre le traitement dans le thread séparé

        try {
            th.join();
            System.out.println("Le nom:§§§§ " + th.getRes());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            json = new JSONObject(th.getRes());
            JSONObject dept = json.getJSONObject("RestResponse");
            JSONObject result = dept.getJSONObject("result");
            String nom = result.getString("name");
            etNomPays.setText(nom);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void searchAll(View view) {

        /*Norme Json
            [ = tableau traitement avec JSONArray
            { = objet traitement avec JSONObject
         */

        MonThread th = new MonThread();
        JSONObject json = null;

        th.setAdr("http://demo@services.groupkt.com/country/get/all");
        th.start(); // Démarre le traitement dans le thread séparé

        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            json = new JSONObject(th.getRes());
            JSONObject rest = json.getJSONObject("RestResponse");

            //tableau qui comprend tous les objets "names","alpha..."
            JSONArray result = rest.getJSONArray("result");

            ArrayList<String> listNom = new ArrayList<String>();

            //boucle d'ajout des noms dans un tableau
            for (int i = 0; i < result.length(); i++) {
                //recherche objet par objet dans le tableau result
                JSONObject listObjets = result.getJSONObject(i);

                //ajout le "nam" de chaque objet parcouru
                listNom.add(listObjets.getString("name"));
            }

            //affichage du tableau dans la ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listNom);
            lv1 = findViewById(R.id.LstPays);
            lv1.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
