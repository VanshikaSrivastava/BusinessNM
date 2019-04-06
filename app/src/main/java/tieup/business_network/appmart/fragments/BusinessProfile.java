package tieup.business_network.appmart.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.ConstantMethods;
import tieup.business_network.appmart.Validation;
import tieup.business_network.appmart.activities.Home;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.adapters.SearchListViewAdapter;
import tieup.business_network.appmart.models.CategorySearchModel;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.services.MyService.categoryArrayList;
import static tieup.business_network.appmart.Utils.ServerAddress.BUSINESS_PROFILE_URL;

public class BusinessProfile extends android.support.v4.app.Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{

    EditText edtUserName, edtBusinessTitle, edtUserPlace, edtUserMobileNumber, edtUserWhatsappNo, edtUserEmail, edtUserWebsite,edtAbout, edtBusinessYear;
    Button btnSave, btnCancel;
    public static Dialog dialog;
    ListView listView;
    public static TextView txtBusinessCategory;
    boolean success;
    private EditText edtTextSearch;
    SearchListViewAdapter searchListViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.business_profile, container, false);

        setHasOptionsMenu(true);

        ((Home) getActivity())
                .setActionBarTitle("My Profile");
        edtUserName = view.findViewById(R.id.edtUserName);
        edtBusinessTitle = view.findViewById(R.id.edtBusinessTitle);
        edtUserPlace = view.findViewById(R.id.edtUserPlace);
        edtUserMobileNumber = view.findViewById(R.id.edtUserMobileNumber);
        edtUserWhatsappNo = view.findViewById(R.id.edtUserWhatsappNo);
        edtUserEmail = view.findViewById(R.id.edtUserEmail);
        edtUserWebsite = view.findViewById(R.id.edtUserWebsite);
        edtAbout = view.findViewById(R.id.edtAbout);
        edtBusinessYear = view.findViewById(R.id.edtBusinessYear);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        txtBusinessCategory = view.findViewById(R.id.txtBusinessCategory);

        edtUserName.setEnabled(false);
        edtUserEmail.setEnabled(false);
        edtUserMobileNumber.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    if (checkConnectivity()) {
                        add_profile_request();
                    }
                }
                }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 1; i< fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            }
        });

        SharedPreferences SavedDetails = PreferenceManager.getDefaultSharedPreferences(getContext());

        edtUserName.setText(SavedDetails.getString("user_name", " "));
        edtUserEmail.setText(SavedDetails.getString("user_emailid", " "));
        edtUserMobileNumber.setText(SavedDetails.getString("user_mobile", " "));

        txtBusinessCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });
        return view;
    }

    private void loadDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.show_all);
        dialog.setCancelable(true);

        listView = (ListView) dialog.findViewById(R.id.mobile_list);

        edtTextSearch = (EditText) dialog.findViewById(R.id.edtTextSearch);

        edtTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchListViewAdapter.getFilter().filter(edtTextSearch.getText().toString());

            }
        });

        searchListViewAdapter = new SearchListViewAdapter( getContext(), getFilterList());
        listView.setAdapter(searchListViewAdapter);

        dialog.show();


    }

    @Override
    public void onDetach() {
        ((Home) getActivity())
                .setActionBarTitle("Home");
        super.onDetach();

    }

    private ArrayList<CategorySearchModel> getFilterList() {
        ArrayList<CategorySearchModel> searchModels = new ArrayList<CategorySearchModel>();

        CategorySearchModel p;
        for (int i = 0; i < categoryArrayList.size(); i++) {
            p = new CategorySearchModel(categoryArrayList.get(i));
            searchModels.add(p);
        }
        return searchModels;
    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isName(edtUserName, true)) ret = false;
        if (!Validation.hasText(edtBusinessTitle,true)) ret = false;
        if (!Validation.hasText(edtBusinessYear,true)) ret = false;
        if (!Validation.hasText(edtAbout,true)) ret = false;
        if (!Validation.hasText(edtUserPlace,true)) ret = false;
        if (!Validation.hasText(edtUserWebsite,true)) ret = false;
        if (!Validation.isPhoneNumber(edtUserMobileNumber, true)) ret = false;
        if (!Validation.isPhoneNumber(edtUserWhatsappNo, true)) ret = false;
        if (!Validation.isEmailAddress(edtUserEmail, true)) ret = false;

        return ret;
    }

    private void add_profile_request() {
        SharedPreferences SavedDetails = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String user_id = SavedDetails.getString("user_id", "");

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("user_id",user_id)
                        .add("user_name", edtUserName.getText().toString())
                        .add("business_title", edtBusinessTitle.getText().toString())
                        .add("business_category", txtBusinessCategory.getText().toString())
                        .add("place", edtUserPlace.getText().toString())
                        .add("user_mobile_number", edtUserMobileNumber.getText().toString())
                        .add("user_whatsapp_number", edtUserWhatsappNo.getText().toString())
                        .add("user_mail", edtUserEmail.getText().toString())
                        .add("user_website", edtUserWebsite.getText().toString())
                        .add("about_business", edtAbout.getText().toString())
                        .add("business_year", edtBusinessYear.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url(BUSINESS_PROFILE_URL)
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
                   ConstantMethods.showOKMessageandBack("Success","Register success",getContext(),Home.class,true);
                   //Toast.makeText(getContext(), "Register success", Toast.LENGTH_SHORT).show();

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
              // Snackbar.make(getView(), getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(getView(), getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
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
