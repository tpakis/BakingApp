package com.scholarship.udacity.aithanasakis.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scholarship.udacity.aithanasakis.bakingapp.R;
import com.scholarship.udacity.aithanasakis.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 3piCerberus on 04/05/2018.
 */

public class RecipeStepsAdapter  extends RecyclerView.Adapter<RecipeStepsAdapter.ResultsHolder> {

    private final RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler mClickHandler;
    private List<Step> stepsItemsList;

    public interface RecipeStepsAdapterOnClickHandler {
        void onClick(Step selectedStepItem);
    }
    public RecipeStepsAdapter(RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }



    public void setStepsListToShow(List<Step> stepsItemsList) {
        this.stepsItemsList = stepsItemsList;
        notifyDataSetChanged();
    }
    public List<Step> getStepsListShown() {
        return stepsItemsList;
    }

    @Override
    public RecipeStepsAdapter.ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_item, parent, false);

        return new RecipeStepsAdapter.ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeStepsAdapter.ResultsHolder holder, int position) {
        Step stepItem = stepsItemsList.get(position);
        holder.stepNumber.setText(String.valueOf(position+1));
        holder.stepName.setText(stepItem.getShortDescription());

    }
    @Override
    public int getItemCount() {
        if (stepsItemsList == null) return 0;
        return stepsItemsList.size();
    }
    
    
    
    
    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_step_number)
        public TextView stepNumber;

        @BindView(R.id.textview_step_name)
        public TextView stepName;

        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int positionClicked = getAdapterPosition();
            Step selectedItem = stepsItemsList.get(positionClicked);
            mClickHandler.onClick(selectedItem);
        }
    }
}
