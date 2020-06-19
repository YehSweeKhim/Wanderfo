package com.example.wanderfoapp.HttpRequest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandleHTTP {

    private static String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";

    public static String[] getHTTP(Context context, String val){
        final String[] returnResponse = new String[1];
        RequestQueue queue = Volley.newRequestQueue(context);
        url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/"+ val;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("ZW","Response is: "+ response);
                        returnResponse[0] = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ZW", "That didn't work!");
                Log.i("ZW", error.getMessage());
            }
        });
        queue.add(stringRequest);
        Log.i("handlehttp", Arrays.toString(returnResponse));
        return returnResponse;
    }

    public static JSONObject postHTTP(final Context context, JSONObject jsonObject){
        String url = "https://da8sv4lct2.execute-api.us-east-1.amazonaws.com/numOne/shop/user/";
        RequestQueue queue = Volley.newRequestQueue(context);
        final JSONObject[] returnObj = {new JSONObject()};

        JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ZW","Response: " + response);
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                returnObj[0] = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ZW error", error.getMessage());
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
        return returnObj[0];
    }
}
