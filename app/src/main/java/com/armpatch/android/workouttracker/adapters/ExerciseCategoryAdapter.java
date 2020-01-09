package com.armpatch.android.workouttracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armpatch.android.workouttracker.R;
import com.armpatch.android.workouttracker.model.ExerciseCategory;

import java.util.List;

public class ExerciseCategoryAdapter extends RecyclerView.Adapter<ExerciseCategoryAdapter.CategoryHolder> {

    Context activityContext;
    List<ExerciseCategory> categories;

    public ExerciseCategoryAdapter(Context activityContext, List<ExerciseCategory> categories) {
        this.activityContext = activityContext;
        this.categories = categories;
    }

    public void setCategories(List<ExerciseCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activityContext)
                .inflate(R.layout.content_exercise_category, parent, false);

        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int position) {
        categoryHolder.bind(categories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        String name;
        TextView nameTextView;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
        }

        void bind(String name) {
            this.name = name;
            nameTextView.setText(name);
        }
    }
}