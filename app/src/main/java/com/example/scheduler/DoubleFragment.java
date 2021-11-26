package com.example.scheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.scheduler.databinding.FragmentDoubleBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoubleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoubleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mainContext;
    private String tableName;
    private EntityList list = new EntityList(EntityList.SCHEDULE_ENTITY);

    FragmentDoubleBinding binding;

    public DoubleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoubleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoubleFragment newInstance(String param1, String param2) {
        DoubleFragment fragment = new DoubleFragment();
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
    public void setData(EntityList data){ list = data; }
    public void setTableName(String name) { tableName = name; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoubleBinding.inflate(inflater, container, false);
        mainContext = container.getContext();
        makeUI();
        return binding.getRoot();
    }

    private void makeUI(){
        int width = 200;
        int nameWidth = 300;
        int height = 100;
        int widthAdjust = 30;

        binding.doubleTableName.setText(tableName);
        binding.doubleLayout.removeAllViews();

        LinearLayout column1Layout = new LinearLayout(this.getContext());
        column1Layout.setOrientation(LinearLayout.HORIZONTAL);
        column1Layout.setMinimumHeight(80);

        TextView emptyText = new TextView(this.getContext());
        emptyText.setWidth(nameWidth + widthAdjust);
        column1Layout.addView(emptyText);

        LinearLayout column2Layout = new LinearLayout(this.getContext());
        column2Layout.setOrientation(LinearLayout.HORIZONTAL);
        column2Layout.setMinimumHeight(80);

        emptyText = new TextView(this.getContext());
        emptyText.setWidth(nameWidth - (width / 2) + widthAdjust);
        column2Layout.addView(emptyText);

        int index = 0;
        for(String item : list.getColumns()){
            TextView nameText = new TextView(this.getContext());
            nameText.setText(item);
            nameText.setWidth(width);
            nameText.setMaxWidth(width);
            nameText.setSingleLine(true);
            nameText.setGravity(Gravity.CENTER_HORIZONTAL);
            nameText.setEllipsize(TextUtils.TruncateAt.END);

            if(index % 2 == 1){
                column1Layout.addView(nameText);
            }else{
                column2Layout.addView(nameText);
            }

            index++;
        }
        binding.doubleLayout.addView(column1Layout);
        binding.doubleLayout.addView(column2Layout);

        for(Entity item : list.getList()){
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(height);

            TextView nameText = new TextView(this.getContext());
            nameText.setText(item.getName());
            nameText.setWidth(nameWidth);

            ll.addView(nameText);

            for(int i = 0; i < item.getValueList().length(); i++){
                CheckBox cb = new CheckBox(this.getContext());
                Integer ii = i;
                cb.setWidth(width / 2);
                switch(item.getValueList().charAt(i)){
                    case 'O':
                        cb.setChecked(true);
                        break;
                    case 'X':
                        cb.setChecked(false);
                        break;
                }
                cb.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(cb.isChecked()) {
                            item.setValueOfList('O', ii);
                        }else{
                            item.setValueOfList('X', ii);
                        }
                        Log.i("test", item.getValueList());
                    }
                });
                ll.addView(cb);
            }
            binding.doubleLayout.addView(ll);
        }

        Button addBtn = new Button(this.getContext());
        addBtn.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        addBtn.setTextSize(10);
        addBtn.setText("+");
        addBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                makeDialogue();
            }
        });
        binding.doubleLayout.addView(addBtn);
    }

    private void makeDialogue(){
        int height = 150;
        int width = 100;
        int INF = 9999;

        String dayStr = "일";
        String weekStr = "주";
        String monthStr = "월";
        String yearStr = "년";

        AlertDialog.Builder ad = new AlertDialog.Builder(this.getContext());

        ad.setTitle("추가하기");
        LinearLayout ll = new LinearLayout(this.getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(100, 20, 100, 100);

        //
        //  이름
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
        nameInput.setHint("이름을 입력하시오.");
        nameLayout.addView(nameInput);

        ll.addView(nameLayout);

        //
        // 주기
        //
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
        periodInput.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try{
                    String str = periodInput.getText().toString().trim();
                    int num = Integer.parseInt(str);
                } catch(NumberFormatException e){
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    periodInput.setText("");
                }
                return false;
            }
        });
        periodLayout.addView(periodInput);

        Spinner periodSpinner = new Spinner(this.getContext());
        List<String> periodList = new ArrayList<String>();
        periodList.add(dayStr);
        periodList.add(weekStr);
        periodList.add(monthStr);
        periodList.add(yearStr);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, periodList);
        periodSpinner.setAdapter(periodAdapter);
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
        dateText.setText("주기 : ");
        dateLayout.addView(dateText);

        Calendar dateValue = Entity.getToday();
        Context ctx = this.getContext();

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

        ad.setPositiveButton("설정", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int index){
                String name;
                int periodType;
                int period;

                if(nameInput.getText().toString().equals("")){
                    name = "새로운 항목";
                }else{
                    name = nameInput.getText().toString();
                }

                String periodTypeStr = periodSpinner.getSelectedItem().toString();
                if(periodTypeStr.equals(dayStr)){
                    periodType = Entity.PERIOD_DAY;
                }else if(periodTypeStr.equals(weekStr)){
                    periodType = Entity.PERIOD_WEEK;
                }else if(periodTypeStr.equals(monthStr)) {
                    periodType = Entity.PERIOD_MONTH;
                }else if(periodTypeStr.equals(yearStr)){
                    periodType = Entity.PERIOD_YEAR;
                }else{
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

                String values = "";
                for(int i = 0; i < list.getColumns().size(); i++){
                    values = values + "X";
                }

                list.add(new Entity(name, values, periodType, period));
                makeUI();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.setView(ll);
        ad.show();
    }

}