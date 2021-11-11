package com.example.apputviklingmappe3_s344106_s344082;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HusList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_list);

        ListView listView = (ListView) findViewById(R.id.list_view_hus);

        DBHandler db = new DBHandler(this);
        List<Hus> husList = db.findAllHus();

        HusListAdapter adapter = new HusListAdapter(this, R.layout.list_item_hus, husList);
        listView.setAdapter(adapter);
    }

}
