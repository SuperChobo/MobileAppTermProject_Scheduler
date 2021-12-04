package com.example.scheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.scheduler.databinding.FragmentNormalChecklistBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalChecklist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalChecklist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String tableName = "";
    private Context mainContext;

    private EntityList list;
    FragmentNormalChecklistBinding binding;

    public NormalChecklist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NormalChecklist.
     */
    // TODO: Rename and change types and number of parameters
    public static NormalChecklist newInstance(String param1, String param2) {
        NormalChecklist fragment = new NormalChecklist();
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
        binding = FragmentNormalChecklistBinding.inflate(inflater, container, false);
        mainContext = container.getContext();
        makeUI();
        return binding.getRoot();
    }

    public void setData(EntityList data) {
        list = data;
    }

    public void setTableName(String name) {
        tableName = name;
    }

    private void makeUI() {
        binding.normalListLayout.removeAllViews();

        for (Entity item : list.getList()) {
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            ll.setMinimumHeight(100);
            String name = item.getName();

            TextView text = new TextView(this.getContext());
            text.setText(name);
            text.setWidth(300);
            text.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    makeDialogue(item);
                    return true;
                }
            });

            LinearLayout.MarginLayoutParams margin = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            margin.setMargins(0, 0, 100, 0);
            text.setLayoutParams(margin);

            ll.addView(text);

            switch (item.getType()) {
                case Entity.TYPE_CHECK:
                    CheckBox cb = new CheckBox(this.getContext());
                    cb.setChecked(item.getValueBool());
                    cb.setOnClickListener(new CheckBox.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item.valueNotBoolean();
                        }
                    });
                    ll.addView(cb);
                    break;

                case Entity.TYPE_NUM:
                    /*
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout rl = new RelativeLayout(this.getContext());
                    rl.setLayoutParams(params);
                    ProgressBar pb = new ProgressBar(this.getContext());
                    pb.setProgressDrawable();
                    pb.setLayoutParams(new RelativeLayout.LayoutParams(300, RelativeLayout.LayoutParams.MATCH_PARENT));
                    pb.setProgress(item.getValue() * 100 / item.getGoal());
                    ll.addView(pb);
                    */
                {
                    TextView numText = new TextView(this.getContext());
                    numText.setText(String.valueOf(item.getValue()) + " / " + String.valueOf(item.getGoal()));
                    ll.addView(numText);

                    LinearLayout buttonLayout = new LinearLayout(this.getContext());

                    buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                    Button adjustButton = new Button(this.getContext());
                    adjustButton.setText("수정");
                    adjustButton.setLayoutParams(new LinearLayout.LayoutParams(160, 90));
                    adjustButton.setTextSize(10);
                    adjustButton.setGravity(Gravity.CENTER);
                    buttonLayout.addView(adjustButton);
                    adjustButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeNumDialogue(item);
                            makeUI();
                        }
                    });
                    ll.addView(buttonLayout);
                    break;
                }
                case Entity.TYPE_COUNT: {
                    TextView countText = new TextView(this.getContext());
                    countText.setText(String.valueOf(item.getValue()) + " / " + String.valueOf(item.getGoal()));
                    ll.addView(countText);

                    LinearLayout buttonLayout = new LinearLayout(this.getContext());

                    buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                    Button addButton = new Button(this.getContext());
                    addButton.setText("+");
                    addButton.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                    addButton.setTextSize(10);
                    addButton.setGravity(Gravity.CENTER);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item.setValue(item.getValue() + 1);
                            makeUI();
                        }
                    });
                    buttonLayout.addView(addButton);

                    Button subButton = new Button(this.getContext());
                    subButton.setText("-");
                    subButton.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                    subButton.setTextSize(12);
                    subButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item.setValue(item.getValue() - 1);
                            makeUI();
                        }
                    });
                    buttonLayout.addView(subButton);
                    ll.addView(buttonLayout);
                    break;
                }
            }

            binding.normalListLayout.addView(ll);
        }

        Button addBtn = new Button(this.getContext());
        addBtn.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        addBtn.setTextSize(10);
        addBtn.setText("+");
        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialogue(null);
            }
        });
        binding.normalListLayout.addView(addBtn);
    }

    private void setSize(View view, int width, int height) {
        view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    private void makeDialogue(Entity target) {
        int height = 150;
        int width = 200;
        int INF = 9999;
        String checkStr = "체크리스트";
        String countStr = "카운트";
        String numStr = "수치";

        String noneStr = "반복 안함";
        String dayStr = "일";
        String weekStr = "주";
        String monthStr = "월";
        String yearStr = "년";

        Calendar dateValue;
        if (target != null) {
            dateValue = target.getDate();
        } else {
            dateValue = Entity.getToday();
        }


        AlertDialog.Builder ad = new AlertDialog.Builder(this.getContext());

        if (target != null) {
            ad.setTitle("수정하기");
        } else {
            ad.setTitle("추가하기");
        }
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

        if (target != null) {
            nameInput.setText(target.getName());
        }

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
        periodInput.setWidth(width);
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
        dateText.setText("기한 : ");
        dateLayout.addView(dateText);

        Context ctx = this.getContext();

        Button dateButton = new Button(this.getContext());
        dateButton.setWidth(INF);
        dateButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateValue.getTime()));

        dateButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
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
                }else {
                    if(periodLayout.indexOfChild(periodInput) < 0) {
                        periodLayout.addView(periodInput, 1);
                        periodInput.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //
        //  타입
        //
        LinearLayout typeLayout = new LinearLayout(this.getContext());
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView typeText = new TextView(this.getContext());
        typeText.setWidth(width);
        typeText.setText("타입 : ");
        typeLayout.addView(typeText);

        Spinner typeSpinner = new Spinner(this.getContext());
        List<String> typeList = new ArrayList<String>();
        typeList.add(checkStr);
        typeList.add(countStr);
        typeList.add(numStr);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, typeList);
        typeSpinner.setAdapter(typeAdapter);
        if (target != null) {
            int selection = 0;
            switch (target.getType()) {
                case Entity.TYPE_CHECK:
                    selection = 0;
                    break;
                case Entity.TYPE_COUNT:
                    selection = 1;
                    break;
                case Entity.TYPE_NUM:
                    selection = 2;
                    break;
            }
            typeSpinner.setSelection(selection);
        }

        typeLayout.addView(typeSpinner);

        ll.addView(typeLayout);

        //
        //  목표
        //
        LinearLayout goalLayout = new LinearLayout(this.getContext());
        goalLayout.setOrientation(LinearLayout.HORIZONTAL);
        goalLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView goalText = new TextView(this.getContext());
        goalText.setWidth(width);
        goalText.setText("목표 : ");
        goalLayout.addView(goalText);

        EditText goalInput = new EditText(this.getContext());
        goalInput.setWidth(INF);
        goalInput.setHint("목표 수치를 입력하시오.");
        goalInput.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

        if (target != null) {
            goalInput.setText(String.valueOf(target.getGoal()));
        }

        goalInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    String str = goalInput.getText().toString().trim();
                    int num = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    goalInput.setText("");
                }
                return false;
            }
        });
        goalLayout.addView(goalInput);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ll.removeView(goalLayout);
                if (!checkStr.equals(typeSpinner.getItemAtPosition(i).toString())) {
                    ll.addView(goalLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ad.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                int type = -1;
                String name;
                int periodType;
                int period;
                int goal = 0;

                String selectedType = typeSpinner.getSelectedItem().toString();
                if (selectedType.equals(checkStr)) {
                    type = Entity.TYPE_CHECK;
                } else if (selectedType.equals(countStr)) {
                    type = Entity.TYPE_COUNT;
                } else if (selectedType.equals(numStr)) {
                    type = Entity.TYPE_NUM;
                } else {
                    Log.i("test", "Type error");
                    return;
                }

                if (nameInput.getText().toString().equals("")) {
                    name = "새로운 항목";
                } else {
                    name = nameInput.getText().toString();
                }

                try {
                    if (type == Entity.TYPE_CHECK | goalInput.getText().toString().equals("")) {
                        goal = Entity.VALUE_TRUE;
                    } else {
                        goal = Integer.parseInt(goalInput.getText().toString().trim());
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    goalInput.setText("");
                    return;
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
                } catch (NumberFormatException e) {
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    periodInput.setText("");
                    //ad.dismiss();
                    return;
                }

                if (target == null) {
                    list.add(new Entity(type, name, 0, goal, periodType, period, dateValue));
                } else {
                    target.setType(type);
                    target.setName(name);
                    target.setGoal(goal);
                    target.setPeriodType(periodType);
                    target.setPeriod(period);
                    target.setDate(dateValue);
                }
                makeUI();
            }
        });

        if (target != null) {
            ad.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    list.remove(target);
                    makeUI();
                }
            });
        }

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.setView(ll);
        ad.show();
    }

    private void makeNumDialogue(Entity target) {
        int height = 150;
        int width = 100;
        int INF = 9999;

        AlertDialog.Builder ad = new AlertDialog.Builder(this.getContext());
        ad.setTitle("값 수정");

        LinearLayout ll = new LinearLayout(this.getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(100, 20, 100, 100);

        LinearLayout numLayout = new LinearLayout(this.getContext());
        numLayout.setOrientation(LinearLayout.HORIZONTAL);
        numLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        TextView numText = new TextView(this.getContext());
        numText.setWidth(width);
        numText.setText("값 : ");
        numLayout.addView(numText);

        EditText numInput = new EditText(this.getContext());
        numInput.setWidth(INF);
        numInput.setHint("원하는 수치를 입력하시오.");
        numInput.setText(String.valueOf(target.getValue()));
        numInput.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        numLayout.addView(numInput);

        ll.addView(numLayout);

        ad.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                int num = 0;
                try {
                    num = Integer.parseInt(numInput.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(mainContext, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
                    numInput.setText("");
                }
                target.setValue(num);
                makeUI();
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.setView(ll);
        ad.show();
    }
}