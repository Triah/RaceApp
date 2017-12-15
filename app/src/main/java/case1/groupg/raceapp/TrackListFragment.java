package case1.groupg.raceapp;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class TrackListFragment extends Fragment {

    RecyclerView recyclerView;
    TrackListAdapter listAdapter;
    RecyclerView.LayoutManager layoutManager;

    public TrackListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_list, container, false);

        listAdapter = new TrackListAdapter();
        recyclerView = view.findViewById(R.id.track_list_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        return view;
    }
}
