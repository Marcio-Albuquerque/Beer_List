package com.example.beerlist.Adapters;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.beerlist.Activities.BeerDetails;
import com.example.beerlist.Database.FavDB;
import com.example.beerlist.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import punkapi.Beer;

/*
 * Class with the Adaptive Beer of the API
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private ArrayList<Beer> beerList;
    private FavDB favDB;
    private ArrayList<Beer> mFilteredbeerList;
    Context context;


    //Constructor
    public RecyclerAdapter(Context context,ArrayList<Beer> beerList) {
        this.context=context;
        this.beerList = beerList;
        this.mFilteredbeerList = beerList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            favDB = new FavDB(context);
            //Create table on first
            SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            boolean firstStart = prefs.getBoolean("firstStart", true);
            if (firstStart) {
                createTableOnFirstStart();
            }

            View view = layoutInflater.inflate(R.layout.row_item, parent, false);

            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            final Beer itemBeer = mFilteredbeerList.get(position);
            //Read list data for check os favorites item
            readCursorData(itemBeer,holder);

            holder.textName.setText(itemBeer.getName());
            holder.textTagline.setText(itemBeer.getTagline());

            //Implementation of load image with the API (https://github.com/bumptech/glide)
            Glide.with(context)
                    .load(itemBeer.getImageUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .into(holder.imageView);

            //Fix bug RecyclerView adapter showing wrong images in button when use Filter
            holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return mFilteredbeerList.size();
    }

    //Bug fix RecyclerView adapter showing wrong images in button
    @Override
    public int getItemViewType(int position) {
       return position;
    }

    //Bug fix RecyclerView adapter showing wrong images in button
    @Override
    public long getItemId(int position) {

        return position;
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredbeerList = beerList;
                } else {

                    ArrayList<Beer> filteredList = new ArrayList<>();

                    for (Beer beerItem : beerList) {

                        if (beerItem.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(beerItem);
                        }
                    }

                    mFilteredbeerList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredbeerList;
                if(mFilteredbeerList.size()==0){
                    Toast toast = Toast.makeText(context, context.getString(R.string.stringReturnSearch), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredbeerList = (ArrayList<Beer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

     class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        Button favBtn;
        ImageView imageView;
        TextView textName,textTagline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textTagline = itemView.findViewById(R.id.textTagline);
            favBtn = itemView.findViewById(R.id.imageButtonFavorite);


            //Button to favorite list item
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Beer itemBeer = mFilteredbeerList.get(position);
                    //Check status Favorite
                    if (itemBeer.getIsFavourite().equals("0")){
                        itemBeer.setFavourite("1");
                        //Add information in database
                        favDB.insertIntoTheDatabase(itemBeer.getName(),
                                itemBeer.getTagline(),
                                itemBeer.getDescription(),
                                itemBeer.getImageUrl(),
                                itemBeer.getId(),
                                itemBeer.getIsFavourite());
                        //Change background Button
                        favBtn.setBackgroundResource(R.drawable.staron);
                        favBtn.setSelected(true);
                        Toast.makeText(view.getContext(), context.getString(R.string.stringToastAdd) + ": \n" + mFilteredbeerList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }else {
                        //Remove information in database
                        itemBeer.setFavourite("0");
                        favDB.remove_fav(itemBeer.getId());
                        //Change background Button
                        favBtn.setBackgroundResource(R.drawable.staroff);
                        favBtn.setSelected(false);
                        Toast.makeText(view.getContext(), context.getString(R.string.stringToastRemove) + ": \n" + mFilteredbeerList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Send information to the Activity BeerDetails
            final Intent intent = new Intent(context, BeerDetails.class);
            intent.putExtra("NAME_BEER", mFilteredbeerList.get(getAdapterPosition()).getName());
            intent.putExtra("TAGLINE_BEER", mFilteredbeerList.get(getAdapterPosition()).getTagline());
            intent.putExtra("DESCRIPTION_BEER", mFilteredbeerList.get(getAdapterPosition()).getDescription());
            intent.putExtra("IMAGEURL_BEER", mFilteredbeerList.get(getAdapterPosition()).getImageUrl());
            context.startActivity(intent);
        }


    }

    private void createTableOnFirstStart() {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(Beer itemBeer, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(itemBeer.getId());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try{
            while(cursor.moveToNext()){
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                itemBeer.setFavourite(item_fav_status);

                //Check fav status
                if(item_fav_status != null && item_fav_status.equals("1")){
                    viewHolder.favBtn.setBackgroundResource(R.drawable.staron);
                } else if (item_fav_status != null && item_fav_status.equals("0")){
                    viewHolder.favBtn.setBackgroundResource(R.drawable.staroff);
                }
            }
        } finally {
            if(cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }
}





