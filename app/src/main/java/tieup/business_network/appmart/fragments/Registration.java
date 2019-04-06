package tieup.business_network.appmart.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.ConstantMethods;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.Validation;
import tieup.business_network.appmart.activities.Login;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.Utils.ServerAddress.ADD_USER_URL;

public class Registration extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    EditText edtUserName, edtUserEmailid, edtUserMobile, edtUserPassword, edtUserConfirmPassword;
    Button btnRegister;
    boolean success;
    ScrollView registrationLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        registrationLayout = view.findViewById(R.id.registrationLayout);
        edtUserName = view.findViewById(R.id.edtUserName);
        edtUserEmailid = view.findViewById(R.id.edtUserEmailid);
        edtUserMobile = view.findViewById(R.id.edtUserMobile);
        edtUserPassword = view.findViewById(R.id.edtUserPassword);
        edtUserConfirmPassword = view.findViewById(R.id.edtUserConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    if (checkConnectivity()) {
                        add_user();
                    }
                }
            }
        });

        return view;
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isName(edtUserName, true)) ret = false;
        if (!Validation.isPhoneNumber(edtUserMobile, true)) ret = false;
        if (!Validation.isEmailAddress(edtUserEmailid, true)) ret = false;
        if (!Validation.hasText(edtUserPassword, true)) ret = false;
        if (!Validation.hasText(edtUserConfirmPassword, true)) ret = false;
        return ret;
    }

    private void add_user() {

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("user_name", edtUserName.getText().toString())
                        .add("user_emailid", edtUserEmailid.getText().toString())
                        .add("user_mobile", edtUserMobile.getText().toString())
                        .add("user_password", edtUserPassword.getText().toString())
                        .add("user_confirm_password", edtUserConfirmPassword.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(ADD_USER_URL)
                        .post(requestBody)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    JSONObject object = new JSONObject(response.body().string());

                    success = object.getBoolean("success");

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
                    ConstantMethods.showOKMessageandBack("Success","Register success",getContext(), Login.class,true);

                } else {
                    Toast.makeText(getContext(), "Already register ", Toast.LENGTH_SHORT).show();
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
            Snackbar.make(registrationLayout, getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(registrationLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
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
