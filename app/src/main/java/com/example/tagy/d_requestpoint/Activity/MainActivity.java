package com.example.tagy.d_requestpoint.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tagy.d_requestpoint.Helper.SQLiteHandler;
import com.example.tagy.d_requestpoint.Helper.SessionManager;
import com.example.tagy.d_requestpoint.HttpRmi;
import com.example.tagy.d_requestpoint.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private  TextView txtPoints;
    private TextView txtQr;
    private TextView txtError;
    private ProgressDialog pDialog;
    String TAG="TAG";
    private  JSONObject jObj;
    // Strings
    private String id;
    private String url ="http://d-request.com/rewards2/PAPI/trans_service";
    private String code;
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (TextView) findViewById(R.id.name);
        txtPoints = (TextView) findViewById(R.id.points);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        this.id = user.get("id");
        String name = user.get("name");
        String email = user.get("email");
        //comming from splash screen
        Intent i = getIntent();
        String points = i.getStringExtra("points_from_server");
        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        txtPoints.setText(points);
        Log.d("data in main activity",email);
        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            final String contents = result.getContents();
            if (contents != null) {
                // send json object data
                this.code=contents;
                Log.d(TAG,"before start");
                json_request();
                Log.d(TAG, "after start");
//                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
//               txtQr.setText(contents.toString());
            } else {
                Toast.makeText(this,"Error please try again",Toast.LENGTH_LONG).show();

            }
        }
    }

    public void start_qr(View view) {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);

        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
        //stomize the prompt message before scanning
        integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
//        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }


void hideDialog(){

    if(pDialog.isShowing()){
        pDialog.dismiss();
    }
}

protected void json_request(){
    RequestQueue queue = Volley.newRequestQueue(this);
    pDialog.setMessage("please wait..");
    pDialog.show();
    StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                jObj = new JSONObject(response.trim().toString());
                String points = jObj.getString("points");
                if(Integer.parseInt(points)==0) {
                    txtQr.setText(response);
                    txtError.setText(code);

                }else{
                    txtQr.setText(response);
                }
                pDialog.dismiss();
                Log.d(TAG, "Response" + response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           Log.d(TAG,"on response error"+error.getMessage());
        }
    }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("code",code);
            params.put("customer_id",id);
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



}
