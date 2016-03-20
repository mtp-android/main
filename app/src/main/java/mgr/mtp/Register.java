package mgr.mtp;

/**
 * Created by lukas on 21.02.2016.
 */
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 *
 * Register Activity Class
 */
public class Register extends Activity {

    ProgressDialog prgDialog;
    TextView errorMsg;

    EditText nameET;
    EditText emailET;
    EditText pwdET;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        errorMsg = (TextView)findViewById(R.id.register_error);
        nameET = (EditText)findViewById(R.id.registerName);
        emailET = (EditText)findViewById(R.id.registerEmail);
        pwdET = (EditText)findViewById(R.id.registerPassword);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.registration));

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(View view){

        String name = nameET.getText().toString();
        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();
        RequestParams params = new RequestParams();

        if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){

            if(Utility.validate(email)){

                params.put("name", name);
                params.put("username", email);
                params.put("password", password);

                invokeWS(params);
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.invalidMailFormat), Toast.LENGTH_LONG).show();
            }
        }

        else{
            Toast.makeText(getApplicationContext(), getString(R.string.fillAllFields), Toast.LENGTH_LONG).show();
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
        client.get(Constants.host+"/register/doregister",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");

                    prgDialog.hide();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.getBoolean("status")){

                            setDefaultValues();
                            Toast.makeText(getApplicationContext(), getString(R.string.successRegister), Toast.LENGTH_LONG).show();
                        }
                        else{
                            errorMsg.setText(obj.getString("error_msg"));
                            Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongJson), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(responseBody != null)
                    try {
                        String response = new String(responseBody, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                prgDialog.hide();

                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), getString(R.string.serverError), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),Login.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }


    public void setDefaultValues(){
        nameET.setText("");
        emailET.setText("");
        pwdET.setText("");
    }

}