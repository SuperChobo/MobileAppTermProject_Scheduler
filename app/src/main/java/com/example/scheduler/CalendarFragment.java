package com.example.scheduler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.scheduler.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mainContext;

    private String tableName = "";
    private EntityList list = new EntityList(EntityList.SCHEDULE_ENTITY);
    FragmentCalendarBinding binding;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Callander.
     */

    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        mainContext = container.getContext();

        binding.dataSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!binding.dataSwitch.isChecked()){
                    makeScheduleUI();
                }else{
                    makeDataUI();
                }
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!binding.dataSwitch.isChecked()){
            makeScheduleUI();
        }else{
            makeDataUI();
        }
    }

    public void setData(EntityList data){
        list = data;
    }
    public void setTableName(String name) { tableName = name; }

    Calendar nowDate = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    void makeScheduleUI(){
        binding.calendarListLayout.removeAllViews();

        Context ctx = this.getContext();
        {
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(200);

            Button dateButton = new Button(this.getContext());
            dateButton.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
            dateButton.setText(new SimpleDateFormat("yy년 MM월 dd일").format(nowDate.getTime()));

            dateButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dp = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            nowDate.set(i, i1, i2);
                            dateButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(nowDate.getTime()));
                            makeScheduleUI();
                        }
                    }, nowDate.get(Calendar.YEAR), nowDate.get(Calendar.MONTH), nowDate.get(Calendar.DATE));
                    dp.show();
                }
            });
            ll.addView(dateButton);

            Button addButton = new Button(this.getContext());
            addButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            addButton.setText("+");
            addButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowDate.set(Calendar.DATE, nowDate.get(Calendar.DATE) + 1);
                    makeScheduleUI();
                }
            });
            ll.addView(addButton);

            Button subButton = new Button(this.getContext());
            subButton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            subButton.setText("-");
            subButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowDate.set(Calendar.DATE, nowDate.get(Calendar.DATE) - 1);
                    makeScheduleUI();
                }
            });
            ll.addView(subButton);

            binding.calendarListLayout.addView(ll);
        }

        class Schedule{
            String name;
            Calendar time;
            Schedule(String name, Calendar time){
                this.name = name;
                this.time = time;
            }
        }

        List<Schedule> dataList = new ArrayList<Schedule>();
        for(Entity item : list.getList()){
            int calendarType = 0;
            int offset = 1;

            if(UsefulFunction.isDateEqual(item.getDate(), nowDate)){
                dataList.add(new Schedule(item.getName(), item.getDate()));
                continue;
            } else if(item.getDate().after(nowDate)){
                continue;
            }
            if(item.getPeriodType() == Entity.PERIOD_NONE){
                continue;
            }

            switch(item.getPeriodType()) {
                case Entity.PERIOD_DAY:
                    calendarType = Calendar.DATE;
                    break;
                case Entity.PERIOD_WEEK:
                    calendarType = Calendar.DATE;
                    offset *= 7;
                    break;
                case Entity.PERIOD_MONTH:
                    calendarType = Calendar.MONTH;
                    break;
                case Entity.PERIOD_YEAR:
                    calendarType = Calendar.YEAR;
                    break;
                default:
                    Log.i("test", "No Period Type");
                    return;
            }

            offset *= item.getPeriod();
            Calendar nextDate = Calendar.getInstance();
            nextDate.set(item.getDate().get(Calendar.YEAR),item.getDate().get(Calendar.MONTH),item.getDate().get(Calendar.DATE),0, 0, 0);
            nextDate.set(calendarType, nextDate.get(calendarType) + offset);

            if(item.getPeriod() == 0){
                Log.i("test", "Period 에 0이 옴");
                break;
            }

            while(nextDate.compareTo(nowDate) < 0)
            {
                if(UsefulFunction.isDateEqual(nowDate, nextDate)){
                    dataList.add(new Schedule(item.getName(), item.getDate()));
                    break;
                }

                Calendar newDate = Calendar.getInstance();
                newDate.set(nextDate.get(Calendar.YEAR),nextDate.get(Calendar.MONTH), nextDate.get(Calendar.DATE), 0, 0, 0);
                newDate.set(calendarType, nextDate.get(calendarType) + offset);
                nextDate = newDate;
            }
        }

        dataList.sort(new Comparator<Schedule>() {
            @Override
            public int compare(Schedule t1, Schedule t2) {
                if(t1.time.get(Calendar.HOUR_OF_DAY) > t2.time.get(Calendar.HOUR_OF_DAY)){
                    return 1;
                }else if(t1.time.get(Calendar.HOUR_OF_DAY) < t2.time.get(Calendar.HOUR_OF_DAY)){
                    return -1;
                }else if(t1.time.get(Calendar.MINUTE) > t2.time.get(Calendar.MINUTE)){
                    return 1;
                }
                else if(t1.time.get(Calendar.MINUTE) < t2.time.get(Calendar.MINUTE)){
                    return -1;
                }
                else return t1.name.compareTo(t2.name);
            }
        });

        for(Schedule item : dataList){
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(100);

            TextView timeText = new TextView(this.getContext());

            timeText.setText(new SimpleDateFormat("hh시 mm분").format(item.time.getTime()));
            timeText.setLayoutParams(new LinearLayout.LayoutParams(200, 100));

            ll.addView(timeText);

            TextView nameText = new TextView(this.getContext());
            nameText.setText(item.name);
            nameText.setLayoutParams(new LinearLayout.LayoutParams(200, 100));
            ll.addView(nameText);

            binding.calendarListLayout.addView(ll);
        }
    }

    void makeDataUI(){
        binding.calendarListLayout.removeAllViews();

        Calendar nowDate = Calendar.getInstance();
        nowDate.set(1900, 1, 1);
        boolean isFirst = true;
        for(Entity item : list.getList()){

            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(100);

            TextView periodText = new TextView(this.getContext());
            if(item.getPeriodType() == Entity.PERIOD_NONE){
                periodText.setText("[반복 없음]");
            }else {
                periodText.setText("[".concat(String.valueOf(item.getPeriod())).concat(item.getPeriodTypeString()).concat(" 마다]"));
            }
            periodText.setGravity(Gravity.LEFT);
            periodText.setLayoutParams(new LinearLayout.LayoutParams(200, 100));
            ll.addView(periodText);

            TextView timeText = new TextView(this.getContext());
            timeText.setText(item.getHourString());
            timeText.setLayoutParams(new LinearLayout.LayoutParams(250, 100));
            ll.addView(timeText);

            TextView nameText = new TextView(this.getContext());
            nameText.setText(item.getName());
            ll.addView(nameText);

            ll.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View view) {
                    makeDialogue(item);
                    return true;
                }
            });


            binding.calendarListLayout.addView(ll);
        }

        Button addBtn = new Button(this.getContext());
        addBtn.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        addBtn.setTextSize(10);
        addBtn.setText("+");
        addBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                makeDialogue(null);
            }
        });
        binding.calendarListLayout.addView(addBtn);
    }

    @SuppressLint("SimpleDateFormat")
    private void makeDialogue(Entity target){
        int height = 150;
        int width = 100;
        int INF = 9999;

        String noneStr = "반복 안함";
        String dayStr = "일";
        String weekStr = "주";
        String monthStr = "월";
        String yearStr = "년";
        Context ctx = this.getContext();

        Calendar dateValue;
        if(target != null){
            dateValue = target.getDate();
        }else {
            dateValue = Entity.getToday();
        }

        AlertDialog.Builder ad = new AlertDialog.Builder(this.getContext());
        if(target != null){
            ad.setTitle("수정하기");
        }else {
            ad.setTitle("추가하기");
        }
        LinearLayout ll = new LinearLayout(this.getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(100, 20, 100, 100);

        //
        // Name
        //
        LinearLayout nameLayout = new LinearLayout(this.getContext());
        nameLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView nameText = new TextView(this.getContext());
        nameText.setWidth(width);
        nameText.setText("이름 : ");
        nameLayout.addView(nameText);

        EditText nameInput = new EditText(this.getContext());
        nameInput.setWidth(INF);
        nameInput.setHint("스케줄을 입력하세요");
        nameLayout.addView(nameInput);
        if(target != null){
            nameInput.setText(target.getName());
        }
        ll.addView(nameLayout);

        LinearLayout periodLayout = new LinearLayout(this.getContext());
        periodLayout.setOrientation(LinearLayout.HORIZONTAL);
        periodLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView periodText = new TextView(this.getContext());
        periodText.setWidth(width);
        periodText.setText("주기 : ");
        periodLayout.addView(periodText);

        EditText periodInput = new EditText(this.getContext());
        periodInput.setWidth(width * 2);
        periodInput.setGravity(Gravity.CENTER);
        periodInput.setHint(String.valueOf(1));
        periodInput.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

        if (target != null) {
            periodInput.setText(String.valueOf(target.getPeriod()));
        }

        periodInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    String str = periodInput.getText().toString().trim();
                    int num = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    periodInput.setText("");
                }
                return false;
            }
        });
        periodLayout.addView(periodInput);

        Spinner periodSpinner = new Spinner(this.getContext());
        List<String> periodList = new ArrayList<String>();
        periodList.add(noneStr);
        periodList.add(dayStr);
        periodList.add(weekStr);
        periodList.add(monthStr);
        periodList.add(yearStr);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, periodList);
        periodSpinner.setAdapter(periodAdapter);
        if (target != null) {
            int selection = 0;
            switch (target.getPeriodType()) {
                case Entity.PERIOD_NONE:
                    selection = 0;
                    break;
                case Entity.PERIOD_DAY:
                    selection = 1;
                    break;
                case Entity.PERIOD_WEEK:
                    selection = 2;
                    break;
                case Entity.PERIOD_MONTH:
                    selection = 3;
                    break;
                case Entity.PERIOD_YEAR:
                    selection = 4;
                    break;

            }
            periodSpinner.setSelection(selection);
        }
        periodLayout.addView(periodSpinner);
        ll.addView(periodLayout);

        //
        // 기준 날짜
        //
        LinearLayout dateLayout = new LinearLayout(this.getContext());
        dateLayout.setOrientation(LinearLayout.HORIZONTAL);
        dateLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView dateText = new TextView(this.getContext());
        dateText.setWidth(width);
        dateText.setText("날짜 : ");
        dateLayout.addView(dateText);

        Button dateButton = new Button(this.getContext());
        dateButton.setWidth(INF);
        dateButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateValue.getTime()));

        dateButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateValue.set(i, i1, i2);
                        dateButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateValue.getTime()));
                    }
                }, dateValue.get(Calendar.YEAR), dateValue.get(Calendar.MONTH), dateValue.get(Calendar.DATE));
                dp.show();
            }
        });
        dateLayout.addView(dateButton);
        ll.addView(dateLayout);

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (noneStr.equals(periodSpinner.getItemAtPosition(i).toString())) {
                    periodLayout.removeView(periodInput);
                } else {
                    if (periodLayout.indexOfChild(periodInput) < 0) {
                        periodLayout.addView(periodInput, 1);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //
        // Time Picker
        //
        LinearLayout timeLayout = new LinearLayout(this.getContext());
        timeLayout.setOrientation(LinearLayout.HORIZONTAL);
        timeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView timeText = new TextView(this.getContext());
        timeText.setWidth(width);
        timeText.setText("시간 : ");
        timeLayout.addView(timeText);

        Button timeButton = new Button(this.getContext());
        timeButton.setWidth(INF);
        timeButton.setText(new SimpleDateFormat("HH시 mm분").format(dateValue.getTime()));

        timeButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                TimePickerDialog tp = new TimePickerDialog(ctx, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        dateValue.set(Calendar.HOUR_OF_DAY, i);
                        dateValue.set(Calendar.MINUTE, i1);
                        timeButton.setText(new SimpleDateFormat("HH시 mm분").format(dateValue.getTime()));
                    }
                }, dateValue.get(Calendar.HOUR_OF_DAY), dateValue.get(Calendar.MINUTE), true);
                tp.show();
            }
        });
        timeLayout.addView(timeButton);
        ll.addView(timeLayout);



        ad.setPositiveButton("설정", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i){
                String name;
                int periodType;
                int period;
                if(nameInput.getText().toString().equals("")){
                    name = "새로운 항목";
                }else{
                    name = nameInput.getText().toString();
                }


                String periodTypeStr = periodSpinner.getSelectedItem().toString();
                if (periodTypeStr.equals(dayStr)) {
                    periodType = Entity.PERIOD_DAY;
                } else if (periodTypeStr.equals(weekStr)) {
                    periodType = Entity.PERIOD_WEEK;
                } else if (periodTypeStr.equals(monthStr)) {
                    periodType = Entity.PERIOD_MONTH;
                } else if (periodTypeStr.equals(yearStr)) {
                    periodType = Entity.PERIOD_YEAR;
                } else if (periodTypeStr.equals(noneStr)) {
                    periodType = Entity.PERIOD_NONE;
                } else {
                    Log.i("test", "Period error");
                    return;
                }

                try {
                    if (periodInput.getText().toString().equals("")) {
                        period = 1;
                    } else {
                        period = Integer.parseInt(periodInput.getText().toString().trim());
                    }
                } catch(NumberFormatException e){
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    periodInput.setText("");
                    //ad.dismiss();
                    return;
                }


                if(nameInput.getText().toString().equals("")){
                    if(target == null) {
                        list.add(new Entity("새로운 스케줄", dateValue));
                    }else{
                        Toast.makeText(mainContext, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(target == null) {
                        list.add(new Entity(nameInput.getText().toString(), dateValue));
                    }else{
                        target.setName(nameInput.getText().toString());
                        target.setDate(dateValue);
                    }
                }
                makeDataUI();
            }
        });

        if(target != null) {
            ad.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    list.remove(target);
                    makeDataUI();
                }
            });
        }

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        // 마지막 레이아웃 넣어주기
        ad.setView(ll);
        ad.show();
    }
}