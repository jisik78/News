package com.example.recycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.RecycView);

        List<String> datas = new ArrayList<>();

        for(int i = 0; i <100; i++){

            datas.add("视图" + i + "号");
        }

       // rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setLayoutManager(new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false));

        new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false);
        AdapterRecyclerView adapter = new AdapterRecyclerView(datas,this);
        rv.setAdapter(adapter);


    }
}
