package com.example.reccyclerviewjsonvolley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Objectif de cette app : récupérer des images et du texte sur le site de pixabay.com sous forme
 * de fichier JSON et de remplir un RecyclerView en utilisant Volley
 * #1 Ajout des librairies pour le bon fonctionnement de l'app dans le Gradle Module
 * #2 Ajout des permissions dans le manifest
 * #3 Création du layout de main activity
 * #4 Création du layout pour une ligne du Recycler
 * #5 Création du model : ModelItem
 * #6 Création de l'adapter du recyclerView
 **/

/**
 * #10 implémentation de l'interface dans le MainActivity
 **/
public class MainActivity extends AppCompatActivity implements AdapterItem.OnItemClickListener {

    private static final String TAG = "MainActivity";

    /**
     * #7 Création des variables globales
     **/
    private RecyclerView recyclerView;
    private AdapterItem adapterItem;
    private ArrayList<ModelItem> itemArrayList;
    private RequestQueue requestQueue;
    private String search;

    /**
     * 11 Ajout du formulaire pour une recherche
     **/
    private EditText etSearch;

    /**
     * 10.1.1 VAR pour les putExtra
     **/
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creator";
    public static final String EXTRA_LIKES = "likes";

    /**
     * #8 Création de la méthode init pour initaliser les différentes composants
     **/
    public void init() {
        // Le recycler et ses composants
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true); // Permet de fixer la hauteur et la largeur et donc d'améliorer les perfs
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Ajout du gestion de ligne pour notre recycler

        // Le tableau avec les données
        itemArrayList = new ArrayList<>();

        // La requête pour obtenir les données du JSON
        requestQueue = Volley.newRequestQueue(this);

        /** 11.1 Ajout de la recherche **/
        etSearch = findViewById(R.id.etSearch);
    }

    /**
     * 11.2 Click bouton
     **/
    public void newSearch(View view) {
        itemArrayList.clear();
        search = etSearch.getText().toString().trim();
        Log.i(TAG, "newSearch: " + search);
        parseJSON(search);
    }

    /**
     * #9 Création de la méthode pour parse
     **/
    private void parseJSON(String search) {
        /** 11.2 Ajout de la recherche personalisé **/
//        search = etSearch.getText().toString().trim();

        String pixabayApiKey = "VOTRE API KEY DEPUIS LE SITE PIXABAY";

        // Ouvrir un compte sur Pixabay, pour plus d'informations sur les options qui peuvent être
        // ajoutés voir ici : https://pixabay.com/api/docs/
        String urlJSONFile = "https://pixabay.com/api/"
                + "?key="
                + pixabayApiKey // La clé de l'API Pixabay
                + "&q="
                + search // la requête
                + "&image_typephoto" // le type d'image à afficher
                + "&pretty=true"; // l'affichage du fichier

        Log.i(TAG, "parseJSON: " + urlJSONFile);
        // Création de l'objet JSON request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlJSONFile, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // On récupère le tableau de données JSON à partir de notre objet JsonObjectRequest
                            // dans un try and catch ajouter en auto en corrigeant l'erreur
                            JSONArray jsonArray = response.getJSONArray("hits");

                            // On récupère dans un premier temps toutes les données présentent dans le Array avec une boucle for
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                // Puis on sélectionne celles dn on à besoin soit user - likes - webformatURL
                                String creator = hit.getString("user");
                                int likes = hit.getInt("likes");
                                String imageUrl = hit.getString("webformatURL");

                                // On ajoute les données à notre tableau en utilisant son model
                                itemArrayList.add(new ModelItem(imageUrl, creator, likes));
                            }
                            // En dehors du try on adapte le tableau de données
                            adapterItem = new AdapterItem(MainActivity.this, itemArrayList); // Noter MainActivity.this car nous sommes dans une classe interne

                            // Puis on lie l'adpter au Recycler
                            recyclerView.setAdapter(adapterItem);

                            /** #10.3 On peut alors ajouter le listener **/
                            adapterItem.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        /** #9.1 On rempli la request avec les données récupérées **/
        requestQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** #8.1 Appel de la méthode d'initialisation des composants **/
        init();


        /** #9.2 Appel de la méthode de Parse **/
        parseJSON(search);
        // On peut tester puis créer une nouvelle activité pour afficher le détail lors du clic on sort avant le #10
    }

    /**
     * #10.1 Auto implémente de la méthode
     **/
    @Override
    public void onItemClick(int position) {
        // Ici on va ajouter le code pour démarrer la seconde activité et y passer les données de l'item cliqué
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ModelItem clickItem = itemArrayList.get(position); // On récupère les infos de l'item cliqué sur la base du model

        // On créer d'abord les variables globales pour les putExtra
        detailIntent.putExtra(EXTRA_URL, clickItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickItem.getLikes());

        // On démarre l'activité
        startActivity(detailIntent);
        // On passe à DetailActivity pour récupérer et afficher les données
    }
}