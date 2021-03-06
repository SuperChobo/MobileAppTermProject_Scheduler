package com.example.scheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = new DBHelper(MainActivity.this, "Scheduler.db", null, 3);
        setTitle("Advanced Scheduler");

        //getWindow().setNavigationBarColor(Color.GRAY);
        getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.teal_700, null));
        DBInit();

        setTabView();
        setFragment(0);
        binding.menuTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position >= list.size()){
                    position = -1;
                }
                makeDialog(position);
            }
        });
    }

    private void DBInit(){
        list = db.getTable();
        /*
        list.add(new EntityList("?????????", EntityList.SCHEDULE_ENTITY));
        list.add(new EntityList("??????", EntityList.NORMAL_ENTITY));

        List<String> strList = new ArrayList<String>();
        strList.add("??????");
        strList.add("??????");
        strList.add("??????");
        strList.add("??????");
        strList.add("??????");
        list.add(new EntityList("??????", EntityList.DOUBLE_ENTITY, strList));

        //ScheduleEntity(String name, int year, int month, int day, int hour, int minute)
        list.get(0).add(new Entity(
                "??????", 2021, 11, 24, 9, 40, Entity.PERIOD_WEEK, 1));
        list.get(0).add(new Entity(
                "??????", 2021, 11, 21, 9, 40, Entity.PERIOD_DAY, 1));
        list.get(0).add(new Entity(
                "??????", 2021, 1, 5, 10, 40, Entity.PERIOD_DAY, 2));
        list.get(0).add(new Entity(
                "??????", 2021, 12, 10, 11, 40, Entity.PERIOD_NONE, 1));
        list.get(0).add(new Entity(
                "????????????", 2021, 12, 5, 12, 35, Entity.PERIOD_WEEK, 1));
        list.get(0).add(new Entity(
                "????????????", 2021, 12, 6, 13, 40, Entity.PERIOD_WEEK, 1));

        //CheckEntity(int type, String name, int value, int goal, int periodType, int period)
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "??????", Entity.VALUE_FALSE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_COUNT, "??????", 5, 10, Entity.PERIOD_WEEK, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "??????", Entity.VALUE_TRUE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_CHECK, "??????", Entity.VALUE_FALSE, Entity.VALUE_TRUE, Entity.PERIOD_DAY, 1));
        list.get(1).add(new Entity(
                Entity.TYPE_NUM, "??? ?????????", 1000, 10000, Entity.PERIOD_WEEK, 1));


        //Entity(String name, String valueList, int periodType, int period){
        list.get(2).add(new Entity(
                "??????", "OXOXO", Entity.PERIOD_DAY, 1));
        list.get(2).add(new Entity(
                "??????", "XXOOO", Entity.PERIOD_DAY, 1));
        list.get(2).add(new Entity(
                "????????????", "OXOXO", Entity.PERIOD_WEEK, 1));
        list.get(2).add(new Entity(
                "????????????", "XOXOX", Entity.PERIOD_WEEK, 1));
        list.get(2).add(new Entity(
                "????????????", "OXOXO", Entity.PERIOD_WEEK, 1));

        */
    }

    public void saveData(View view){
        db.setTable(list);
    }

    private void setTabView(){
        TabLayout tabLayout = binding.menuTab;

        for(EntityList item : list){
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(item.getName());
            tabLayout.addTab(tab);
        }

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("+");
        tabLayout.addTab(tab);
    }

    private void setFragment(int position) {
        Fragment selected = null;

        if (position < list.size()) {
            switch (list.get(position).getType()) {
                case EntityList.SCHEDULE_ENTITY:
                    calendarFragment = new CalendarFragment();
                    selected = calendarFragment;
                    calendarFragment.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                    calendarFragment.setData(list.get(position));
                    break;
                case EntityList.NORMAL_ENTITY:
                    normalChecklist = new NormalChecklist();
                    selected = normalChecklist;
                    normalChecklist.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                    normalChecklist.setData(list.get(position));
                    UsefulFunction.refreshEntityList(list.get(position));
                    break;
                case EntityList.DOUBLE_ENTITY:
                    doubleFragment = new DoubleFragment();
                    selected = doubleFragment;
                    doubleFragment.setTableName(binding.menuTab.getTabAt(position).getText().toString());
                    doubleFragment.setData(list.get(position));
                    UsefulFunction.refreshEntityList(list.get(position));
                    break;
            }
            binding.titleText.setText(list.get(position).getName());
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, selected).commit();
        } else {
            makeDialog(-1);
        }

    }

    private void makeDialog(int target){
        final String scheduleStr = "?????????";
        final String normalStr = "??????";
        final String doubleStr = "2??????";

        int height = 150;
        int width = 100;
        int INF = 9999;

        AlertDialog.Builder ad = new AlertDialog.Builder(binding.getRoot().getContext());
        if(target != -1) {
            ad.setTitle("????????? ??????");
        } else {
            ad.setTitle("????????? ??????");
        }

        LinearLayout ll = new LinearLayout(binding.getRoot().getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(100, 20, 100, 100);

        LinearLayout nameLayout = new LinearLayout(binding.getRoot().getContext());
        nameLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView nameText = new TextView(binding.getRoot().getContext());
        nameText.setWidth(width);
        nameText.setText("?????? : ");
        nameLayout.addView(nameText);

        EditText nameInput = new EditText(binding.getRoot().getContext());
        nameInput.setWidth(INF);
        nameInput.setHint("????????? ????????? ???????????????.");


        if(target != -1){
            nameInput.setText(list.get(target).getName());
        }
        nameLayout.addView(nameInput);
        ll.addView(nameLayout);


        LinearLayout typeLayout = new LinearLayout(binding.getRoot().getContext());
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView typeText = new TextView(binding.getRoot().getContext());
        typeText.setWidth(width);
        typeText.setText("?????? : ");
        typeLayout.addView(typeText);

        Spinner typeSpinner = new Spinner(binding.getRoot().getContext());
        List<String> typeList = new ArrayList<String>();
        typeList.add(normalStr);
        typeList.add(scheduleStr);
        typeList.add(doubleStr);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(binding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, typeList);
        typeSpinner.setAdapter(typeAdapter);

        if(target == -1) {
            typeLayout.addView(typeSpinner);
            ll.addView(typeLayout);
        }

        ad.setPositiveButton("??????", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int index){
                final String name;
                int type = 0;

                if(nameInput.getText().toString().equals("")){
                    name = "????????? ?????????";
                }else{
                    name = nameInput.getText().toString();
                }

                if(target == -1) {
                    String typeStr = typeSpinner.getSelectedItem().toString();
                    switch (typeStr) {
                        case normalStr:
                            type = EntityList.NORMAL_ENTITY;
                            break;
                        case scheduleStr:
                            type = EntityList.SCHEDULE_ENTITY;
                            break;
                        case doubleStr:
                            type = EntityList.DOUBLE_ENTITY;
                            break;
                    }
                }

                if(target != -1){
                    list.get(target).setName(name);
                    binding.menuTab.getTabAt(target).setText(name);
                } else {
                    list.add(new EntityList(name, type));
                    TabLayout tabLayout = binding.menuTab;
                    TabLayout.Tab tab = tabLayout.newTab();
                    tab.setText(name);
                    tabLayout.addTab(tab, list.size() - 1);
                    tabLayout.selectTab(tab);
                    setFragment(list.size() - 1);
                }
            }
        });

        ad.setNegativeButton("??????", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        if(target != -1) {
            ad.setNeutralButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    list.remove(target);
                    binding.menuTab.removeTabAt(target);
                    if(list.size() == 0){
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new BlankFragment()).commit();
                    }
                }
            });
        }

        ad.setView(ll);
        ad.show();
    }
}
