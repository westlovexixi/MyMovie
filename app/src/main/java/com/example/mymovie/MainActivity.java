package com.example.mymovie;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymovie.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Button btn_signup, btn_signin;
    private String userName, passWord, md5Psd, id, fname;
    private Boolean isCheck = false;
    private EditText et_username, et_password;
    private CheckBox cb_login;
    private ImageView imageView;
    NetworkConnection networkConnection = new NetworkConnection();
    final CheckUser checkUser = new CheckUser();
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        imageView = findViewById(R.id.imageView_sign_in);


        imageView.setImageDrawable(getResources().getDrawable(R.drawable.visibility_off_24px));

        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck){
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.visibility_off_24px));
                    isCheck = false;
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.visibility_24px));
                    isCheck = true;
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = et_username.getText().toString().trim();
                passWord = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(MainActivity.this, "Please enter the username.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!isEmail(userName)){
                    Toast.makeText(MainActivity.this, "Please enter the proper username.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(passWord)){
                    Toast.makeText(MainActivity.this, "Please enter the password.", Toast.LENGTH_LONG).show();
                    return;
                }
                md5Psd = hashPsd(passWord);
                checkUser.execute(userName, md5Psd);
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

    private boolean isEmail(String username){
        boolean email = false;
        String pattern =  "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        boolean isMatch = Pattern.matches(pattern, username);
        if (isMatch){
            email = true;
        }
        return email;
    }

    private class CheckUser extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params){
            String username = params[0];
            String psd = params[1];
            return networkConnection.checkUser(username,psd);
        }
        @Override
        protected void onPostExecute(String state){
            if (!state.equals("[]")){
                try {
                    jsonArray = new JSONArray(state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject = (JSONObject) jsonArray.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    id = jsonObject.get("pid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    fname = jsonObject.get("fname").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Login successful.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                UserInfo.setPreID(id);
                intent.putExtra("id", id);
                intent.putExtra("fname", fname);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please check you username/password.", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}
