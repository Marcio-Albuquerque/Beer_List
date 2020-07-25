package com.example.beerlist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.beerlist.R;

import java.util.Objects;

/*
* Class with Beer Details: Name, Tagline, Description and Image URL with API Glide;
* Receives data from previous Activity: Favorite or MainActivity
*/

public class BeerDetails extends AppCompatActivity {
    private ImageView imageViewBeer;
    private TextView textNameBeer,textTaglineBeer,textDescriptionBeer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details);

        //Retrieves information from previous Activity: Favorite or MainActivity
        Intent intent = getIntent();
        String name_beer = intent.getStringExtra("NAME_BEER");
        String tagline_beer = intent.getStringExtra("TAGLINE_BEER");
        String description_beer = intent.getStringExtra("DESCRIPTION_BEER");
        String imageurl_beer = intent.getStringExtra("IMAGEURL_BEER");

        //Connects the application to the layout
        textNameBeer = findViewById(R.id.textNameBeerDetail);
        textTaglineBeer = findViewById(R.id.textTaglineBeerDetail);
        textDescriptionBeer = findViewById(R.id.textDescriptionBeerDetail);
        imageViewBeer = findViewById(R.id.imageViewBeerDetail);

        //Sending the information to the layout
        textNameBeer.setText(name_beer);
        textTaglineBeer.setText(tagline_beer);
        textDescriptionBeer.setText(description_beer);

        //Implementation of load image with the API (https://github.com/bumptech/glide)
        Glide.with(this)
                .load(imageurl_beer)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .into(imageViewBeer);

        //Back button in the title bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //Action back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
