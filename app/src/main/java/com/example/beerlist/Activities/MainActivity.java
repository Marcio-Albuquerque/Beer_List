package com.example.beerlist.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beerlist.Adapters.RecyclerAdapter;
import com.example.beerlist.R;

import java.util.ArrayList;
import java.util.List;

import punkapi.Beer;
import punkapi.remote.ControlBeerList;
import punkapi.remote.PunkAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Class that communicates with API and shows in the adaptive list
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView textViewFeedback;
    private ImageView imageViewFeedback;

    private boolean flag_status_list = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Condition for changing the layout.xml
        if (isNetworkAvailable()){
            setContentView(R.layout.activity_main);
            progressBar = findViewById(R.id.progressbar);
            imageViewFeedback = findViewById(R.id.imageViewSearchFeedback);
            textViewFeedback = findViewById(R.id.textViewFeedBack);

            initViews();

            //Create handle for the RetrofitInstance interface
            PunkAPIService service = ControlBeerList.getPunkAPIClient();

            Call <List<Beer>> call = service.getAllBeer();
            call.enqueue(new Callback<List<Beer>>() {
                @Override
                public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                    generateDataList((ArrayList<Beer>) response.body());
                }

                @Override
                public void onFailure(Call<List<Beer>> call, Throwable t) {
                }
            });



        }else{
            setContentView(R.layout.layout_no_connection);
        }


    }

    private void initViews(){
        //Connects the application to the layout and configuration and add data to the RecyclerAdapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void generateDataList(ArrayList<Beer> beerList) {

        mAdapter = new RecyclerAdapter(this, beerList);
        progressBar.setVisibility(View.GONE);
        flag_status_list = true;
        recyclerView.setAdapter(mAdapter);
    }


    //Creating the menu on the ActionBar and configure search
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
                 if(flag_status_list){
                    mAdapter.getFilter().filter(newText);
                    //Check the list after search
                    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                        @Override
                        public void onChanged() {
                            super.onChanged();
                            checkEmpty();
                        }

                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            checkEmpty();
                        }

                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);
                            checkEmpty();
                        }

                        void checkEmpty() {
                            imageViewFeedback.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                            textViewFeedback.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                        }
                    });
                    return true;
                }else{
                    return false;
                }

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Action of the click the menu on the ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item change Activity
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                //Check connection
                if (!isNetworkAvailable()&&flag_status_list){
                      setContentView(R.layout.layout_no_connection);
                }


            default:

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
