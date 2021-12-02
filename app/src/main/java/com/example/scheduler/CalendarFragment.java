package com.example.scheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.scheduler.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;

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
        makeUI();
        return binding.getRoot();
    }

    public void setData(EntityList data){
        list = data;
    }
    public void setTableName(String name) { tableName = name; }

    void makeUI(){
        binding.calendarTableName.setText(tableName);
        binding.calendarListLayout.removeAllViews();

        list.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity entity, Entity t1) {
                if(entity.getDate().after(t1.getDate()))
                    return 1;
                else if(entity.getDate().before(t1.getDate()))
                    return -1;
                return 0;
            }
        });

        for(Entity item : list.getList()){
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(100);

            TextView dateText = new TextView(this.getContext());
            dateText.setText(item.getDateString());
            dateText.setWidth(500);
            dateText.setOnLongClickListener(new View.OnLongClickListener(){
               public boolean onLongClick(View view) {
                   makeDialogue(item);
                   return true;
               }
            });

            ll.addView(dateText);

            TextView nameText = new TextView(this.getContext());
            nameText.setText(item.getName());
            ll.addView(nameText);

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

    private void makeDialogue(Entity target){
        int height = 150;
        int width = 100;
        int INF = 9999;

        Context ctx = this.getContext();

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

        //
        // Date Picker
        //
        Calendar dateValue;
        if(target == null) {
            dateValue = Entity.getToday();
        }else{
            dateValue = target.getDate();
        }
        Log.i("test", new SimpleDateFormat("yyyy-MM-dd").format(dateValue.getTime()));

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
                DatePickerDialog dp = new  DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener(){
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
                if(nameInput.getText().toString().equals("")){
                    if(target == null) {
                        list.add(new Entity("새로운 스케줄", dateValue));
                    }else{
                        Toast.makeText(mainContext, "이름을 입력하세요.", Toast.LENGTH_SHORT);
                    }
                }else{
                    if(target == null) {
                        list.add(new Entity(nameInput.getText().toString(), dateValue));
                    }else{
                        target.setName(nameInput.getText().toString());
                        target.setDate(dateValue);
                    }
                }
                makeUI();
            }
        });

        if(target != null) {
            ad.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    list.remove(target);
                    makeUI();
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