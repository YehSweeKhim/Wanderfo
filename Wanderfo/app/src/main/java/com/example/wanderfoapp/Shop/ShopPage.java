package com.example.wanderfoapp.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.Home.Home;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopPage extends AppCompatActivity {
    // for spinner
    private ArrayList<SpinnerTypeElement> spinnerTypeList;
    private SpinnerTypeAdapter spinnerTypeAdapter;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonInsert;
    private ArrayList<ExampleItem> examplelist;
    public final static String NAME_KEY = "NAME_KEY";
    public final static String TYPE_KEY = "TYPE_KEY";
    public final static String TYPE_VALUE_KEY = "TYPE_VALUE_KEY";
    public static String STORE_NAME_KEY = "STORE_NAME";
    public static String ITEM_KEY = "ITEM_KEY";

    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    TextView storeName;
    TextView permission;
    TextView status;
    TextView bio;
    Button editBioButt;
    EditText editBio;
    Button saveBioButt;

    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        examplelist = new ArrayList<>();

        storeName = findViewById(R.id.shopname);
        permission = findViewById(R.id.permission);
        status = findViewById(R.id.status);
        bio = findViewById(R.id.bio);
        editBio = findViewById(R.id.editBio);
        editBioButt = findViewById(R.id.editBioButt);
        saveBioButt = findViewById(R.id.saveBioButt);

        editBio.setVisibility(View.GONE);
        saveBioButt.setVisibility(View.GONE);

        editBioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bio.setVisibility(View.GONE);
                editBio.setVisibility(View.VISIBLE);
                saveBioButt.setVisibility(View.VISIBLE);
            }
        });

        saveBioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editBio.getText().toString().isEmpty()){
                    Toast.makeText(ShopPage.this, "Please fill in the bio/services.", Toast.LENGTH_LONG).show();
                } else{
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("target", "setbio");
                        jsonBody.put("shopname", storeName.getText().toString().toLowerCase());
                        jsonBody.put("value", editBio.getText().toString());

                        Log.i("ZW string json to post", jsonBody.toString());

                        String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";
                        RequestQueue queue = Volley.newRequestQueue(ShopPage.this);
                        final JSONObject[] returnObj = {new JSONObject()};

                        JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("ZW","Response: " + response);
                                Toast.makeText(ShopPage.this, response.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                    Log.i("ZW error", error.getMessage());
                                Log.i("ZW error", "   error");
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                return headers;
                            }
                        };
                        queue.add(jsonObjectRequestt);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    saveBioButt.setVisibility(View.GONE);
                }
            }
        });

        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String nameStore = mPreferences.getString("STORE_NAME_KEY", "");
        storeName.setText(nameStore);

        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(ShopPage.this);
        String  newUrl =  url + "getshopdetail/" + storeName.getText().toString().toLowerCase();

        Log.i("ZW store name", newUrl );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("ZW","Response is: "+ response.replace("\"", ""));
                    Log.i("ZW","checking error herer: "+ response.replace("\"", ""));

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
                    status.setText(hMapData.get("status"));
                    if(hMapData.get("status") == "0"){
                        status.setText("Status: Closed");
                    }else{
                        status.setText("Status: Open");
                    }
                    bio.setText(hMapData.get("bio"));

                    String itemString = hMapData.get("item");
                    //[notebook, Availability, Available,  towelStock, -, 30 ]
                    String[] itemsArray = itemString.split(",");
                    Log.i("ZW items: ", Arrays.toString(itemsArray));

                    if((itemsArray[0]).equals("nothing") ){
                        itemsArray = new String[0];
                    }

                    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(itemsArray));
                    for(int i=0;i<arrayList.size();i+=3){

                        examplelist.add(new ExampleItem(arrayList.get(i+1), arrayList.get(i+2) ,arrayList.get(i)));

                        System.out.println("\n");
                    }
                    buildRecyclerView(examplelist);
                    buttonInsert = findViewById(R.id.button_insert);
                    buttonInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ShopPage.this, AddItemActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("ZW", "That didn't work!");
            }
        });

        queue.add(stringRequest);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.user);
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
    }

    public void removeItem(int position){
        Log.i("jo","position "+position);
//        examplelist.remove(position);
        // trigger the db remove
        mAdapter.notifyDataSetChanged();
        // get the arraylist of items from database again
        ExampleItem exampleItem = examplelist.get(position);
        String typeVal = exampleItem.getType();
        String type = exampleItem.getType_value();
        String name = exampleItem.getName();
        String value = name + "," + type + "," + typeVal;

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("target", "deleteitem");
            jsonBody.put("shopname", storeName.getText().toString().toLowerCase());
            jsonBody.put("value", value);

            Log.i("ZW string json to post", jsonBody.toString());

            String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";
            RequestQueue queue = Volley.newRequestQueue(ShopPage.this);
            final JSONObject[] returnObj = {new JSONObject()};

            JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("ZW","Response: " + response);
                    Toast.makeText(ShopPage.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ZW error", "   error");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            queue.add(jsonObjectRequestt);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void editItem(int position){
        String name = examplelist.get(position).getName();
        String type = examplelist.get(position).getType();
        String type_value=examplelist.get(position).getType_value();
        Intent intent = new Intent(ShopPage.this, EditItemActivity.class);
        intent.putExtra(NAME_KEY,name);
        intent.putExtra(TYPE_KEY,type);
        intent.putExtra(TYPE_VALUE_KEY,type_value);
        intent.putExtra(STORE_NAME_KEY, storeName.getText().toString());
        String value = name + "," + type + "," + type_value;
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("target", "deleteitem");
            jsonBody.put("shopname", storeName.getText().toString().toLowerCase());
            jsonBody.put("value", value);

            Log.i("ZW string json to post", jsonBody.toString());

            String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";
            RequestQueue queue = Volley.newRequestQueue(ShopPage.this);
            final JSONObject[] returnObj = {new JSONObject()};

            JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("ZW","Response: " + response);
                    Toast.makeText(ShopPage.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("ZW error", "   error");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            queue.add(jsonObjectRequestt);
        } catch (JSONException e){
            e.printStackTrace();
        }
        startActivity(intent);
    }

    public void buildRecyclerView(final ArrayList<ExampleItem> examplelist){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(examplelist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                examplelist.remove(position);
                mAdapter.notifyDataSetChanged();
                Log.i("ZW_Remove_size", ""+position);
            }
            @Override
            public void onEditClick(int position) {
                editItem(position);
            }
        });
    }
}
