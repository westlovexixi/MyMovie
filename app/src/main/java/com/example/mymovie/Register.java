package com.example.mymovie;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovie.networkconnection.NetworkConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private Button btn_cancel, btn_submit;
    private TextView showDialog;
    private String username, psd, repsd, fname, lname, stNo, stName, state, postcode, dob, gender, password, signupDate;
    private int newPid, creid;
    private RadioGroup radioGroup;
    private String[] details = new String[9];
    private String[] creDetails = new String[5];
    private EditText et_su_username, et_su_psd, et_su_re_psd, et_su_fname, et_su_lname, et_su_stNo, et_su_stName, et_su_state, et_su_postcode;
    private Spinner sp_su_state;
    private CheckBox cbpsd, cbrepsd;
    NetworkConnection networkConnection = new NetworkConnection();
    private boolean check = false;
    final CheckEmail checkEmail = new CheckEmail();
    final GetMaxPid getMaxPid = new GetMaxPid();
    final AddPerson addPerson = new AddPerson();
    final GetMaxCreid getMaxCreid = new GetMaxCreid();
    final AddCredential addCredential = new AddCredential();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btn_cancel = findViewById(R.id.btn_su_cancel);
        btn_submit = findViewById(R.id.btn_su_submit);
        showDialog = findViewById(R.id.tv_su_showDialog);
        et_su_username = findViewById(R.id.et_su_username);
        et_su_psd = findViewById(R.id.et_su_password);
        et_su_re_psd =findViewById(R.id.et_su_re_password);
        et_su_fname = findViewById(R.id.et_su_fname);
        et_su_lname = findViewById(R.id.et_su_lname);
        et_su_stNo = findViewById(R.id.et_su_stno);
        et_su_stName = findViewById(R.id.et_su_stname);
        et_su_postcode = findViewById(R.id.et_su_postcode);
        sp_su_state = findViewById(R.id.sp_su_state);
        cbpsd = findViewById(R.id.cbPsd);
        cbrepsd = findViewById(R.id.cbRePsd);
        radioGroup = findViewById(R.id.rgroup);


        //if checked, show password
        cbpsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    et_su_psd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    et_su_psd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //if checked, show re-enter password
        cbrepsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    et_su_re_psd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_su_re_psd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //gender
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int checkedId)
            {
                switch(checkedId){
                    case R.id.ch1:gender = "male";break;
                    case R.id.ch2:gender = "female";break;
                }
            }
        });


        //cancel button
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.this.finish();
            }
        });

        //show date for register to choose
        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this, "Please enter the username.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!isEmail(username)){
                    Toast.makeText(Register.this, "Please enter the proper username.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(psd)){
                    Toast.makeText(Register.this, "Please enter the password.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(repsd)){
                    Toast.makeText(Register.this, "Please re-enter the password.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!repsd.equals(psd)){
                    Toast.makeText(Register.this, "Please enter the same password.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(fname)){
                    Toast.makeText(Register.this, "Please enter the first name.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(lname)){
                    Toast.makeText(Register.this, "Please enter the last name.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(dob)){
                    Toast.makeText(Register.this, "Please select the date of birth.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(stNo)){
                    Toast.makeText(Register.this, "Please enter No of street.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(stName)){
                    Toast.makeText(Register.this, "Please enter Name of street.", Toast.LENGTH_LONG).show();
                    return;
                } else if (state.equals("Please select your state")){
                    Toast.makeText(Register.this, "Please select your state.", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(postcode)){
                    Toast.makeText(Register.this, "Please enter your postcode.", Toast.LENGTH_LONG).show();
                    return;
                }
                password = et_su_psd.getText().toString();
                checkEmail.execute(username);
            }
        });
    }

    private String hashPsd(String passWord){
        String hashtext = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(passWord.getBytes());
            for (byte b : bytes)
            {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1)
                {
                    temp = "0" + temp;
                }
                hashtext += temp;
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                showDialog.setText(new StringBuilder()
                        .append(year)
                        .append("-")
                        .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1))
                        .append("-")
                        .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void getEditString(){
        username = et_su_username.getText().toString().trim();
        psd = et_su_psd.getText().toString().trim();
        repsd = et_su_re_psd.getText().toString().trim();
        fname = et_su_fname.getText().toString().trim();
        lname = et_su_lname.getText().toString().trim();
        stNo = et_su_stNo.getText().toString().trim();
        dob = showDialog.getText().toString();
        stName = et_su_stName.getText().toString().trim();
        state = sp_su_state.getSelectedItem().toString();
        postcode = et_su_postcode.getText().toString().trim();
    }


    private boolean isEmail(String username){
        boolean email = false;
        String pattern =  "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        boolean isMatch = Pattern.matches(pattern, username);
        if (isMatch){
            email = true;
        }
        return email;
    }

    private class CheckEmail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            return networkConnection.findByUsername(username);
        }
        @Override protected void onPostExecute(String state) {
            if (state != null){
                check = true;
            } else {
                check = false;
            }
            if (check = true){
                getMaxPid.execute();
            } else {
                Toast.makeText(Register.this, "Please enter another email.", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private class GetMaxPid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMaxPid();
        }
        @Override protected void onPostExecute(String returnMessage) {
            newPid = Integer.parseInt(returnMessage) + 1;
            details[0] = String.valueOf(newPid);
            details[1] = fname;
            details[2] = lname;
            details[3] = gender;
            details[4] = dob + "T00:00:00+10:00";
            details[5] = stNo;
            details[6] = stName;
            details[7] = state;
            details[8] = postcode;
            addPerson.execute(details);
        }
    }

    private class AddPerson extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... params) {
            String[] keyword = params[0];
            return networkConnection.addPerson(keyword);
        }
        @Override protected void onPostExecute(String returnMessage) {
            if (returnMessage.equals("")){
                getMaxCreid.execute();
            }else {
                Toast.makeText(Register.this, "Ops, TRY AGAIN!", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private class GetMaxCreid extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMaxCreid();
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            creid = Integer.parseInt(returnMessage) + 1;
            String no = String.valueOf(creid);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            signupDate = simpleDateFormat.format(date);
            creDetails[0] = no;
            creDetails[1] = username;
            creDetails[2] = hashPsd(password);
            creDetails[3] = signupDate + "T00:00:00+11:00";
            creDetails[4] = String.valueOf(newPid);
            addCredential.execute(creDetails);
        }
    }

    private class AddCredential extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... params) {
            String[] keyword = params[0];
            return networkConnection.addCredential(keyword);
        }
        @Override
        protected void onPostExecute(String returnMessage) {
            if (returnMessage.equals("")){
                Toast.makeText(Register.this, "Register successful!", Toast.LENGTH_LONG).show();
                Register.this.finish();
            } else{
                Toast.makeText(Register.this, "Ops, TRY AGAIN!", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
}
