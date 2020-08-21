package com.example.mymovie;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovie.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddToMemoir extends AppCompatActivity {
    private String title_temp = "", release_date_temp = "", id = "", mid = "";
    private String date = "", time = "", cinema = "", comment = "", rate = "", pid = "";
    private TextView tv_title, tv_release, tv_watch_date, tv_watch_time;
    private EditText et_comment;
    private float rateFloat = 0;
    private Button addCinema, btn_submit, btn_cancel;
    private ImageView imageView;
    private Spinner spinner;
    private RatingBar ratingBar;
    private String[] details = new String[9];
    private NetworkConnection networkConnection = new NetworkConnection();
    final AddImage addImage = new AddImage();
    final AddSpinner addSpinner = new AddSpinner();
    final GetCid getCid = new GetCid();
    final PostMemoir postMemoir = new PostMemoir();
    final GetMaxMid getMaxMid = new GetMaxMid();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_memoir);
        tv_title = findViewById(R.id.title_memoir);
        tv_release = findViewById(R.id.release_date_memoir);
        imageView = findViewById(R.id.image_memoir);
        tv_watch_date = findViewById(R.id.watch_date_memoir);
        tv_watch_time = findViewById(R.id.watch_time_memoir);
        addCinema = findViewById(R.id.add_cinema);
        spinner = findViewById(R.id.cinema_spinner_add_memoir);
        btn_submit = findViewById(R.id.submit_memoir);
        btn_cancel = findViewById(R.id.cancel_add_memoir);
        et_comment = findViewById(R.id.comment_memoir);
        ratingBar = findViewById(R.id.rating_memoir);

        Intent intent = getIntent();
        title_temp = intent.getStringExtra("title");
        release_date_temp = intent.getStringExtra("release");
        id = intent.getStringExtra("id");
        tv_title.setText(title_temp);
        tv_release.setText(release_date_temp);
        addImage.execute(id);
        addSpinner.execute();
        SharedPreferences settings = getSharedPreferences("setting", 0);
        pid = settings.getString("pid",id);

        tv_watch_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        tv_watch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        addCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddToMemoir.this, AddCinema.class);
                startActivity(intent1);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToMemoir.this.finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = tv_watch_date.getText().toString();
                time = tv_watch_time.getText().toString();
                cinema = spinner.getSelectedItem().toString();
                comment = et_comment.getText().toString().trim();
                rateFloat = ratingBar.getRating();
                rate = String.valueOf(rateFloat);
                if (TextUtils.isEmpty(date)){
                    Toast.makeText(AddToMemoir.this, "Please select the watching date.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(time)){
                    Toast.makeText(AddToMemoir.this, "Please select the watching time.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cinema.equals("Select cinema")){
                    Toast.makeText(AddToMemoir.this, "Please select the cinema.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(comment)){
                    Toast.makeText(AddToMemoir.this, "Please enter the comment.", Toast.LENGTH_LONG).show();
                return;
                }
                getMaxMid.execute();
            }
        });
    }

    private class GetMaxMid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMaxMid();
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            int i = Integer.parseInt(returnMessage) + 1;
            mid = String.valueOf(i);
            details[0] = mid;
            details[1] = title_temp;
            details[2] = release_date_temp + "T00:00:00+10:00";
            details[3] = "1970-01-01T" + time + ":00+10:00";
            details[4] = date + "T00:00:00+11:00";
            details[5] = comment;
            details[6] = rate;
            details[7] = pid;
            getCid.execute(cinema);
        }
    }

    private class AddImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String keyword = params[0];
            return networkConnection.GetImage(keyword);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            String post_path = returnMessage;
            String url = "https://image.tmdb.org/t/p/original";
            String final_path = url + post_path;
            Picasso.get()
                    .load(final_path)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 296)
                    .centerInside()
                    .into(imageView);
        }
    }

    private class AddSpinner extends AsyncTask<String, Void, List> {
        @Override
        protected List doInBackground(String... params) {
            return networkConnection.getAllCinema();
        }
        @Override
        protected void onPostExecute(List returnMessage) {
            List cinema = returnMessage;
            int a = 0;
            List<String> list = new ArrayList<String>();
            list.add("Select cinema");
            for (a = 0; a < cinema.size(); a++){
                list.add(cinema.get(a).toString());
            }
            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddToMemoir.this, android.R.layout.simple_spinner_item, list);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    private class GetCid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String keyword = params[0];
            return networkConnection.getCid(keyword);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            details[8] = returnMessage;
            postMemoir.execute(details);
        }
    }

    private class PostMemoir extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... params) {
            String[] keyword = params[0];
            return networkConnection.addMemoir(keyword);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            if (returnMessage.equals("")){
                Toast.makeText(AddToMemoir.this, "Add Successful.", Toast.LENGTH_LONG).show();
                AddToMemoir.this.finish();
            } else {
                Toast.makeText(AddToMemoir.this, "Something wrong. TRY AGAIN..", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }


    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddToMemoir.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                tv_watch_date.setText(new StringBuilder()
                .append(year)
                .append("-")
                .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1))
                .append("-")
                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tv_watch_time.setText("" + hourOfDay + ":" + minute);
            }
        }, hourOfDay, minute, true).show();
    }
}
