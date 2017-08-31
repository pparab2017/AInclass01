package com.amad.ainclass01;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private User mLoginUser;
    final static String LOGGEDIN_USER = "LOGGED_IN_USER";
    final static String LOGGEDIN_TOKEN = "LOGGED_IN_USER_TOKEN";
    final static String STORED = "stored";
    private TextView txt_userName, txt_password, txt_errorMsg, link_signUp;
    private final OkHttpClient client = new OkHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = (Button) findViewById(R.id.button_Login);
        btn_login.setOnClickListener(this);
        txt_errorMsg = (TextView) findViewById(R.id.text_errorMsg);
        link_signUp = (TextView) findViewById(R.id.text_signUp);
        link_signUp.setOnClickListener(this);
        txt_userName = (TextView)findViewById(R.id.text_userName);
        txt_password =(TextView)findViewById(R.id.text_password);
    }


    private boolean validateForm()
    {
        boolean toReturn = false;
        if(txt_userName.getText().toString().equals(""))
        {
            txt_userName.setError("Please enter username!");
            toReturn = true;
        }
        else{
            if(!Utils.isEmailValid(txt_userName.getText().toString()))
            {
                txt_userName.setError("Please enter proper username (email)!");
                toReturn = true;
            }
        }
        if(txt_password.getText().toString().equals(""))
        {
            txt_password.setError("Please enter password!");
            toReturn = true;
        }

        return  toReturn;

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);

        if(mPrefs.contains("MyAppKey"))
        {
            String token = (String)  mPrefs.getString("MyAppKey",null);
            Intent i = new Intent(MainActivity.this,UserProfile.class);
            i.putExtra(LOGGEDIN_TOKEN,token);
            startActivity(i);
        }
    }


    private void login()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("email",txt_userName.getText().toString())
                .add("password", txt_password.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(Utils.Api_url.LOGIN.toString())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();
                //Run view-related code back on the main thread
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoginUser = new User();
                        try {
                            mLoginUser = JsonParser.JsonParse.Parse(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("json",mLoginUser.toString() );
                        if(mLoginUser.getToken() != null)
                        {
                            txt_errorMsg.setText("Logged in Success!");
                            SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();

                            prefsEditor.putString("MyAppKey", mLoginUser.getToken());
                            prefsEditor.commit();

                            Intent i = new Intent(MainActivity.this,UserProfile.class);
                            i.putExtra(LOGGEDIN_USER,mLoginUser);
                            startActivity(i);
                            finish();
                        }
                        else{
                            if(mLoginUser.getStatus().toLowerCase().equals("error")){
                                txt_errorMsg.setText(mLoginUser.getErrorMessage());
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {

        txt_errorMsg.setText("");

        switch (v.getId())
        {
            case R.id.button_Login:

                if(!validateForm())
                {
                    login();
                }
                break;

            case R.id.text_signUp:
                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);

                break;
        }
        txt_errorMsg.setText("");
        txt_userName = (TextView)findViewById(R.id.text_userName);
        txt_password =(TextView)findViewById(R.id.text_password);




    }
}
