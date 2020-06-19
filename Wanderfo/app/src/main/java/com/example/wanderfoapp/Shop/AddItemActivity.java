package com.example.wanderfoapp.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.wanderfoapp.Government.Announcement;
import com.example.wanderfoapp.HttpRequest.HandleHTTP;
import com.example.wanderfoapp.Home.Home;
import com.example.wanderfoapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText nameEditText;
    private Spinner spinner;
    private RelativeLayout valueLayout;
    private Button submitbutton;
    private int stockID;
    private int qtyID;
    private Spinner spinner2;
    private EditText qty;

    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameEditText = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        submitbutton = findViewById(R.id.submit);
//        storeName = MainActivity.STORE_NAME;

        List<String> categories = new ArrayList<String>();
        categories.add("-");
        categories.add("Stock");
        categories.add("Availability");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        final String nameStore = mPreferences.getString("STORE_NAME_KEY", "");

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check name is not null
                if(!nameEditText.getText().toString().equals("")){
                    if(spinner.getSelectedItem().toString().equals("Availability")) {
                        // push these stuff to db
                        Toast.makeText(AddItemActivity.this, "Submitted: "+ nameEditText.getText().toString()+": "+spinner.getSelectedItem().toString()+" - "+ spinner2.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                        String itemSet =  nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + "," + spinner2.getSelectedItem().toString();
                        try {
                            JSONObject jsonBody = new JSONObject();

                            jsonBody.put("target", "setitem");
                            jsonBody.put("shopname", nameStore.toLowerCase());
                            jsonBody.put("value", itemSet);

                            Log.i("ZW string json to post", jsonBody.toString());

                            HandleHTTP.postHTTP(AddItemActivity.this, jsonBody);
                            Log.i("ZW success post", "ffffff");

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    else if(spinner.getSelectedItem().toString().equals("Stock")){
                        if(qty.getText().toString().equals("")){
                            Toast.makeText(AddItemActivity.this, "Please key in a number for quantity",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // push stuff to db
                            Toast.makeText(AddItemActivity.this, "Submitted: "+nameEditText.getText().toString()+": "+ spinner.getSelectedItem().toString()+" - "+qty.getText().toString(),Toast.LENGTH_LONG).show();

                            String itemSet =  nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString()+ "," + qty.getText().toString();
                            try {
                                JSONObject jsonBody = new JSONObject();

                                jsonBody.put("target", "setitem");
                                jsonBody.put("shopname", nameStore.toLowerCase());
                                jsonBody.put("value", itemSet);

                                Log.i("ZW string json to post", jsonBody.toString());

                                HandleHTTP.postHTTP(AddItemActivity.this, jsonBody);
                                Log.i("ZW success post", "ffffff");

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(spinner.getSelectedItem().toString().equals("-")){
                        // push to db with just the name
                        Toast.makeText(AddItemActivity.this, "Submitted: "+ nameEditText.getText().toString()+": "+spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                        String itemSet =  nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + ",-" ;
                        try {
                            JSONObject jsonBody = new JSONObject();

                            jsonBody.put("target", "setitem");
                            jsonBody.put("shopname", nameStore.toLowerCase());
                            jsonBody.put("value", itemSet);

                            Log.i("ZW string json to post", jsonBody.toString());

                            HandleHTTP.postHTTP(AddItemActivity.this, jsonBody);
                            Log.i("ZW success post", "ffffff");

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(AddItemActivity.this, "Please enter a name for your item",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(AddItemActivity.this, ShopPage.class);
                startActivity(intent);
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

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
        valueLayout = findViewById(R.id.type_value);
        if(item.equals("Stock")){
            valueLayout.removeAllViews();
            qty = new EditText(this);
            qty.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,150));
            this.qtyID = ViewCompat.generateViewId();
            qty.setInputType(InputType.TYPE_CLASS_NUMBER);
            qty.setHint("Quantity");
            qty.setId(this.qtyID);
            qty.setTextColor(Color.BLACK);
            valueLayout.addView(qty);
        }
        else if(item.equals("Availability")){
            valueLayout.removeAllViews();
            List<String>  avail_status = new ArrayList<String>();
            avail_status.add("Available");
            avail_status.add("Out of Stock");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, avail_status);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner2 = new Spinner(this);
            this.stockID = ViewCompat.generateViewId();
            spinner2.setId(this.stockID);
            // attaching data adapter to spinner
            spinner2.setAdapter(dataAdapter);
            valueLayout.addView(spinner2);
        }
        else if(item.equals("-")){
            valueLayout.removeAllViews();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
