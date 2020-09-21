package com.example.userdisplayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://randomuser.me/api/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject results = null;
                try {
                    results = response.getJSONArray("results").getJSONObject(0);
                    Log.d("myApp", "Phase 1 complete");
                    JSONObject picture = null;
                    picture = results.getJSONObject("picture");
                    final ImageView imageView = findViewById(R.id.imageView);
                    String pic = null;
                    pic = picture.getString("large");
                    Log.d("myApp", "Phase 2 complete");
                    RequestQueue imageRequestQueue = Volley.newRequestQueue(MainActivity.this);
                    ImageRequest imageRequest = new ImageRequest(pic, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imageView.setImageBitmap(response);
                        }
                    }, 0, 0, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("myApp", "Something went wrong");
                        }
                    });
                    imageRequestQueue.add(imageRequest);
                    Log.d("myApp", pic);
                    Log.d("myApp", "Phase 3 complete");
                    ArrayList<String> details = new ArrayList<>();
                    JSONObject name = results.getJSONObject("name");
                    details.add("Name: " + name.getString("title") + ". " + name.getString("first") + " " + name.getString("last"));
                    details.add("Gender: " + results.getString("gender"));
                    details.add("Email: " + results.getString("email"));
                    JSONObject dob = results.getJSONObject("dob");
                    details.add("Date of Birth: " + dob.getString("date"));
                    details.add("Age: " + dob.getString("age"));
                    details.add("Phone: " + results.getString("phone"));
                    details.add(("Cell: " + results.getString("cell")));
                    Log.d("myApp", "Phase 4 complete");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, details);
                    ListView listView = findViewById(R.id.info);
                    listView.setAdapter(arrayAdapter);
                    Log.d("myApp", "Phase 5 complete");
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("myApp", "Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myApp", "Something went wrong");
            }
        });

        requestQueue.add(jsonObjectRequest);
        Log.d("myApp", "Mission Successful");
    }
}