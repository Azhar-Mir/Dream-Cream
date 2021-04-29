package com.example.dream_cream.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream_cream.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_Recycler_View extends RecyclerView.Adapter<com.example.dream_cream.UI.MyAdapter_Recycler_View.ViewHolder> {
    private List<favouritesClass> horizontalProductModelList;

    public MyAdapter_Recycler_View(List<favouritesClass> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @NonNull
    @Override
    public com.example.dream_cream.UI.MyAdapter_Recycler_View.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.dream_cream.UI.MyAdapter_Recycler_View.ViewHolder holder, int position) {
        favouritesClass horizontalProductModel = horizontalProductModelList.get(position);

        holder.producttitle.setText(horizontalProductModel.getProducttitle());
        holder.productprice.setText(horizontalProductModel.getProductprice());
        Picasso.get().load(horizontalProductModel.getProductimage()).into(holder.productImage);
        holder.checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        return horizontalProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView producttitle, productprice;
        ImageView checkBox;

        public ViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);
            productprice = view.findViewById(R.id.item_Price);
            checkBox = view.findViewById(R.id.check_box);
        }
    }

}