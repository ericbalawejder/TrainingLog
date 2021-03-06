package com.armpatch.android.workouttracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.armpatch.android.workouttracker.NumberChooser;
import com.armpatch.android.workouttracker.R;
import com.armpatch.android.workouttracker.model.ExerciseSet;

public class TrackerPagerAdapter extends PagerAdapter {

    private Context activityContext;
    private SetEditorPage setEditorPage;
    private HistoryPage historyPage;

    private String currentDate;
    private String exerciseName;

    public TrackerPagerAdapter(Context activityContext, String currentDate, String exerciseName) {
        this.activityContext = activityContext;
        this.currentDate = currentDate;
        this.exerciseName = exerciseName;
    }

    public void loadHistoryPage() {
        historyPage.refresh();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return activityContext.getString(R.string.tracker_page_title_1);
        } else {
            return activityContext.getString(R.string.tracker_page_title_2);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (object instanceof SetEditorPage) {
            return ((SetEditorPage) object).itemView == view;
        }

        if (object instanceof HistoryPage) {
            return ((HistoryPage) object).itemView == view;
        }

        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position == 0) {
            View itemView = LayoutInflater.from(activityContext).inflate(R.layout.content_set_editor_page, container, false);
            setEditorPage = new SetEditorPage(itemView);
            container.addView(itemView);
            return setEditorPage;
        } else {
            View itemView = LayoutInflater.from(activityContext).inflate(R.layout.content_set_history_page, container, false);
            historyPage = new HistoryPage(itemView);
            container.addView(itemView);
            return historyPage;
        }
    }

    /**
     * This holder contains the tab for adding and modifying sets for the currently selected exercise
     */
    class SetEditorPage implements TrackerSetAdapter.SetSelectionCallback {
        View itemView;

        TrackerSetAdapter trackerSetAdapter;
        RecyclerView setRecycler;
        NumberChooser weightChooser, repsChooser;
        Button addUpdateButton;
        Button deleteButton;

        ExerciseSet selectedSet;

        SetEditorPage(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;

            setupNumberChoosers();
            setupButtons();
            setupRecycler();
        }

        private void setupNumberChoosers() {
            View weightChooserLayout = itemView.findViewById(R.id.weight_picker);
            View repsChooserLayout = itemView.findViewById(R.id.reps_picker);

            weightChooser = new NumberChooser(weightChooserLayout);
            weightChooser.setTitle("Weight (lbs)");
            weightChooser.setIncrement(2.5f);
            repsChooser = new NumberChooser(repsChooserLayout);
            repsChooser.setTitle("Reps");
            repsChooser.setIncrement(1);
        }

        private void setupButtons() {
            addUpdateButton = itemView.findViewById(R.id.update_button);
            addUpdateButton.setOnClickListener(v -> {
                if (selectedSet == null) addExerciseSet(); else updateExerciseSet();
            });

            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setEnabled(false);
            deleteButton.setOnClickListener(v -> deleteSet());
        }

        void setupRecycler() {
            setRecycler = itemView.findViewById(R.id.recycler_view);
            setRecycler.setLayoutManager(new LinearLayoutManager(activityContext));
            trackerSetAdapter = new TrackerSetAdapter(activityContext, exerciseName, currentDate);
            setRecycler.setAdapter(trackerSetAdapter);
            trackerSetAdapter.retrieveSetsFromDatabase();
            trackerSetAdapter.setSelectionCallback(this);
        }

        private void addExerciseSet() {
            float weight = weightChooser.getValue();
            float reps = repsChooser.getValue();

            trackerSetAdapter.addSet(weight, reps);
        }

        private void updateExerciseSet() {
            float weight = weightChooser.getValue();
            float reps = repsChooser.getValue();

            selectedSet.setMeasurement1(weight);
            selectedSet.setMeasurement2(reps);

            trackerSetAdapter.updateSet(selectedSet);
            deselectSet();
        }

        private void deleteSet() {
            trackerSetAdapter.deleteSet(selectedSet);
            deselectSet();
        }

        @Override
        public void onSetHolderClicked(ExerciseSet clickedSet) {
            if (selectedSet == clickedSet) {
                deselectSet();
            } else {
                selectSet(clickedSet);
            }
        }

        private void deselectSet() {
            deleteButton.setEnabled(false);
            addUpdateButton.setText(activityContext.getString(R.string.add_set_button_text));

            trackerSetAdapter.removeAllHighlights();
            selectedSet = null;
        }

        private void selectSet(ExerciseSet set) {
            selectedSet = set;
            deleteButton.setEnabled(true);
            addUpdateButton.setText(activityContext.getString(R.string.update_button_text));
            weightChooser.setValue((int) set.getMeasurement1());
            repsChooser.setValue((int) set.getMeasurement2());
            trackerSetAdapter.highlightSet(set);

        }
    }

    class HistoryPage {
        View itemView;

        RecyclerView recyclerView;
        SetHistoryAdapter setHistoryAdapter;

        public HistoryPage(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;

            recyclerView = itemView.findViewById(R.id.recycler_view);
            setHistoryAdapter = new SetHistoryAdapter(activityContext, exerciseName);
            recyclerView.setLayoutManager(new LinearLayoutManager(activityContext));
            recyclerView.setAdapter(setHistoryAdapter);
        }

        void refresh() {
            setHistoryAdapter.refresh();
        }

    }
}
