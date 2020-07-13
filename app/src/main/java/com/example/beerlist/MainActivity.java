package com.example.beerlist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import punkapi.Beer;
import punkapi.remote.ControlBeerList;
import punkapi.remote.PunkAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Condition for changing the layout.xml
        if (isNetworkAvailable()){
            setContentView(R.layout.activity_main);
            /*Create handle for the RetrofitInstance interface*/
            PunkAPIService service = ControlBeerList.getPunkAPIClient();

            Call <List<Beer>> call = service.getAllBeer();
            call.enqueue(new Callback<List<Beer>>() {
                @Override
                public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                    generateDataList(response.body());
                }

                @Override
                public void onFailure(Call<List<Beer>> call, Throwable t) {
                }
            });


        }else{
            setContentView(R.layout.layout_no_connection);
        }


    }

    private void generateDataList(List<Beer> beerList) {
        List<String> nameList = new ArrayList<>();
        List<String> taglineList = new ArrayList<>();
        List<String> desDescripList = new ArrayList<>();

        for(Beer beer:beerList){
            nameList.add(beer.getName());
            taglineList.add(beer.getTagline());
            desDescripList.add(beer.getDescription());
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(nameList, taglineList,desDescripList);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItemSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent intent = new Intent(MainActivity.this,Favorite.class);
                startActivity(intent);
                return true;

            case R.id.action_search:

                //Check connection
                if (!isNetworkAvailable()){
                    setContentView(R.layout.layout_no_connection);
                }


            default:

                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //Function to detect whether there is an Internet
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
