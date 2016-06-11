package com.example.caixindong.big_pro.Fragment.Module2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caixindong.big_pro.Adapter.RunItemAdapter;
import com.example.caixindong.big_pro.Bean.Run;
import com.example.caixindong.big_pro.DB.RunRecordManager;
import com.example.caixindong.big_pro.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Record_fragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Run> runs;
    private RunItemAdapter adapter;
    private TextView emptyView;

    public Record_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_02,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycleview);
        emptyView = (TextView)getActivity().findViewById(R.id.emptytextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        RunRecordManager manager = new RunRecordManager(getContext());
        runs = manager.getRunRecords();
        if (runs.size()>0){
            emptyView.setVisibility(View.INVISIBLE);
        }else {
            emptyView.setVisibility(View.VISIBLE);
        }
        adapter = new RunItemAdapter(runs,getContext());
        recyclerView.setAdapter(adapter);

    }
}
