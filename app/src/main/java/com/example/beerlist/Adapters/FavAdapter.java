package com.example.beerlist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.beerlist.Activities.BeerDetails;
import com.example.beerlist.Database.FavDB;
import com.example.beerlist.Entity.FavItem;
import com.example.beerlist.R;

import java.util.List;

/*
* Class with the Adaptive Favorites List
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private Context context;
    private List<FavItem> favItemList;
    private FavDB favDB;

    //Constructor
    public FavAdapter(Context context, List<FavItem> favItemList) {
        this.context = context;
        this.favItemList = favItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB(context);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fav_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FavItem itemBeerFav = favItemList.get(position);

        holder.favTextName.setText(itemBeerFav.getItem_name());
        holder.favTextTagline.setText(itemBeerFav.getItem_tagline());

        //Implementation of load image with the API (https://github.com/bumptech/glide)
        Glide.with(context)
                .load(itemBeerFav.getItem_image())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .into(holder.favImageView);

    }

    @Override
    public int getItemCount() {
        return favItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button favBtn;
        ImageView favImageView;
        TextView favTextName,favTextTagline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favImageView = itemView.findViewById(R.id.imageViewFav);
            favTextName = itemView.findViewById(R.id.textNameFav);
            favTextTagline = itemView.findViewById(R.id.textTaglineFav);
            favBtn = itemView.findViewById(R.id.imageButtonFavoriteFav);

            //Remove from after click
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavItem favItem = favItemList.get(position);
                    Toast.makeText(view.getContext(), context.getString(R.string.stringToastRemove) + ": \n" + favItemList.get(position).getItem_name(), Toast.LENGTH_SHORT).show();
                    favDB.remove_fav(favItem.getKey_id());
                    removeItem(position);
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Send information to the Activity BeerDetails
            final Intent intent = new Intent(context, BeerDetails.class);
            intent.putExtra("NAME_BEER", favItemList.get(getAdapterPosition()).getItem_name());
            intent.putExtra("TAGLINE_BEER", favItemList.get(getAdapterPosition()).getItem_tagline());
            intent.putExtra("DESCRIPTION_BEER", favItemList.get(getAdapterPosition()).getItem_description());
            intent.putExtra("IMAGEURL_BEER", favItemList.get(getAdapterPosition()).getItem_image());
            context.startActivity(intent);

        }
    }
    //Function to remove favorite for list
    private void removeItem(int position) {
        favItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,favItemList.size());
    }
}
