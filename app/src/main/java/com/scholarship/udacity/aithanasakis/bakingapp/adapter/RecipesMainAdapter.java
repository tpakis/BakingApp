package com.scholarship.udacity.aithanasakis.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 3piCerberus on 02/05/2018.
 */

public class RecipesMainAdapter extends RecyclerView.Adapter<RecipesMainAdapter.ResultsHolder> {
    private final RecipesMainAdapterOnClickHandler mClickHandler;
    private List<Recipe> recipesItemsList;
    private Context context;

    public RecipesMainAdapter(RecipesMainAdapterOnClickHandler handler, Context context) {
        mClickHandler = handler;
        this.context=context;
    }

    public interface RecipesMainAdapterOnClickHandler {
        void onClick(Recipe selectedRecipeItem);
    }
    public void setRecipesListToShow(List<Recipe> recipesItemsList) {
        this.recipesItemsList = recipesItemsList;
        notifyDataSetChanged();
    }
    public List<Recipe> getRecipesListShown() {
        return recipesItemsList;
    }

    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_item, parent, false);

        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        Recipe recipeItem = recipesItemsList.get(position);
        holder.recipeName.setText(recipeItem.getName());
        holder.recipeServings.setText(context.getResources().getString(R.string.servings_count,recipeItem.getServings()));
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .dontTransform()
                .placeholder(R.drawable.ic_restaurant_menu_black_24dp)
                .error(R.drawable.ic_restaurant_menu_black_24dp);
        Glide.with(holder.recipeImage.getContext()).load(recipeItem.getImage()).apply(options).into(holder.recipeImage);

    }
    @Override
    public int getItemCount() {
        if (recipesItemsList == null) return 0;
        return recipesItemsList.size();
    }


    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_recipe_name)
        public TextView recipeName;

        @BindView(R.id.textview_servings)
        public TextView recipeServings;

        @BindView(R.id.imageview_recipe)
        public ImageView recipeImage;

        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positionClicked = getAdapterPosition();
            Recipe selectedItem = recipesItemsList.get(positionClicked);
            mClickHandler.onClick(selectedItem);
        }
    }
}
