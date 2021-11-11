package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HusList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_list);

        ListView listView = (ListView) findViewById(R.id.list_view_hus);

        DBHandler db = new DBHandler(this);
        List<Hus> restaurantList = db.findAllRestauranter();

        RestaurantListAdapter adapter = new RestaurantListAdapter(this, R.layout.list_item_restauranter, restaurantList);
        listView.setAdapter(adapter);


        toolbarButtons();
    }

    private void toolbarButtons(){
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestauranterList.this, MainActivity.class));
                finishAffinity();
            }
        });

        toolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestauranterList.this, Restauranter.class));
                finish();
            }
        });

        ivPreferanser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestauranterList.this, Preferanser.class));
            }
        });
    }

}
