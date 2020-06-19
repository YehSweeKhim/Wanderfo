package com.example.wanderfoapp.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanderfoapp.Shop.ExampleItem;
import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.R;
import com.example.wanderfoapp.Shop.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView storeName;
    TextView storeStatus;
    TextView infoUpdates;
    TextView category;
    private ArrayList<ExampleItem> myDataset;

    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/getshopdetail/";
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        myDataset = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new UserAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        storeName = findViewById(R.id.storeName);
        storeStatus = findViewById(R.id.storeStatus);
        infoUpdates = findViewById(R.id.infoUpdates);
        category = findViewById(R.id.category);

        // Getting store name from Food page
        Intent intent = getIntent();
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String nameStore = mPreferences.getString("SHOP_NAME", "");
        storeName.setText(nameStore);
        infoUpdates.setText(intent.getStringExtra(Food.BIO_KEY));

//        Log.i("getValue", Arrays.toString(getVal));
        //TODO Get store status from response (HTTP Get)

        storeStatus.setText(Food.STAUS_KEY);
        category.setText(Food.CATEGORY_KEY);
        infoUpdates.setText(Food.BIO_KEY);
        String itemString = Food.ITEM_KEY;

        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(UserView.this);
        String  newUrl =  url  + storeName.getText().toString().toLowerCase();

        Log.i("ZW store name", newUrl );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ZW","Response is: "+ response.replace("\"", ""));
                        String stringToMap = response.replace("\"", "");
                        //new HashMap object
                        Map<String, String> hMapData = new HashMap<String, String>();
                        String parts[] = stringToMap.split(", ");

                        for(String part : parts){
                            String empdata[] = part.split(":");
                            String strId = empdata[0].trim();
                            String strName = empdata[1].trim();
                            hMapData.put(strId, strName);
                        }

                        infoUpdates.setText(hMapData.get("bio"));

                        if(hMapData.get("status") == "0"){
                            storeStatus.setText("Closed");
                        }
                        else{
                            storeStatus.setText("Open");
                        }
                        category.setText(hMapData.get("category"));

                        String itemString = hMapData.get("item");
                        //[notebook, Availability, Available,  towelStock, -, 30 ]
                        String[] itemsArray = itemString.split(",");
                        Log.i("ZW items: ", Arrays.toString(itemsArray));

                        if((itemsArray[0]).equals("nothing") ){
                            itemsArray = new String[0];
                        }

                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(itemsArray));
                        for(int i=0;i<arrayList.size();i+=3){
                            myDataset.add(new ExampleItem(arrayList.get(i+1), arrayList.get(i+2) ,arrayList.get(i)));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ZW", "That didn't work!");
                Log.i("ZW", error.getMessage());
            }
        });
        queue.add(stringRequest);
        String[] items = itemString.split(",");

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
