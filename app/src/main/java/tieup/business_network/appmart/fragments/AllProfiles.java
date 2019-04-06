package tieup.business_network.appmart.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tieup.business_network.appmart.R;
import tieup.business_network.appmart.activities.ShowAll;
import tieup.business_network.appmart.adapters.ProfilesAdapter;
import tieup.business_network.appmart.models.ProfilesModel;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.Utils.ServerAddress.PROFILE_LIST_URL;

public class AllProfiles extends Fragment {
    RecyclerView recyclerView;
    String business_category;
    ProfilesAdapter adapter;
    List<ProfilesModel> profile_list;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_profiles_list, container, false);
        business_category =getArguments().getString("business_category");

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);

        profile_list = new ArrayList<>();
        ((ShowAll) getActivity())
                .setActionBarTitle("All Profiles");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

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
                    bundle.putString("user_name", profile_list.get(position).getUser_name());
                    bundle.putString("business_title", profile_list.get(position).getBusiness_title());
                    bundle.putString("business_category", profile_list.get(position).getBusiness_category());
                    bundle.putString("place", profile_list.get(position).getPlace());
                    bundle.putString("user_mobile_number", profile_list.get(position).getUser_mobile_number());
                    bundle.putString("user_whatsapp_number", profile_list.get(position).getUser_whatsapp_number());
                    bundle.putString("user_mail", profile_list.get(position).getUser_mail());
                    bundle.putString("user_website", profile_list.get(position).getUser_website());
                    bundle.putString("about_business", profile_list.get(position).getAbout_business());
                    bundle.putString("business_year", profile_list.get(position).getBusiness_year());
                    bundle.putString("user_profile", profile_list.get(position).getUser_profile());
                    Fragment f = new ProfileDetails();
                    if (f != null) {
                        f.setArguments(bundle);
                        FragmentManager FM = getFragmentManager();
                        FM.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.mainContainer, f)
                                .addToBackStack(String.valueOf(FM))
                                .commit();
                    }
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

        return view;
    }

    @Override
    public void onDetach() {
        ((ShowAll) getActivity())
                .setActionBarTitle("All Categories");
        super.onDetach();
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
                RequestBody requestBody = new FormBody.Builder()
                        .add("business_category",business_category)
                        .build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(PROFILE_LIST_URL)
                        .post(requestBody)
                        .build();

                try {

                    okhttp3.Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        ProfilesModel data = new ProfilesModel(
                                object.getString("user_name"),
                                object.getString("business_title"),
                                object.getString("business_category"),
                                object.getString("place"),
                                object.getString("user_mobile_number"),
                                object.getString("user_whatsapp_number"),
                                object.getString("user_mail"),
                                object.getString("user_website"),
                                object.getString("about_business"),
                                object.getString("business_year"),
                                object.getString("user_profile"));
                        profile_list.add(data);

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
                adapter = new ProfilesAdapter(getContext(), profile_list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        task.execute();

    }
}
