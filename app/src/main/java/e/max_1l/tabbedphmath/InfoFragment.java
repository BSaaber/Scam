package e.max_1l.tabbedphmath;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    public String text = "gg";
    public int k = 0;

    public ArrayList<String> lines = new ArrayList<>();


    public InfoFragment() {
        // Required empty public constructor
        Log.d("eq1", "fragment created");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        //инициализируем RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        MRecyclerViewAdapter adapter = new MRecyclerViewAdapter(getActivity(), lines);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        return v;
    }



}
