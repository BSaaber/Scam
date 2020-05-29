package e.max_1l.tabbedphmath;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SolutionFragment extends Fragment {
    //ArrayList<String> lines;
    TextView tv;
    public MainActivity mainActivity;
    public TabLayout tab;
    RecyclerView recyclerView;

    public SolutionFragment() {
        // Required empty public constructor

    }

    public void setTab(TabLayout tab) {
        this.tab = tab;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solution, container, false);

        //lines = MainActivity.getLinesList();

        recyclerView = v.findViewById(R.id.recycler_view_history);
        SolutionRecyclerViewAdapter adapter = new SolutionRecyclerViewAdapter(getActivity(), tab);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return v;
    }


}
