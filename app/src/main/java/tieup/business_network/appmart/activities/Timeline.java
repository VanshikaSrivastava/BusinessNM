package tieup.business_network.appmart.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.adapters.TimelineAdapter;
import tieup.business_network.appmart.models.TimelineModel;
import okhttp3.OkHttpClient;

import static tieup.business_network.appmart.Utils.ServerAddress.POST_LIST_URL;

public class Timeline extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    RecyclerView recyclerView;
    TimelineAdapter adapter;
    private List<TimelineModel> post_list;
    ProgressBar progressBar;
    RelativeLayout timelineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_list);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar =  findViewById(R.id.progressBar);
        timelineLayout =  findViewById(R.id.timelineLayout);

        recyclerView.setHasFixedSize(true);
        checkConnectivity();

        post_list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(Timeline.this));


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(Timeline.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Bundle bundle = new Bundle();
                    bundle.putString("post_title", post_list.get(position).getPost_title());
                    bundle.putString("post_description", post_list.get(position).getPost_description());
                    bundle.putString("post_image", String.valueOf(post_list.get(position).getPost_image()));
                    bundle.putString("post_datee", String.valueOf(post_list.get(position).getPost_datee()));
                    bundle.putString("post_time", String.valueOf(post_list.get(position).getPost_time()));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }


        });

        loadBusinessProfiles();

    }
    public void checkConnectivity() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    public void showSnack(boolean isConnected) {
        if (isConnected) {
            Snackbar.make(timelineLayout, getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(timelineLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }).setActionTextColor(Color.RED)
                    .show();
        }
    }

    private void loadBusinessProfiles() {


        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progressBar.showContextMenu();
            }

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(POST_LIST_URL)
                        .build();

                try {

                    okhttp3.Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);


                        TimelineModel data = new TimelineModel(
                                object.getString("user_name"),
                                object.getString("post_title"),
                                object.getString("post_description"),
                                object.getString("post_image"),
                                object.getString("post_datee"),
                                object.getString("post_time"));
                        post_list.add(data);

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("End of content");
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                adapter = new TimelineAdapter(Timeline.this, post_list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        task.execute();

    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }

}
