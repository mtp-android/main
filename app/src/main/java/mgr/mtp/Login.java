package mgr.mtp;

/**
 * Created by lukas on 21.02.2016.
 */
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

// test commit from Android Studio

/**
 *
 * Login Activity Class
 *
 */
public class Login extends Activity {

    public static final String MY_PREFS_NAME = "MyPreferencesFile";

    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText emailET;
    EditText pwdET;
    CheckBox rememberCredentials;
    SharedPreferences prefs;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        errorMsg = (TextView)findViewById(R.id.login_error);
        emailET = (EditText)findViewById(R.id.loginEmail);
        pwdET = (EditText)findViewById(R.id.loginPassword);
        rememberCredentials = (CheckBox) findViewById(R.id.rememberCredentials);

        prefs = getPreferences(MODE_PRIVATE);

        boolean savedCredentials = prefs.getBoolean("rememberCredentials", false);

        if(savedCredentials)
            checkIfHasStoredUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.login));

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    public void loginUser(View view){

        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();
        RequestParams params = new RequestParams();

        if(Utility.isNotNull(email) && Utility.isNotNull(password)){

            if(Utility.validate(email)){

                params.put("username", email);
                params.put("password", password);
                invokeWS(params);
            }

            else{
                Toast.makeText(getApplicationContext(),getString(R.string.invalidMailFormat), Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(),getString(R.string.fillAllFields), Toast.LENGTH_LONG).show();
        }

    }


    public void checkIfHasStoredUser()
    {

        String email = prefs.getString("email", null);
        String password = prefs.getString("pass", null);

        if(email != null && password != null)
        navigateToHomeActivity();
    }

    public void storeCredentials(String email,String password)
    {
        if(rememberCredentials.isChecked())
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("pass",password);
            editor.putBoolean("rememberCredentials", true);
            editor.commit();
        }
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params){

        prgDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host+"/login/dologin", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    prgDialog.hide();
                    try {

                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("status")) {
                            Toast.makeText(getApplicationContext(),getString(R.string.successLogin), Toast.LENGTH_LONG).show();
                            navigateToHomeActivity();
                        } else {
                            errorMsg.setText(obj.getString("error_msg"));
                            Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(),getString(R.string.wrongJson), Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody, "UTF-8");

                    prgDialog.hide();

                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.serverError), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigateToHomeActivity(){

        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();

        storeCredentials(email,password);

        Intent homeIntent = new Intent(getApplicationContext(),Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void navigateToRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),Register.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }


}