package com.example.wanderfoapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.Shop.Login;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Food extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Category> adapter;
    private String[] categories = {"All","Food Supply","Food Manufacturing","F&B Outlets","Food Caterer",
                "Food Delivery Services","Food Packaging & Printing Service Providers","Laboratory Food Safety","Activities pertaining to extension of shelf-life"};

    public static final String BIO_KEY = "BIO_KEY";
    public static final String STAUS_KEY = "STATUS_KEY";
    public static final String CATEGORY_KEY = "CATEGORY_KEY";
    public static final String ITEM_KEY = "ITEM_KEY";
    public final static String SHOP_NAME = "SHOP_NAME";

    private SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Food.this, Home.class);
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
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
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

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories));

        listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.list_textview, getList()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=0 && i<categories.length){
                    getSelectedCategoryData(i);
                }else {
                    Toast.makeText(Food.this, "Selected Category Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Food.this,"Go to next page", Toast.LENGTH_SHORT).show();

                Log.i("ZW store name",adapterView.getItemAtPosition(i).toString() );
                String storeName = adapterView.getItemAtPosition(i).toString();
                final Intent intent = new Intent(Food.this, UserView.class);
                String nameWithoutSpace = adapterView.getItemAtPosition(i).toString().replace(" ", "_");

                Log.i("ZW name w/o space", nameWithoutSpace);
                intent.putExtra(SHOP_NAME, nameWithoutSpace);

                mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
                String name = mPreferences.getString(SHOP_NAME, "");

                // Request a string response from the provided URL.
                RequestQueue queue = Volley.newRequestQueue(Food.this);
                String  newUrl =  url + "getshopdetail/" + nameWithoutSpace.toLowerCase();

                Log.i("ZW value shared prefe: ", ""+ mPreferences.getString(SHOP_NAME, ""));

                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString(SHOP_NAME, nameWithoutSpace);
                preferencesEditor.apply();

                startActivity(intent);
            }
        });
    }

    private ArrayList<Category> getList() {
        ArrayList<Category> list = new ArrayList<>();
        list.clear();
        list.add(new Category("Breadtalk", 1));
        list.add(new Category("Peach Garden", 2));
        list.add(new Category("watsons", 1));
        list.add(new Category("fairprice", 1));
        list.add(new Category("7_eleven", 1));
        list.add(new Category("daiso", 1));
        return list;
    }

    private void getSelectedCategoryData(int categoryID) {
        ArrayList<Category> shops = new ArrayList<>();
        if (categoryID == 0) {
            adapter = new ArrayAdapter(this, R.layout.list_textview, getList());
        }else {
            for (Category cosmicBody: getList()) {
                if (cosmicBody.getcategoryID() == categoryID){
                    shops.add(cosmicBody);
                }
            }
            adapter = new ArrayAdapter(this, R.layout.list_textview, shops);
        }
        listView.setAdapter(adapter);
    }
}
