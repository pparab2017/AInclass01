package com.amad.ainclass01;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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


public class UserProfile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView txtWelcome,txtAge,txtWeight,txtAddress;
    private User mLoginUser;
    private ImageView userIcon;
    private final OkHttpClient client = new OkHttpClient();
    final static String USER_EDIT = "userEdit";
    final static int USER_EDIT_CODE = 00101;

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_user, popup.getMenu());
        popup.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            switch (requestCode) {
                case USER_EDIT_CODE:
                    User mUpdateUser = (User) data.getExtras().getParcelable(EditProfile.UPDATED_USER);
                    mLoginUser  = mUpdateUser;
                    makeDisplayScreen(mLoginUser);
                    Log.d("test", mUpdateUser.toString());
                    break;
            }
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Log.d("logout","Logging off");
                SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                if(mPrefs.contains("MyAppKey"))
                {
                    prefsEditor.remove("MyAppKey");
                    Intent i = new Intent(UserProfile.this,MainActivity.class);
                    startActivity(i);
                }
                else
                {

                }
                prefsEditor.commit();
                return true;

            case R.id.editInfo:
                Intent editUser = new Intent(UserProfile.this, EditProfile.class);
                editUser.putExtra(USER_EDIT, mLoginUser );
                startActivityForResult(editUser, USER_EDIT_CODE);

                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mPrefs = getSharedPreferences(MainActivity.STORED,MODE_PRIVATE);

        if(!mPrefs.contains("MyAppKey"))
        {
            Intent i = new Intent(UserProfile.this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3f51b5")));
        }

        if(getIntent().hasExtra(MainActivity.LOGGEDIN_USER)) {
            if (getIntent().getExtras().containsKey(MainActivity.LOGGEDIN_USER)) {
                mLoginUser = (User) getIntent().getExtras().getParcelable(MainActivity.LOGGEDIN_USER);
                Log.d("Intent", mLoginUser.toString());
                makeDisplayScreen(mLoginUser);
            }
        }else  if(getIntent().hasExtra(MainActivity.LOGGEDIN_TOKEN))
        {
            if (getIntent().getExtras().containsKey(MainActivity.LOGGEDIN_TOKEN)) {
                String userToken  = (String) getIntent().getExtras().getString(MainActivity.LOGGEDIN_TOKEN);
                Log.d("token", userToken);
                makeAPICallForUserInfo(userToken);
            }
        }

    }

    private void makeDisplayScreen(User user)
    {
        txtWelcome = (TextView) findViewById(R.id.text_Welcome);
        txtAge = (TextView) findViewById(R.id.text_age);
        txtWeight = (TextView) findViewById(R.id.text_weight);
        txtAddress = (TextView) findViewById(R.id.text_address);
        userIcon = (ImageView)findViewById(R.id.image_user);

        if(user.getGender().equals("MALE"))
        {
            userIcon.setImageResource(R.mipmap.boy);
        }else
        {
            userIcon.setImageResource(R.mipmap.girl);
        }
        txtWelcome.setText(user.getUserFullName());
        txtAge.setText(user.getAge() +"");
        txtWeight.setText(user.getWeight()+"");
        txtAddress.setText(user.getAddress());
    }

    private void makeAPICallForUserInfo(final String token)
    {

        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(Utils.Api_url.USER_INFO.toString())
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

                UserProfile.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoginUser = new User();
                        try {
                            mLoginUser = JsonParser.JsonParse.Parse(responseData);
                            makeDisplayScreen(mLoginUser);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

}
