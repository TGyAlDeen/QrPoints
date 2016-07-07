package com.example.tagy.d_requestpoint.Activity;

/**
 * Created by TGy on 11/26/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tagy.d_requestpoint.Helper.ConnectionDetector;
import com.example.tagy.d_requestpoint.Helper.SQLiteHandler;
import com.example.tagy.d_requestpoint.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends Activity {
    private SQLiteHandler db;
    private String url ;
    private String TAG="TAG";
    private String id;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private JSONObject jObj;
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // check internet connection
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(!isInternetPresent){
            showAlertDialog(SplashScreen.this, "Internet Connection",
                    "You don't have internet connection", false);

        }
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        this.id = user.get("id");
        Log.d(TAG,"user id code is "+id);
        this.url="http://d-request.com/DSAPI/APIV1/loadPoints?key=123&user_id="+id;
        json_request();
    }

    protected void json_request(){
    RequestQueue queue = Volley.newRequestQueue(this);
    StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {

        public void onResponse(String response) {
            try {
                jObj = new JSONObject(response);
                Log.d(TAG,"response is "+response);
                String points = jObj.getString("points");
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                Log.d("response points", points);
                i.putExtra("points_from_server", points);
                startActivity(i);
//                 close this activity
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           Log.d(TAG, "on response error" + error.getMessage());
        }
    }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();

            params.put("key","cGNwb3M");
            params.put("user_id",id);
            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();
            params.put("Content-Type","application/x-www-form-urlencoded; application/json");
            return params;
        }
    };

    queue.add(sr);
}

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.exit(1);
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
    }