package com.example.caixindong.big_pro.Activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.caixindong.big_pro.Fragment.Module1.Home_fragment;
import com.example.caixindong.big_pro.Fragment.Module2.Record_fragment;
import com.example.caixindong.big_pro.Fragment.Module3.Setting_fragment;
import com.example.caixindong.big_pro.R;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"首页", "记录", "设置"};
    private ArrayAdapter arrayAdapter;
    private int currentFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews(); //获取控件
        toolbar.setTitle("Running Life");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new Home_fragment();
        ft.replace(R.id.fragment_layout,fragment);
        ft.commit();

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentFragmentIndex != position){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = null;

                    switch (position){
                        case 0:
                            fragment = new Home_fragment();
                            toolbar.setTitle("Running Life");
                            break;
                        case 1:
                            fragment = new Record_fragment();
                            toolbar.setTitle("Record");
                            break;
                        case 2:
                            fragment = new Setting_fragment();
                            toolbar.setTitle("Setting");
                            break;

                    }
                    ft.replace(R.id.fragment_layout, fragment);
                    ft.commit();
                    currentFragmentIndex = position;
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }
}
