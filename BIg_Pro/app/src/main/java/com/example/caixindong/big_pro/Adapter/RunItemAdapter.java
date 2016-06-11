package com.example.caixindong.big_pro.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.example.caixindong.big_pro.Bean.Run;
import com.example.caixindong.big_pro.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by caixindong on 16/6/9.
 */
public class RunItemAdapter extends RecyclerView.Adapter{

    private List<Run> runs;
    private Context context;

    public RunItemAdapter(List<Run> runs, Context context) {
        this.runs = runs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layour,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder)holder;
        Run run = runs.get(position);
        holder1.kcalLabel.setText(formatDouble(run.getCalories()));
        holder1.distanceLabel.setText("距离："+formatDouble(run.getDistance())+"m");
        holder1.speedLabel.setText("速度："+formatDouble(run.getVelocity())+"m/s");
        holder1.durLabel.setText("时长："+getFormatTime(run.getTimer()));
        holder1.stepLabel.setText("步数："+run.getStepCount());
        holder1.timeLabel.setText(run.getStartTime());
    }



    @Override
    public int getItemCount() {
        return runs.size();
    }

   public class MyViewHolder extends ViewHolder{
        public TextView kcalLabel;
        public TextView distanceLabel;
        public TextView speedLabel;
        public TextView durLabel;
        public TextView stepLabel;
        public TextView timeLabel;

        public MyViewHolder(View v){
            super(v);
            kcalLabel = (TextView)v.findViewById(R.id.item_kcal);
            distanceLabel = (TextView)v.findViewById(R.id.item_distance);
            speedLabel = (TextView)v.findViewById(R.id.item_speed);
            durLabel = (TextView)v.findViewById(R.id.item_dur);
            stepLabel = (TextView)v.findViewById(R.id.item_step);
            timeLabel = (TextView)v.findViewById(R.id.item_starttime);
        }
    }

    /**
     * 得到一个格式化的时间
     *
     * @param time
     *            时间 毫秒
     * @return 时：分：秒：毫秒
     */
    private String getFormatTime(long time) {
        time = time / 1000;
        long second = time % 60;
        long minute = (time % 3600) / 60;
        long hour = time / 3600;

        // 毫秒秒显示两位
        // String strMillisecond = "" + (millisecond / 10);
        // 秒显示两位
        String strSecond = ("00" + second)
                .substring(("00" + second).length() - 2);
        // 分显示两位
        String strMinute = ("00" + minute)
                .substring(("00" + minute).length() - 2);
        // 时显示两位
        String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

        return strHour + ":" + strMinute + ":" + strSecond;
        // + strMillisecond;
    }

    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.0"
                : distanceStr;
    }
}
