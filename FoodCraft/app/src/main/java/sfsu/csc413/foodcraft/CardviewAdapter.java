package sfsu.csc413.foodcraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    List<Recipe> recipeList;

    public CardviewAdapter(Context context, List<Recipe> ourList) {
        this.mContext = context;
        recipeList = ourList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // where we updated the contents of each card

        final Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.name);
        Picasso.with(mContext).load(recipe.getImageResourceId(mContext)).into(holder.recipeImage);
        ImageLoader mImageLoader = VolleyRequest.getInstance(mContext).getImageLoader();
        holder.recipeImage.setImageUrl(recipe.imageURL, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout recipeHolder;
        public LinearLayout recipeNameHolder;
        public TextView recipeName;
        //public ImageView recipeImage;
        public NetworkImageView recipeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeNameHolder = (LinearLayout) itemView.findViewById(R.id.recipeNameHolder);
            recipeImage = (NetworkImageView) itemView.findViewById(R.id.NetworkrecipeImage);
            recipeHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
