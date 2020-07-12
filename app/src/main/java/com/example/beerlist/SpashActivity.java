package com.example.beerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class SpashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("logo.png"));
        } catch (IOException e) {
            Log.e("READ ASSESTS", "Error reading assets", e);
        }

        // showing image
        ImageView imageView = findViewById(R.id.imageViewLogo);
        imageView.setImageBitmap(bitmap);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
