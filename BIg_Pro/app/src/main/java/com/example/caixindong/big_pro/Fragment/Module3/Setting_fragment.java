package com.example.caixindong.big_pro.Fragment.Module3;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.example.caixindong.big_pro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setting_fragment extends Fragment {

    public static final String WEIGHT_VALUE = "weight_value";

    public static final String HEIGHT_VALUE = "height_value";

    public static final String SENSITIVITY_VALUE = "sensitivity_value";

    public static final String SETP_SHARED_PREFERENCES = "setp_shared_preferences";

    public static SharedPreferences sharedPreferences;

    private Editor editor;

    private TextView tv_sensitivity_vlaue;
    private TextView tv_step_length_vlaue;
    private TextView tv_weight_value;

    private SeekBar sb_sensitivity;
    private SeekBar sb_step_length;
    private SeekBar sb_weight;

    private Button saveBtn;

    private int sensitivity = 0;
    private int height = 0;
    private int weight = 0;

    public Setting_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_layout_03,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_sensitivity_vlaue = (TextView)getActivity()
                .findViewById(R.id.sensitivity_value);
        tv_step_length_vlaue = (TextView)getActivity()
                .findViewById(R.id.step_lenth_value);
        tv_weight_value = (TextView)getActivity().findViewById(R.id.weight_value);

        sb_sensitivity = (SeekBar)getActivity().findViewById(R.id.sensitivity);
        sb_step_length = (SeekBar)getActivity().findViewById(R.id.step_lenth);
        sb_weight = (SeekBar)getActivity().findViewById(R.id.weight);

        saveBtn = (Button)getActivity().findViewById(R.id.save);

        if (sharedPreferences == null) {
            sharedPreferences = getActivity().getSharedPreferences(SETP_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
        }

        editor = sharedPreferences.edit();

        sensitivity = sharedPreferences.getInt(SENSITIVITY_VALUE,3);
        height = sharedPreferences.getInt(HEIGHT_VALUE,150);
        weight =sharedPreferences.getInt(WEIGHT_VALUE,50);

        sb_sensitivity.setProgress(sensitivity);
        sb_step_length.setProgress((height - 40) / 5);
        sb_weight.setProgress((weight - 30) / 2);

        tv_sensitivity_vlaue.setText(sensitivity + "");
        tv_step_length_vlaue.setText(height + getString(R.string.cm));
        tv_weight_value.setText(weight + getString(R.string.kg));

        sb_sensitivity
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // TODO Auto-generated method stub
                        sensitivity = progress;
                        tv_sensitivity_vlaue.setText(sensitivity + "");
                    }
                });

        sb_step_length
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        System.out.println(progress);
                        height = progress * 5 + 40;
                        tv_step_length_vlaue.setText(height
                                + getString(R.string.cm));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        sb_weight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                weight = progress * 2 + 30;
                tv_weight_value.setText(weight + getString(R.string.kg));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt(WEIGHT_VALUE,weight);
                editor.putInt(HEIGHT_VALUE,height);
                editor.putInt(SENSITIVITY_VALUE,sensitivity);
                editor.commit();
                Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
