package tieup.business_network.appmart.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import tieup.business_network.appmart.DBHelper;
import okhttp3.OkHttpClient;

import static tieup.business_network.appmart.Utils.ServerAddress.CATEGORIES_URL;



public class MyService extends Service{
    public static ArrayList<String> categoryArrayList;
    private DBHelper mydb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //    Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        getAllCategories();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

        super.onDestroy();
    }


    public void getAllCategories() {
        categoryArrayList = new ArrayList<String>();

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(CATEGORIES_URL)
                        .build();

                try {

                    okhttp3.Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        categoryArrayList.add(object.getString("category_name"));
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
                mydb=new DBHelper(MyService.this);
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    mydb.insertCategory(categoryArrayList.get(i),0);
                }
                MyService.super.onDestroy();
            }
        };

        task.execute();
    }

   /* public boolean checkConnectivity() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    public void showSnack(boolean isConnected) {
        if (isConnected) {
            // Snackbar.make(getView(), getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
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

    }*/


}