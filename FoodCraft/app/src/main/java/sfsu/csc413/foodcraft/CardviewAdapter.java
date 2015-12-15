package sfsu.csc413.foodcraft;

/**
 * This activity is how the card view is created. using the recycler view widget, we are able to create a list of card view objects which
 * takes multiple properties of the recipe object and translates it into a card.
 *
 * @author Robert Chung and Paul Klein
 * @version 1.0 November 15, 2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.ViewHolder> {

    // Initialized context for the application.
    Context mContext;

    // Initialize mItemClickListener to respond to item clicks.
    OnItemClickListener mItemClickListener;

    // Initialized List object called recipeList.
    List<Recipe> recipeList;

    /**
     * Constructor for the card view adapter. It initializes required variables.
     *
     * @param context Information about current application environment.
     * @param ourList Our list of recipes.
     */
    public CardviewAdapter(Context context, List<Recipe> ourList) {
        this.mContext = context;
        recipeList = ourList;
    }

    /**
     * When the view holder is created, load the view from the xml file, activity_cardview.
     *
     * @param parent   the parent activity for which the view is inherited from.
     * @param viewType the type of view used for the layout.
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        return new ViewHolder(view);
    }

    /**
     * The method responsible for assigning each part of the cardview to a property of the recipe. The recipe name and the recipe image are held here to be used in the
     * cardview creating adapter.
     *
     * @param holder   the holder which contains the name and image of the recipe.
     * @param position the position of the recipe in the recipe list.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // where we updated the contents of each card

        final Recipe recipe = recipeList.get(position);

        holder.recipeName.setText(recipe.name);
        ImageLoader mImageLoader = VolleyRequest.getInstance(mContext).getImageLoader();
        holder.recipeImage.setImageUrl(recipe.imageURL, mImageLoader);
    }

    /**
     * Method that obtains the size of the recipe list.
     *
     * @return the size of the recipeList list.
     */
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The linear layout that holds the recipe object.
        public LinearLayout recipeHolder;

        // The linear layout holding the recipe name.
        public LinearLayout recipeNameHolder;

        // The text view inside of the linear layout above.
        public TextView recipeName;

        //public ImageView recipeImage;
        public NetworkImageView recipeImage;

        /**
         * This view holder method contains each of the properties of the recipe. It assigns each of the properties to the appropriate section of the layout.
         *
         * @param itemView The view type of this view holder.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            recipeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            recipeNameHolder = (LinearLayout) itemView.findViewById(R.id.recipeNameHolder);
            recipeImage = (NetworkImageView) itemView.findViewById(R.id.NetworkrecipeImage);
            recipeHolder.setOnClickListener(this);
        }

        /**
         * Method that performs the action when the cards are clicked.
         *
         * @param v The reference to view object.
         */
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    /**
     * Interface invoked when an item in this adapter view has been clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Method that registers the click listener when an item is clicked.
     *
     * @param mItemClickListener callback when an item in an adapter view has been clicked.
     */
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
