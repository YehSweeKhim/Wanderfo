package com.example.wanderfoapp.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.wanderfoapp.HttpRequest.HandleHTTP;
import com.example.wanderfoapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText nameEditText;
    private Spinner spinner;
    private RelativeLayout valueLayout;
    private Button submitbutton;
    private int stockID;
    private int qtyID;
    private Spinner spinner2;
    private EditText qty;
    private String defaultqty;
    private String defaultstatus;
    private String storeName;
    public static final String STORE_NAME_KEY = "STORE_NAME_KEY";
    public static final String FROME_KEY = "FROM_KEY";
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";

    private ArrayList<String> avail_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        nameEditText = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        submitbutton = findViewById(R.id.submit);
        valueLayout = findViewById(R.id.type_value);

        List<String> categories = new ArrayList<String>();
        categories.add("-");
        categories.add("Stock");
        categories.add("Availability");

        avail_status = new ArrayList<String>();
        avail_status.add("Available");
        avail_status.add("Out of Stock");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        Intent intentMaintoEdit  = getIntent();
        final String name = intentMaintoEdit.getStringExtra(ShopPage.NAME_KEY);
        final String type = intentMaintoEdit.getStringExtra(ShopPage.TYPE_KEY);
        final String type_value = intentMaintoEdit.getStringExtra(ShopPage.TYPE_VALUE_KEY);

        SharedPreferences mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        final String nameStore = mPreferences.getString("STORE_NAME_KEY", "");

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if entry is the same as before
                boolean changedentry = true;
                if(nameEditText.getText().toString().equals(name)&&spinner.getSelectedItem().toString().equals(type)){
                    if(spinner.getSelectedItem().toString().equals("Availability")){
                        if(spinner2.getSelectedItem().toString().equals(type_value)){
                            changedentry=false;
                        }
                    }
                    else if(spinner.getSelectedItem().toString().equals("Stock")){
                        if(qty.getText().toString().equals(type_value)){
                            changedentry=false;
                        }
                    }
                }
                if(changedentry==false){
                    Toast.makeText(EditItemActivity.this, "Same Entry/No changes made",Toast.LENGTH_LONG).show();
                }else {
                    // first, delete old entry from db
                    // then proceed, to resubmitting
                    // check name is not null

                    if (!nameEditText.getText().toString().equals("")) {

                        if (spinner.getSelectedItem().toString().equals("Availability")) {
                            // push these stuff to db
                            Toast.makeText(EditItemActivity.this, "Submitted: " + nameEditText.getText().toString() + ": " + spinner.getSelectedItem().toString() + " - " + spinner2.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                            Log.i("ZW", "" + nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + ",-," + spinner2.getSelectedItem().toString());

                            String itemSet = nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + "," + spinner2.getSelectedItem().toString();
                            try {
                                JSONObject jsonBody = new JSONObject();

                                jsonBody.put("target", "setitem");
                                jsonBody.put("shopname", nameStore.toLowerCase());
                                jsonBody.put("value", itemSet);

                                Log.i("ZW string json to post", jsonBody.toString());

                                HandleHTTP.postHTTP(EditItemActivity.this, jsonBody);
                                Log.i("ZW success post", "ffffff");

                                Intent intentPt = new Intent(EditItemActivity.this, ShopPage.class);
                                startActivity(intentPt);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (spinner.getSelectedItem().toString().equals("Stock")) {
                            if (qty.getText().toString().equals("")) {
                                Toast.makeText(EditItemActivity.this, "Please key in a number for quantity", Toast.LENGTH_SHORT).show();

                            } else {
                                // push stuff to db
                                Toast.makeText(EditItemActivity.this, "Submitted: " + nameEditText.getText().toString() + ": " + spinner.getSelectedItem().toString() + " - " + qty.getText().toString(), Toast.LENGTH_LONG).show();

                                Log.i("ZW", "" + nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + "," + qty.getText().toString());

                                String itemSet = nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + "," + qty.getText().toString();
                                Log.i("ZW edit item set", itemSet);
                                try {
                                    JSONObject jsonBody = new JSONObject();

                                    jsonBody.put("target", "setitem");
                                    jsonBody.put("shopname", nameStore.toLowerCase());
                                    jsonBody.put("value", itemSet);

                                    Log.i("ZW string json to post", jsonBody.toString());

                                    HandleHTTP.postHTTP(EditItemActivity.this, jsonBody);
                                    Log.i("ZW success post", "ffffff");

                                    Intent intentPt = new Intent(EditItemActivity.this, ShopPage.class);
                                    startActivity(intentPt);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (spinner.getSelectedItem().toString().equals("-")) {
                            // push to db with just the name
                            Toast.makeText(EditItemActivity.this, "Submitted: " + nameEditText.getText().toString() + ": " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                            String itemSet = nameEditText.getText().toString() + "," + spinner.getSelectedItem().toString() + "," + spinner.getSelectedItem().toString();
                            try {
                                JSONObject jsonBody = new JSONObject();

                                jsonBody.put("target", "setitem");
                                jsonBody.put("shopname", nameStore.toLowerCase());
                                jsonBody.put("value", itemSet);

                                Log.i("ZW string json to post", jsonBody.toString());

                                HandleHTTP.postHTTP(EditItemActivity.this, jsonBody);
                                Log.i("ZW success post", "ffffff");

                                Intent intentPt = new Intent(EditItemActivity.this, ShopPage.class);
                                startActivity(intentPt);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(EditItemActivity.this, "Please enter a name for your item and contain comma or semi-colon.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nameEditText.setText(name);

        if(type!=null){
            spinner.setSelection(categories.indexOf(type));
            if(type.equals("Stock")){
                defaultqty = type_value;
            }
            else if(type.equals("Availability")){
                defaultstatus = type_value;
            }
        }else{
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this);
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("Stock")){
            valueLayout.removeAllViews();
            qty = new EditText(this);
            qty.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,150));
            this.qtyID = ViewCompat.generateViewId();
            qty.setInputType(InputType.TYPE_CLASS_NUMBER);
            qty.setHint("Quantity");
            qty.setId(this.qtyID);
            qty.setTextColor(Color.BLACK);
            if(defaultqty!=null){
                qty.setText(defaultqty);
            }
            valueLayout.addView(qty);
        }
        else if(item.equals("Availability")){
            valueLayout.removeAllViews();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, avail_status);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner2 = new Spinner(this);
            this.stockID = ViewCompat.generateViewId();
            spinner2.setId(this.stockID);
            // attaching data adapter to spinner
            spinner2.setAdapter(dataAdapter);
            if(defaultstatus!=null){
                spinner2.setSelection(avail_status.indexOf(defaultstatus));
            }
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