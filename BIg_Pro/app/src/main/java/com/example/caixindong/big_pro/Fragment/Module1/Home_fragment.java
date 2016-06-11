package com.example.caixindong.big_pro.Fragment.Module1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.caixindong.big_pro.DB.RunRecordManager;
import com.example.caixindong.big_pro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_fragment extends Fragment {

    private Button goBtn;
    private TextView distanceLabel;
    private TextView countLabel;
    private TextView speedLabel;

    public Home_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_01,null);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        goBtn = (Button)getActivity().findViewById(R.id.gobtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), RunActivity.class);
                startActivity(intent);
            }
        });

        distanceLabel = (TextView)getActivity().findViewById(R.id.distanceLabel);
        speedLabel = (TextView)getActivity().findViewById(R.id.speedLabel);
        countLabel = (TextView)getActivity().findViewById(R.id.countLabel);
    }

    @Override
    public void onResume() {
        super.onResume();
        RunRecordManager manager = new RunRecordManager(getContext());
        distanceLabel.setText(manager.totalDistance()*0.001+"");
        countLabel.setText(manager.runCount()+"");
        speedLabel.setText(manager.advSpeed()+"m/s");

    }
}
