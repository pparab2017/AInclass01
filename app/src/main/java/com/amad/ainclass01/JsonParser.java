package com.amad.ainclass01;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pushparajparab on 8/27/17.
 */

public class JsonParser {

    static class JsonParse{

        static User Parse(String s) throws JSONException {
            User toReturn = new User();
            JSONObject jsonObject =new JSONObject(s);

            Log.d("obj",jsonObject.toString());
            toReturn.setStatus(jsonObject.getString("status"));
            if(toReturn.getStatus().toLowerCase().equals("ok") ) {

                toReturn.setfName(jsonObject.getString("userFname"));
                toReturn.setlName(jsonObject.getString("userLname"));
                if(jsonObject.has("token")) {
                    toReturn.setToken(jsonObject.getString("token"));
                }
                toReturn.setId(Integer.parseInt(jsonObject.getString("userId")));
                toReturn.setEmail(jsonObject.getString("userEmail"));
                toReturn.setGender(jsonObject.getString("gender"));
                toReturn.setAge(Integer.parseInt(jsonObject.getString("age")));
                toReturn.setWeight(Integer.parseInt(jsonObject.getString("weight")));
                toReturn.setAddress(jsonObject.getString("address"));
            }else
            {
                toReturn.setErrorMessage(jsonObject.getString("message"));
            }
            return  toReturn;
        }
    }
}
