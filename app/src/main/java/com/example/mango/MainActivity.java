package com.example.mango;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;//button is a?
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //main starts as soon as the program starts and calls first functions
//onCreate starts as soon as the program starts
    Button b1;
    Button b2;
    FloatingActionButton b3;
    TextView t1;
    TextView t4;
    TextView t5;
    ImageView i1;
    boolean firstStart= true;
    ArrayList<String> fav = new ArrayList<String>(); // Create an ArrayList object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the intent, verify the action and get the query

        //add a button here
        b1= (Button) findViewById(R.id.b1);
        b2= (Button) findViewById(R.id.b2);
        b3= (FloatingActionButton) findViewById(R.id.b3);
        t1=(TextView) findViewById((R.id.t1));
        t4=(TextView) findViewById((R.id.t4));
        t5=(TextView) findViewById((R.id.t5));
        i1=(ImageView) findViewById(R.id.i1) ;

        if(firstStart) {
            randommeal();
        }
        handleIntent(getIntent());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randommeal();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.add((String) t1.getText());
                saveData();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, favorite.class);
                intent.putExtra("key", fav);
                startActivity(intent);
            }
        });

    }
    private void saveData(){
        SharedPreferences sharedPreferences= getSharedPreferences("Shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(fav);
        editor.putString("fav list",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences= getSharedPreferences("Shared preferences",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("fav list",null);
        Type type=new TypeToken<ArrayList<String>>() {}.getType();
        fav=gson.fromJson(json, type);
        if (fav==null){
            fav=new ArrayList<>();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            searchmeal(query);

        }
        else{
            String str = intent.getStringExtra("message_key");
            searchmeal(str);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }
    void randommeal(){
        String a="https://www.themealdb.com/api/json/v1/1/random.php";
        String apikey="1";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, a, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //show that response of the screen
                try {
                    StringBuilder sb1 = new
                            StringBuilder("Ingredients : ");
                    for(int i=1;i<=20;i++){
                        String t=response.getJSONArray("meals").getJSONObject(0).getString(String.format("strIngredient%d",i));
                        if(t!=null){
                            sb1.append(t+",");
                        }
                    }
                    t5.setText(sb1);
                    t1.setText(response.getJSONArray("meals").getJSONObject(0).getString("strMeal"));
                    t4.setText("Recipe : "+response.getJSONArray("meals").getJSONObject(0).getString("strInstructions"));
                    //response.getJSONArray("meals").getJSONObject(0).getString("strCategory")
                    Glide
                            .with(MainActivity.this)
                            .load(response.getJSONArray("meals").getJSONObject(0).getString("strMealThumb"))
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(i1);
                } catch (JSONException e) {
                    Toast errorToast= Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast errorToast= Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG);
                errorToast.show();
            }
        });

        queue.add(request);
        firstStart=false;
    }
    void searchmeal(String o){
        String a=String.format("https://www.themealdb.com/api/json/v1/1/search.php?s=%1$s",o);
        String apikey="1";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, a, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //show that response of the screen
                try {
                    StringBuilder sb1 = new
                            StringBuilder("");
                    for(int i=1;i<=20;i++){
                        String t=response.getJSONArray("meals").getJSONObject(0).getString(String.format("strIngredient%d",i));
                        if(t!=null){
                            sb1.append(t);
                        }
                    }
                    t5.setText(sb1);
                    t1.setText(response.getJSONArray("meals").getJSONObject(0).getString("strMeal"));
                    t4.setText(response.getJSONArray("meals").getJSONObject(0).getString("strInstructions"));
                    Glide
                            .with(MainActivity.this)
                            .load(response.getJSONArray("meals").getJSONObject(0).getString("strMealThumb"))
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(i1);
                } catch (JSONException e) {
                    Toast errorToast= Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast errorToast= Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG);
                errorToast.show();
            }
        });

        queue.add(request);
    }


}
/*Java.lang.Object
   ↳	android.content.Context
 	   ↳	android.content.ContextWrapper
 	 	   ↳	android.view.ContextThemeWrapper
 	 	 	   ↳	android.app.Activity
 	 	 	 	   ↳	androidx.activity.ComponentActivity
 	 	 	 	 	   ↳	androidx.fragment.app.FragmentActivity
 	 	 	 	 	 	   ↳	androidx.appcompat.app.AppCompatActivity(class)*/