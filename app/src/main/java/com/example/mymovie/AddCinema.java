package com.example.mymovie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovie.networkconnection.NetworkConnection;


public class AddCinema extends AppCompatActivity {
    private EditText cinema_name, cinema_code;
    private String name = "", code = "";
    private Button btn, btn_cancel;
    final CheckCinema checkCinema = new CheckCinema();
    final GetDetails getDetails = new GetDetails();
    final PostCinema postCinema = new PostCinema();
    private NetworkConnection networkConnection = new NetworkConnection();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cinema);
        cinema_name = findViewById(R.id.cinema_name);
        cinema_code = findViewById(R.id.cinema_postcode);
        btn = findViewById(R.id.submit_add_cinema);
        btn_cancel = findViewById(R.id.cancel_add_cinema);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cinema_name.getText().toString().trim();
                code = cinema_code.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(AddCinema.this, "Please enter the Cinema Name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(AddCinema.this, "Please enter the Cinema Code.", Toast.LENGTH_LONG).show();
                    return;
                }
                checkCinema.execute(name, code);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCinema.this.finish();
            }
        });
    }

    private class CheckCinema extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            String name = params[0];
            String code = params[1];
            return networkConnection.checkCinema(name, code);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            String check = returnMessage;
            if (!check.equals("[]")){
                Toast.makeText(AddCinema.this, "Cinema exist.", Toast.LENGTH_LONG).show();
                return;
            } else {
                getDetails.execute();
            }
        }
    }

    private class GetDetails extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            return networkConnection.getMaxID();
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            int newID = Integer.parseInt(returnMessage) + 1;
            String ID = Integer.toString(newID);
            String[] details = new String[3];
            details[0] = ID;
            details[1] = name;
            details[2] = code;
            postCinema.execute(details);
        }
    }

    private class PostCinema extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            return networkConnection.addCinema(params);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            if (returnMessage.equals("")){
                Toast.makeText(AddCinema.this, "Add Successful.", Toast.LENGTH_LONG).show();
                AddCinema.this.finish();
            } else {
                Toast.makeText(AddCinema.this, "Something wrong. TRY AGAIN..", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }


}
