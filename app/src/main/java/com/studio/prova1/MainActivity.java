package com.studio.prova1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> lista = new ArrayList<>();
    private List<JSONObject> listObject = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView =  (ListView) findViewById(R.id.listCidade);



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!lista.isEmpty() && !listObject.isEmpty())
            return;

        String url = "https://jsonplaceholder.typicode.com/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                lista.add(jsonObject.getString("username"));
                                listObject.add(jsonObject.getJSONObject("address").getJSONObject("geo"));

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        MainActivity.this,android.R.layout.simple_list_item_1,lista);

                                listView.setAdapter(adapter);

                            }catch (JSONException e){

                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Erro " + error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JSONObject coordenadas = listObject.get(position);
                try {

                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("lat",coordenadas.getString("lat"));
                    intent.putExtra("long",coordenadas.getString("lng"));
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}