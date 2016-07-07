package com.example.tagy.d_requestpoint.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tagy.d_requestpoint.App.AppConfig;
import com.example.tagy.d_requestpoint.App.AppController;
import com.example.tagy.d_requestpoint.Helper.SQLiteHandler;
import com.example.tagy.d_requestpoint.Helper.SessionManager;
import com.example.tagy.d_requestpoint.HttpRmi;
import com.example.tagy.d_requestpoint.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    private static final String TAG="Q-request";
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    public TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
//        txt = (TextView) findViewById(R.id.textView);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        // Link to Register Screen
//        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        RegisterActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });



//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String email = inputEmail.getText().toString().trim();
//                String password = inputPassword.getText().toString().trim();
//
//                // Check for empty data in the form
//                if (!email.isEmpty() && !password.isEmpty()) {
//                    // login user
//                    checkLogin(email, password);
//                } else {
//                    // Prompt user to enter credentials
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter the credentials!", Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * function to verify login details in mysql db
     * */
//    private void checkLogin(final String email, final String password) {
//        // Tag used to cancel the request
//        String tag_string_req = "req_login";
//
//        pDialog.setMessage("Logging in ...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                AppConfig.URL_LOGIN, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("status");
//
//                    // Check for error node in json
//                    if (!error) {
//                        // user successfully logged in
//                        // Create login session
//                        session.setLogin(true);
//
//                        // Now store the user in SQLite
//                        String uid = jObj.getString("id");
//                        String points = jObj.getString("points");
////                        JSONObject user = jObj.getJSONObject("user");
////                        String name = user.getString("name");
////                        String email = user.getString("email");
////                        String created_at = user
////                                .getString("created_at");
//
//                        // Inserting row in users table
//                        db.addUser(email, uid);
//
//                        // Launch main activity
//                        Intent intent = new Intent(Login.this,
//                                MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("key","cGNwb3M");
//                params.put("email", email);
//                params.put("password", password);
//
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }



    public void validate(View view) {

            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            // Check for empty data in the form
            if (!email.isEmpty() && !password.isEmpty()) {
                // login user
                new LongOperation().execute(email,password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String resturning_response="";
            try {

                HttpRmi permissions = new HttpRmi("http://www.d-request.com/DSAPI/APIV1/login?key=cGNwb3M=&username="+params[0]+"&password="+params[1]);
                String response = permissions.execute();
                resturning_response=response.toString();
                Log.d("RESPONSE",response.toString());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return resturning_response;
        }

        @Override
        protected void onPostExecute(String result) {

            hideDialog();

            JSONObject jObj = null;
            try {
                jObj = new JSONObject(result.trim().toString());

            boolean error = jObj.getBoolean("status");
                Log.d("result", "" + result.trim().toString());
                if(error) {
                    Integer id = Integer.parseInt(jObj.getString("id"));
                    String points = jObj.getString("points");
                    String name = jObj.getString("full_name");
                    String email = jObj.getString("email");
                    Log.d("adduser", id + " " + name + " " + email + " " + points);
                    db.addUser(id, email, points, name);
                    session.setLogin(error);
                    Intent intent = new Intent(Login.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                    Toast.makeText(getApplicationContext(),
                            "Emain or Password isn't correct !", Toast.LENGTH_LONG)
                            .show();
                }

            } catch (JSONException e) {
            e.printStackTrace();
        }
        }

        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Logging in ...");
            showDialog();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}

