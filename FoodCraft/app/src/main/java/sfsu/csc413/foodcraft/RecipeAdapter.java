package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> ourList) {
        this.mContext = context;
        recipeList = ourList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // where we updated the contents of each card

        final Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.name);

        holder.progressBar.setProgress((recipe.matchedingredients/recipe.ingredients.size())*100);
        holder.ingredientText.setText("Matched Ingredients " + recipe.matchedingredients + ". Total "+ recipe.ingredients.size());

        Picasso.with(mContext).load(recipe.getImageResourceId(mContext)).into(holder.recipeImage);

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), recipe.getImageResourceId(mContext));

        // do in background for smooth scrolling
        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int mutedLight = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.recipeNameHolder.setBackgroundColor(mutedLight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout recipeHolder;
        public LinearLayout recipeNameHolder;
        public TextView recipeName;
        public ImageView recipeImage;

        public TextView ingredientText;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeNameHolder = (LinearLayout) itemView.findViewById(R.id.recipeNameHolder);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);

            ingredientText = (TextView) itemView.findViewById(R.id.textProgress);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

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
