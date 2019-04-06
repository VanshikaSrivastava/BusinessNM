package tieup.business_network.appmart.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.fragments.Registration;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.Utils.ServerAddress.LOGIN_URL;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    EditText edtMobile, edtPassword;
    private SharedPreferences saveUserDetails;
    Button btnLogin;
    TextView txtRegister;
    Fragment fragment;
    boolean success;
    RelativeLayout mainContainer;
    String user_profile, user_id, user_name, user_emailid, user_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        SharedPreferences SavedDetails = PreferenceManager.getDefaultSharedPreferences(Login.this);
        final String user_id = SavedDetails.getString("user_id", "");


        if (!user_id.isEmpty()) {
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
            finish();
        }

        edtMobile = findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.edtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        mainContainer = findViewById(R.id.mainContainer);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkConnectivity()) {
                        login_request();
                    }
                }
            });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new Registration();
                loadFragment();
            }
        });

    }


    private void loadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainContainer, fragment).addToBackStack(String.valueOf(fm)).commit();

    }

    private void login_request() {


        @SuppressLint("StaticFieldLeak")
        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("user_mobile", edtMobile.getText().toString())
                        .add("user_password", edtPassword.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(LOGIN_URL)
                        .post(requestBody)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    JSONObject object = new JSONObject(response.body().string());

                    success = object.getBoolean("success");

                    if (success) {
                        user_id = object.getString("user_id");
                        user_name = object.getString("user_name");
                        user_emailid = object.getString("user_emailid");
                        user_mobile = object.getString("user_mobile");
                        user_profile = object.getString("user_profile");
                        object.getString("user_password");
                        object.getString("user_confirm_password");


                        saveUserDetails = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor = saveUserDetails.edit();
                        editor.putString("user_id", user_id);
                        editor.putString("user_name", user_name);
                        editor.putString("user_emailid", user_emailid);
                        editor.putString("user_mobile", user_mobile);
                        editor.putString("user_profile", user_profile);
                        editor.commit();


                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                if (success) {
                    Toast.makeText(Login.this, "Welcome " + user_name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, " Invalid Mobile number or Password ", Toast.LENGTH_SHORT).show();
                }

            }
        };

        task.execute();

    }
    public boolean checkConnectivity() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    public void showSnack(boolean isConnected) {
        if (isConnected) {
            Snackbar.make(mainContainer, getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(mainContainer, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }).setActionTextColor(Color.RED)
                    .show();
        }
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);

    }
}
