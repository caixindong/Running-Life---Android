package com.example.caixindong.big_pro.Fragment.Module1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.caixindong.big_pro.Bean.Run;
import com.example.caixindong.big_pro.Service.StepDetector;
import com.example.caixindong.big_pro.DB.RunRecordManager;
import com.example.caixindong.big_pro.Fragment.Module3.Setting_fragment;
import com.example.caixindong.big_pro.R;
import com.example.caixindong.big_pro.Service.StepCounterService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RunActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener{

    //定义文本框控件
    private TextView tv_show_step;// 步数

    private TextView tv_timer;//  运行时间

    private TextView tv_distance;// 行程
    private TextView tv_calories;// 卡路里
    private TextView tv_velocity;// 速度

    private Button btn_start;// 开始按钮
    private Button btn_stop;// 停止按钮
    private Button btn_pause;//暂停按钮

    private Button btn_showMap;
    private Button btn_hideMap;

    private long timer = 0;// 运动时间
    private  long startTimer = 0;// 开始时间

    private  long tempTime = 0;

    private Double distance = 0.0;// 路程：米
    private Double calories = 0.0;// 热量：卡路里
    private Double velocity = 0.0;// 速度：米每秒

    private Double step_length = 0.0;  //步长
    private int weight = 0;       //体重
    private int total_step = 0;   //走的总步数

    private Thread thread;  //定义线程对象

    private MapView mapView;
    private AMap aMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private String currentTime = "";

    private Boolean isFirstLatLng = true;

    private LatLng oldLatLng;

    Handler handler = new Handler() {// Handler对象用于更新当前步数,定时发送消息，调用方法查询数据用于显示？？？？？？？？？？
        //主要接受子线程发送的数据, 并用此数据配合主线程更新UI
        //Handler运行在主线程中(UI线程中), 它与子线程可以通过Message对象来传递数据,
        //Handler就承担着接受子线程传过来的(子线程用sendMessage()方法传递Message对象，(里面包含数据)
        //把这些消息放入主线程队列中，配合主线程进行更新UI。

        @Override                  //这个方法是从父类/接口 继承过来的，需要重写一次
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);        // 此处可以更新UI

            countDistance();     //调用距离方法，看一下走了多远

            if (timer != 0 && distance != 0.0) {

                // 体重、距离
                // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
                calories = weight * distance * 0.001*1.036;
                //速度velocity
                velocity = distance * 1000 / timer;
            } else {
                calories = 0.0;
                velocity = 0.0;
            }

            countStep();          //调用步数方法

            tv_show_step.setText(total_step + "");// 显示当前步数

            tv_distance.setText(formatDouble(distance));// 显示路程
            tv_calories.setText(formatDouble(calories)+"卡路里");// 显示卡路里
            tv_velocity.setText(formatDouble(velocity)+"m/s");// 显示速度

            tv_timer.setText(getFormatTime(timer));// 显示当前运行时间

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (aMap==null){
            aMap = mapView.getMap();
            setupMap();
        }


        if (thread == null) {

            thread = new Thread() {// 子线程用于监听当前步数的变化

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int temp = 0;
                    while (true) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.d("-->","end");
                        if (StepCounterService.FLAG) {
                            Log.d("-->","start");
                            Message msg = new Message();
                            if (temp != StepDetector.CURRENT_SETP) {
                                temp = StepDetector.CURRENT_SETP;
                            }
                            if (startTimer != System.currentTimeMillis()) {
                                timer = tempTime + System.currentTimeMillis()
                                        - startTimer;
                            }
                            handler.sendMessage(msg);// 通知主线程
                        }
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(thread);
        mlocationClient.stopLocation();
        Log.d("on stop","->>>");
    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.i("APP", "on resuame.");
        // 获取界面控件
        addView();

        // 初始化控件
        init();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 地图配置
     */
    private void setupMap(){
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }

    /**
     * 获取Activity相关控件
     */
    private void addView() {
        tv_show_step = (TextView) findViewById(R.id.step_count_label);

        tv_timer = (TextView) findViewById(R.id.time_label);

        tv_distance = (TextView) findViewById(R.id.diatance_label);
        tv_calories = (TextView) findViewById(R.id.kcal_label);
        tv_velocity = (TextView)findViewById(R.id.speed_label);

        btn_start = (Button) findViewById(R.id.startbutton);
        btn_stop = (Button) findViewById(R.id.stopbutton);
        btn_pause = (Button) findViewById(R.id.pauseBtn);

        btn_hideMap = (Button) findViewById(R.id.closemapBtn);
        btn_showMap = (Button) findViewById(R.id.showmapBtn);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.bglayout);
        layout.getBackground().setAlpha(100);

        Intent service = new Intent(this, StepCounterService.class);
        stopService(service);
        StepDetector.CURRENT_SETP = 0;
        tempTime = timer = 0;
        tv_timer.setText(getFormatTime(timer));      //如果关闭之后，格式化时间
        tv_show_step.setText("0");
        tv_distance.setText(formatDouble(0.0));
        tv_calories.setText(formatDouble(0.0) + "卡路里");
        tv_velocity.setText(formatDouble(0.0) + "m/s");


        SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm");
        currentTime = format.format(new Date(System.currentTimeMillis()));

        handler.removeCallbacks(thread);

    }

    /**
     * 初始化界面
     */
    private void init() {

        SharedPreferences sp = Setting_fragment.sharedPreferences;
        if (sp!=null){
            step_length = sp.getInt(Setting_fragment.HEIGHT_VALUE,150)*0.3;
            weight = sp.getInt(Setting_fragment.WEIGHT_VALUE,50);
        }else {
            step_length = 150*0.3;
            weight = 50;
        }
        countDistance();
        countStep();
        if ((timer += tempTime) != 0 && distance != 0.0) {  //tempTime记录运动的总时间，timer记录每次运动时间

            // 体重、距离
            // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036，换算一下
            calories = weight * distance * 0.001;

            velocity = distance * 1000 / timer;
        } else {
            calories = 0.0;
            velocity = 0.0;
        }

        tv_timer.setText(getFormatTime(timer + tempTime));

        tv_distance.setText(formatDouble(distance));
        tv_calories.setText(formatDouble(calories));
        tv_velocity.setText(formatDouble(velocity));

        tv_show_step.setText(total_step + "");


        if (StepCounterService.FLAG) {
            btn_stop.setText(getString(R.string.pause));
        } else if (StepDetector.CURRENT_SETP > 0) {
            btn_stop.setEnabled(true);
            btn_stop.setText(getString(R.string.cancel));
        }
    }


    /**
     * 定位成功回调
     * @param aMapLocation
     */
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                LatLng newLatLng = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                if (isFirstLatLng){
                    oldLatLng = newLatLng;
                    isFirstLatLng = false;
                }

                if (oldLatLng != newLatLng){
                    Log.d("AMap","update"+newLatLng.latitude+" "+newLatLng.longitude);
                    setupMap(oldLatLng,newLatLng);
                    oldLatLng = newLatLng;
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                if (!isFirstLatLng){
                    Toast.makeText(this,errText,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(1000);
            mLocationOption.setOnceLocation(false);
            //mLocationOption.setGpsFirst(true);

            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }



    /**
     * 计算行走的距离
     */
    private void countDistance() {
        if (StepDetector.CURRENT_SETP % 2 == 0) {
            distance = (StepDetector.CURRENT_SETP / 2) * 3 * step_length * 0.01;
        } else {
            distance = ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length * 0.01;
        }
    }

    /**
     * 实际步数
     */
    private void countStep() {
        if (StepDetector.CURRENT_SETP % 2 == 0) {
            total_step = StepDetector.CURRENT_SETP;
        } else {
            total_step = StepDetector.CURRENT_SETP +1;
        }

        total_step = StepDetector.CURRENT_SETP;
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
        return distanceStr.equals(getString(R.string.zero)) ? getString(R.string.double_zero)
                : distanceStr;
    }



    public void onClick(View view) {
        Intent service = new Intent(this, StepCounterService.class);
        switch (view.getId()) {
            case R.id.startbutton:
                startService(service);
                btn_start.setVisibility(View.INVISIBLE);
                btn_stop.setVisibility(View.INVISIBLE);
                btn_pause.setVisibility(View.VISIBLE);

                btn_start.setText(getString(R.string.restart));
                startTimer = System.currentTimeMillis();
                tempTime = timer;
                break;

            case R.id.stopbutton:
                stopService(service);
                btn_start.setVisibility(View.VISIBLE);

                if (distance>0){
                    showAlertDialog();
                }else {
                    RunActivity.this.finish();
                }
                break;
            case R.id.pauseBtn:
                stopService(service);
                btn_pause.setVisibility(View.INVISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                btn_stop.setVisibility(View.VISIBLE);
                break;
            case R.id.showmapBtn:
                mapView.setVisibility(View.VISIBLE);
                btn_hideMap.setVisibility(View.VISIBLE);
                break;
            case R.id.closemapBtn:
                mapView.setVisibility(View.INVISIBLE);
                btn_hideMap.setVisibility(View.INVISIBLE);
                break;
        }

    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RunActivity.this);
        builder.setTitle("是否保存记录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RunRecordManager manager = new RunRecordManager(getApplicationContext());
                Run run = new Run();
                run.setVelocity(velocity);
                run.setTimer(timer);
                run.setDistance(distance);
                run.setCalories(calories);
                run.setStepCount(total_step);
                run.setStartTime(currentTime);
                long i = manager.insertRecord(run);
                Log.d("db---->",i+"");
                RunActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RunActivity.this.finish();
            }
        });
        builder.show();
    }

    private void setupMap(LatLng oldData,LatLng newData){
        aMap.addPolyline(new PolylineOptions().
                add(oldData,newData).
                geodesic(true).color(Color.BLUE).width(4.0f));
    }
}
