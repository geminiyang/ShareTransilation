package com.yqq.touchtest.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yqq.touchtest.Data.Cheeses;
import com.yqq.touchtest.R;

import java.util.List;

/**
 * Created by user on 2017/2/23.
 */

public class SwipeRefreshLayoutFragment extends Fragment {

    private static final int LIST_ITEM_COUNT = 20;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView listView;

    private ArrayAdapter<String> myListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //告诉系统允许这个fragment拥有选项菜单
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);

        // Retrieve the SwipeRefreshLayout and ListView instances
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        // BEGIN_INCLUDE (change_colors)
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        swipeRefreshLayout.setColorScheme(getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2), getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4));
        // END_INCLUDE (change_colors)

        // Retrieve the ListView
        listView = (ListView) view.findViewById(android.R.id.list);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Create an ArrayAdapter to contain the data for the ListView. Each item in the ListView
         * uses the system-defined simple_list_item_1 layout that contains one TextView.
         */
        myListAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Cheeses.randomList(LIST_ITEM_COUNT));

        // Set the adapter between the ListView and its backing data.
        listView.setAdapter(myListAdapter);

        // BEGIN_INCLUDE (setup_refreshlistener)
        /**
         * Implement {@link SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.swipefragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:

                // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }

                // Start our refresh background task
                initiateRefresh();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateRefresh() {
        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        new DummyBackgroundTask().execute();
    }

    /**
     * 处理耗时任务
     */
    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a new random list of cheeses
            return Cheeses.randomList(LIST_ITEM_COUNT);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            //告诉Fragment刷新已经完成
            onRefreshComplete(result);
        }

    }

    private void onRefreshComplete(List<String> result) {
        // Remove all items from the ListAdapter, and then replace them with the new items
        myListAdapter.clear();
        for (String cheese : result) {
            myListAdapter.add(cheese);
        }

        // Stop the refreshing indicator
        swipeRefreshLayout.setRefreshing(false);
    }
}
