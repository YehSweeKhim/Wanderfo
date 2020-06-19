package com.example.wanderfoapp.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.Home.Home;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button buttonLogin;
    EditText userName;
    EditText userPw;

    public static final String STORE_NAME_KEY = "STORE_NAME_KEY";
    public static final String PERMISSION_KEY = "PERMISSON_KEY";
    public static final String STATUS_KEY = "STATUS_KEY";
    public static final String BIO_KEY = "BIO_KEY";

    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    public static final String ITEM_KEY = "ITEM_KEY";
    public static final String FROM_KEY = "FROM_KEY";

    SharedPreferences mPreferences;
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(STORE_NAME_KEY, userName.getText().toString());
        preferencesEditor.apply();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.username);
        userPw = findViewById(R.id.password);

        buttonLogin =findViewById(R.id.butttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ZW", userName.getText().toString());
                Log.i("ZW", userPw.getText().toString());

                mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

                // Request a string response from the provided URL.
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                String  newUrl =  url + "getshopdetail/" + userName.getText().toString().toLowerCase();

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

                            Log.i("ZW", "hashmap permission" + hMapData.get("permission"));
                            Log.i("ZW", "hashmap status" + hMapData.get("status"));
                            Log.i("ZW", "hashmap category "+ hMapData.get("category") );

                            if(hMapData.get("permission").equals("0")){
                                Intent intentToDenied = new Intent(Login.this, RequestAccess.class);
                                intentToDenied.putExtra(STORE_NAME_KEY, userName.getText().toString());
                                intentToDenied.putExtra(FROM_KEY, "Login");
                                startActivity(intentToDenied);
                            } else {
                                //TODO get the list of available food items (?) and pass over the arraylist
                                Intent intentToEdit = new Intent(Login.this, ShopPage.class);
                                intentToEdit.putExtra(STORE_NAME_KEY, userName.getText().toString());
                                intentToEdit.putExtra(PERMISSION_KEY, "ALLOW");
                                intentToEdit.putExtra(STATUS_KEY, hMapData.get("status"));
                                intentToEdit.putExtra(BIO_KEY, hMapData.get("bio"));
                                intentToEdit.putExtra(ITEM_KEY, hMapData.get("item"));
                                startActivity(intentToEdit);
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
            }
        });

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
                        return true;
                }
                return false;
            }
        });
    }
}
