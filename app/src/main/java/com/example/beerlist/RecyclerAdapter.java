package com.example.beerlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import punkapi.Beer;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    List<String> nameList;
    List<String> nameListAll;
    List<Beer> beerList;

    Context context;
    private static int pos;

    //Constructor
    public RecyclerAdapter(Context context,List<Beer> beerList, List<String> nameList) {
        this.nameList = nameList;
        nameListAll = new ArrayList<>();
        nameListAll.addAll(nameList);
        this.beerList = beerList;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Beer itemBeer = beerList.get(position);

        holder.textName.setText(itemBeer.getName());
        holder.textTagline.setText(itemBeer.getTagline());

        //Implementation of load image with the API (https://github.com/bumptech/glide)
        Glide.with(context)
                .load(itemBeer.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .into(holder.imageView);

        /* TODO: Star implementation favorite problem in set correct position
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, beerList.get(position).getName(), Toast.LENGTH_SHORT).show();
                if(itemBeer.getIsFavourite()) {
                    view.setBackgroundResource(R.drawable.staroff);
                    itemBeer.setFavourite(false);
                 //   Toast.makeText(context, beerList.get(position).getName(), Toast.LENGTH_SHORT).show();
                }else {
                    view.setBackgroundResource(R.drawable.staron);
                    itemBeer.setFavourite(true);
                    //Toast.makeText(context, beerList.get(position).getName(), Toast.LENGTH_SHORT).show();
                }

            }
        });

         */

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    //TODO: implementation seach
    @Override
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();


            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(nameListAll);

            } else {
                for (String movie: nameListAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);

                    }
                }
            }

            FilterResults filterResults = new FilterResults();

            filterResults.values = filteredList;

            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            nameList.clear();
            nameList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();

        }
    };



     class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageButton imageButton;
        ImageView imageView;
        TextView textName,textTagline;
        boolean isPlay = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textTagline = itemView.findViewById(R.id.textTagline);
            imageButton = itemView.findViewById(R.id.imageButtonFavorite);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), beerList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
            final Intent intent = new Intent(context,BeerDetails.class);
            intent.putExtra("NAME_BEER", beerList.get(getAdapterPosition()).getName());
            intent.putExtra("TAGLINE_BEER", beerList.get(getAdapterPosition()).getTagline());
            intent.putExtra("DESCRIPTION_BEER", beerList.get(getAdapterPosition()).getDescription());
            intent.putExtra("IMAGEURL_BEER", beerList.get(getAdapterPosition()).getImageUrl());
            context.startActivity(intent);
        }


    }


}





