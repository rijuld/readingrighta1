package com.example.mango;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ArrayList<String> numbersList = (ArrayList<String>) getIntent().getSerializableExtra("key");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, numbersList);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a1=numbersList.get(position);
                Intent pieceOfCake = new Intent(favorite.this, MainActivity.class);
                pieceOfCake.putExtra("message_key", a1);
                startActivity(pieceOfCake);
            }


});


    }
}