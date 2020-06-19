package com.example.wanderfoapp.Shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.Home.Home;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAccess extends AppCompatActivity {

    EditText requestInp;
    Button buttonSendReq;
    TextView storeName;

    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denied_access);

        requestInp = findViewById(R.id.requestInp);
        buttonSendReq = findViewById(R.id.buttonSendReq);
        storeName = findViewById(R.id.storeNameDenied);
        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String nameStore = mPreferences.getString("STORE_NAME_KEY", "");
        storeName.setText(nameStore);

        buttonSendReq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("ZW", requestInp.getText().toString());
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("target", "setbio");
                    jsonBody.put("shopname", "breadtalk");
                    jsonBody.put("value", "fheufwhwefwf");
                } catch (JSONException e){
                    e.printStackTrace();
                }
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
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });













    }
}
