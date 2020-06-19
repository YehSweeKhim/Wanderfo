package com.example.wanderfoapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.Shop.Login;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    private static String[] shopnames = new String[]{"Select shop"};
    AutoCompleteTextView search;
    public final static String SHOP_NAME = "SHOP_NAME";
    ArrayAdapter adapter;

    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(SHOP_NAME, search.getText().toString());
        preferencesEditor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //database query to get the list
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        search = findViewById(R.id.tvAutoComp);

        Log.i("ZW", "onStart()");
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(Home.this);
        String  newUrl =  url + "getshopname/null";

        Log.i("ZW store name", newUrl );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ZW",response);
                        Log.i("ZW","Response is: "+ response.replace("\"", ""));
                        List<String> items = Arrays.asList(response.split(","));
                        Log.i("ZW", items.toString());
                        shopnames = response.split(",");
                        Log.i("ZW", Arrays.toString(shopnames));
                        adapter = new ArrayAdapter(Home.this, android.R.layout.simple_list_item_1,items);
                        search.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ZW", "That didn't work!");
                Log.i("ZW", error.getMessage());
            }
        });

        queue.add(stringRequest);

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, UserView.class);
                startActivity(intent);
            }
        });

        CardView food = findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Food.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Announcement.class));
                        overridePendingTransition(0,0);
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });
    }
}
