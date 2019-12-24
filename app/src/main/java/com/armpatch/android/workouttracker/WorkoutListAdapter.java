package com.armpatch.android.workouttracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class WorkoutListAdapter extends PagerAdapter {

    static final int PAGE_COUNT = 10000;
    static final int STARTING_PAGE = 5000;

    LayoutInflater inflater;

    WorkoutListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.content_workout_exercises, null);

        TextView text = itemView.findViewById(R.id.empty_workout_placeholder);
        text.setText("The position is: " + position);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
