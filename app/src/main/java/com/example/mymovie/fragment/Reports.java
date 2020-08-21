package com.example.mymovie.fragment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mymovie.R;
import com.example.mymovie.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reports extends Fragment {

    private Button submit, select;
    private TextView startDate, endDate;
    private Spinner yearSpinner;
    private String start = "", end = "", id = "", year = "";
    private NetworkConnection networkConnection = new NetworkConnection();
    private PieChart pieChart;
    private BarChart barChart;
    final UserPieChart userPieChart = new UserPieChart();
    final UserBarChart userBarChart = new UserBarChart();


    public Reports(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.report, container, false);
        submit = view.findViewById(R.id.submit_report);
        startDate = view.findViewById(R.id.start_date_report);
        endDate = view.findViewById(R.id.end_date_report);
        pieChart = view.findViewById(R.id.pie_chart_report);
        yearSpinner = view.findViewById(R.id.yearSpinner_report);
        select = view.findViewById(R.id.select_report);
        barChart = view.findViewById(R.id.bar_chart_report);

        Bundle bundle = getArguments();
        id = bundle.getString("id");

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = startDate.getText().toString();
                end = endDate.getText().toString();
                Date d1 = null, d2 = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    d1 = sdf.parse(start);
                    d2 = sdf.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(start)){
                    Toast.makeText(getContext(), "Please select Start Date.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(end)){
                    Toast.makeText(getContext(), "Please select End Date.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (d1.getTime() > d2.getTime()){
                    Toast.makeText(getContext(), "Please select Start date earlier then End Date.", Toast.LENGTH_LONG).show();
                    return;
                }
                userPieChart.execute(id, start, end);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = yearSpinner.getSelectedItem().toString();
                userBarChart.execute(id, year);
            }
        });



        return view;
    }


    private class UserPieChart extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            String id = params[0];
            String start = params[1];
            String end = params[2];
            return networkConnection.getData(id, start, end);
        }
        @Override
        protected void onPostExecute(String returnMessage){
            int total = 0, temp = 0, i;
            String entity = "";
            float s = 0;
            int[] color = new int[] {R.color.akak, R.color.ayame, R.color.sabi, R.color.sakura, R.color.toki};
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = new JSONArray(returnMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (i = 0; i < jsonArray.length(); i++ ){
                try {
                    temp = Integer.parseInt(jsonArray.getJSONObject(i).get("moviewatched").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                total = total + temp;
            }
            List<PieEntry> entries = new ArrayList<>();
            List<Integer> colors = new ArrayList<>();
            pieChart.setTouchEnabled(false);
            for (i = 0; i < jsonArray.length(); i++){
                try {
                    entity = jsonArray.getJSONObject(i).get("Cpostcode").toString();
                    temp = Integer.parseInt(jsonArray.getJSONObject(i).get("moviewatched").toString());
                    s = (float)temp/total;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                entries.add(new PieEntry(s, entity));
                colors.add(getResources().getColor(color[i]));
            }
            PieDataSet set = new PieDataSet(entries, "User Reports");
            set.setColors(colors);
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.invalidate();
        }
    }

    private class UserBarChart extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            String id = params[0];
            String year = params[1];
            return networkConnection.barChart(id, year);
        }
        @Override
        protected void onPostExecute(String returnMessage){
            JSONArray jsonArray = new JSONArray();
            List<Integer> colors = new ArrayList<>();
            Map<Object, Object> map;
            Map<Object, Object> mapB = null;
            int i = 0, real = 0;
            int[] color = new int[] {
                    R.color.usubeni,
                    R.color.torin,
                    R.color.colorPrimaryDark,
                    R.color.momo,
                    R.color.sakura,
                    R.color.colorPrimary,
                    R.color.toki,
                    R.color.sabi,
                    R.color.ayame,
                    R.color.akak,
                    R.color.colorAccent,
                    R.color.ouchi
            };
            List<Map<Object, Object>> list = new ArrayList<>();
            for (i = 1; i <= 12; i ++){
                map = new HashMap<>();
                map.put(i, 0);
                list.add(map);
            }

            try {
                jsonArray = new JSONArray(returnMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (i = 0; i < jsonArray.length(); i++) {
                String month = "";
                String number = "";
                try {
                    month = jsonArray.getJSONObject(i).get("MONTH").toString();
                    number = jsonArray.getJSONObject(i).get("NumberPerMouth").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map = new HashMap<>();
                int intMonth = Integer.parseInt(month);
                int intNumber = Integer.parseInt(number);
                map.put(intMonth,intNumber);
                int position = Integer.parseInt(month);
                list.set(position - 1, map);
            }
            List<BarEntry> entries = new ArrayList<>();
            for (i = 0; i < 12; i ++){
                float position = (float)i;
                mapB = list.get(i);
                int a = i + 1;
                Object obj = mapB.get(a);
                int no = Integer.parseInt(obj.toString());
                float number = (float)no;
                entries.add(new BarEntry(position, number));
                colors.add(getResources().getColor(color[i]));
            }
            BarDataSet set = new BarDataSet(entries, "Year_Report");
            BarData data = new BarData(set);
            set.setColors(colors);
            data.setBarWidth(0.9f); // set custom bar width
            barChart.setData(data);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.invalidate(); // refresh

        }
    }


    private void startDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                startDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                startDate.setGravity(Gravity.CENTER);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void endDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                endDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                endDate.setGravity(Gravity.CENTER);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

}
