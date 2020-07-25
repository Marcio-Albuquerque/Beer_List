package com.example.beerlist.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beerlist.Adapters.FavAdapter;
import com.example.beerlist.Database.FavDB;
import com.example.beerlist.Entity.FavItem;
import com.example.beerlist.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* Class that collects data from the database and sends it to the adaptive list
*/

public class Favorite extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavDB favDB;
    private List<FavItem> favItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //Back button in the title bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Database object
        favDB = new FavDB(this);

        //Connects the application to the layout and configuration the FavAdapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Loading of data
        loadData();

    }

    private void loadData() {
        //Check list
        if (favItemList != null) {
            favItemList.clear();
        }
        //Collects data from the database
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_NAME));
                String tagline = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_TAGLINE));
                String description = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_DESCRIPTION));
                String id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));
                String urlimagem = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGEURL));

                //Add data to the Entity - FavItem
                FavItem favItem = new FavItem(name, tagline, description,id,urlimagem);
                favItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        //Add data to the Adapter - FavAdapter
        recyclerView.setAdapter(new FavAdapter(this, favItemList));

    }

    //Action back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Reload Intent for updated list remove
                Intent intent = new Intent(this, MainActivity.class);
                NavUtils.navigateUpTo(this, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
