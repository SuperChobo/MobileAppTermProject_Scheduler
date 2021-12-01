package com.example.scheduler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.scheduler.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private BlankFragment blankFragment;
    private NormalChecklist normalChecklist;
    private CalendarFragment calendarFragment;
    private DoubleFragment doubleFragment;
    private List<EntityList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Advanced Scheduler");

        DBInit();

        setTabView();
        setFragment(0);
        binding.menuTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setFragment(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void DBInit(){
        list = new ArrayList<EntityList>();

        calendarFragment = new CalendarFragment();
        normalChecklist = new NormalChecklist();
        blankFragment = new BlankFragment();
        doubleFragment = new DoubleFragment();

        list.add(new EntityList("스케줄", EntityList.SCHEDULE_ENTITY));
        list.add(new EntityList("체크", EntityList.CHECK_ENTITY));

        List<String> strList = new ArrayList<String>();
        strList.add("국어");
        strList.add("영어");
        strList.add("수학");
        strList.add("사회");
        strList.add("과학");
        list.add(new EntityList("공부", EntityList.DOUBLE_ENTITY, strList));

        //ScheduleEntity(String name, int year, int month, int day, int hour, int minute)
        list.get(0).add(new Entity(
                "수업", 2021, 11, 24, 9, 40));
        list.get(0).add(new Entity(
                "게임", 2020, 1, 5, 9, 40));

        //CheckEntity(int type, String name, int value, int goal, int periodType, int period)
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "운동", Entity.VALUE_FALSE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_NUM, "게임", 5, 10, Entity.PERIOD_WEEK, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "공부", Entity.VALUE_TRUE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "과제", Entity.VALUE_FALSE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_NUM, "돈 모으기", 1000, 10000, Entity.PERIOD_WEEK, 1));


        //Entity(String name, String valueList, int periodType, int period){
        list.get(2).add(new Entity(
                "예습", "OXOXO", Entity.PERIOD_DAY, 1));
        list.get(2).add(new Entity(
                "강의", "XXOOO", Entity.PERIOD_DAY, 1));
        list.get(2).add(new Entity(
                "노트필기", "OXOXO", Entity.PERIOD_WEEK, 1));
        list.get(2).add(new Entity(
                "문제필이", "XOXOX", Entity.PERIOD_WEEK, 1));
        list.get(2).add(new Entity(
                "오답노트", "OXOXO", Entity.PERIOD_WEEK, 1));
    }

    private void setTabView(){
        TabLayout tabLayout = binding.menuTab;
        for(EntityList item : list){
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(item.getName());
            tabLayout.addTab(tab);
        }
    }

    private void setFragment(int position){
        Fragment selected = null;
        switch(position){
            case 0:
                selected = calendarFragment;
                calendarFragment.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                calendarFragment.setData(list.get(position));
                break;
            case 1:
                selected = normalChecklist;
                normalChecklist.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                normalChecklist.setData(list.get(position));
                break;
            case 2:
                selected = doubleFragment;
                doubleFragment.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                doubleFragment.setData(list.get(position));
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, selected).commit();
    }
}
