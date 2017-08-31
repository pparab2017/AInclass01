package com.amad.ainclass01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    EditText fname,lname,age,weight,address;
    Button btnSave,btnCanel;
    final static String UPDATED_USER = "Updated_user";
    private User mLoggedInUser,updatedUser;
    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fname = (EditText)findViewById(R.id.editText_fname);
        lname = (EditText)findViewById(R.id.editText_lname);
        age = (EditText)findViewById(R.id.editText_age);
        weight = (EditText)findViewById(R.id.editText_weight);
        address = (EditText)findViewById(R.id.editText_address);
        if(getIntent().hasExtra(UserProfile.USER_EDIT)) {
            if (getIntent().getExtras().containsKey(UserProfile.USER_EDIT)) {
                mLoggedInUser = (User) getIntent().getExtras().getParcelable(UserProfile.USER_EDIT);
                Log.d("Edit Intent", mLoggedInUser.toString());
                makeEditFormReady(mLoggedInUser);
            }
        }




        btnSave =(Button) findViewById(R.id.button_saveMyInfo);
        btnSave.setOnClickListener(this);

        btnCanel =(Button) findViewById(R.id.button_cancel);
        btnCanel.setOnClickListener(this);

    }

    private void makeEditFormReady(User user)
    {

        fname.setText(user.getfName());
        lname.setText(user.getlName());
        age.setText(user.getAge()+"");
        weight.setText(user.getWeight()+"");
        address.setText(user.getAddress());
    }


    private boolean ValidateForm()
    {
        boolean toCheck  =false;
        if(fname.getText().toString().equals(""))
        {
            fname.setError("Please Enter First name!");
            toCheck  =true;
        }
        if(lname.getText().toString().equals(""))
        {
            lname.setError("Please Enter Last name!");
            toCheck  =true;
        }

        if(age.getText().toString().equals(""))
        {
            age.setError("Please enter age!");
            toCheck  =true;
        }
        if(weight.getText().toString().equals(""))
        {
            weight.setError("Please enter weight!");
            toCheck  =true;
        }
        if(address.getText().toString().equals(""))
        {
            address.setError("Please enter address!");
            toCheck  =true;
        }

        return  toCheck;
    }



    private void PostUpdates()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("fname",fname.getText().toString())
                .add("lname",lname.getText().toString())
                .add("weight", weight.getText().toString())
                .add("age",age.getText().toString())
                .add("gender",mLoggedInUser.getGender())
                .add("address", address.getText().toString())
                .build();

        SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
        if(mPrefs.contains("MyAppKey")) {
            String token = (String) mPrefs.getString("MyAppKey", null);


            Request request = new Request.Builder()
                    .url(Utils.Api_url.UPDATE_INFO.toString())
                    .addHeader("Authorization", "BEARER " + token)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();
                    Log.d("userInfo", responseData);

                    EditProfile.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatedUser = new User();
                            try {
                                updatedUser = JsonParser.JsonParse.Parse(responseData);
                                Intent toSend = new Intent();
                                toSend.putExtra(UPDATED_USER, updatedUser);
                                setResult(RESULT_OK, toSend);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_saveMyInfo:

                if(!ValidateForm())
                {
                    PostUpdates();
                }
                break;
            case R.id.button_cancel:
                Intent toSend = new Intent();
                toSend.putExtra(UPDATED_USER,mLoggedInUser);
                setResult(RESULT_OK,toSend);
                finish();
                break;

        }
    }
}
