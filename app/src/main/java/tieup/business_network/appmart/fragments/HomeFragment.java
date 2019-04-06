package tieup.business_network.appmart.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import tieup.business_network.appmart.DBHelper;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.activities.ShowAll;
import okhttp3.OkHttpClient;

import static tieup.business_network.appmart.Utils.ServerAddress.GET_SLIDER_URL;

public class HomeFragment extends Fragment {
    TextView txtshowAll, txtGetCategory;
    CarouselView carouselView, carouselView1;
    private ArrayList<String> image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtshowAll = view.findViewById(R.id.txtshowAll);
        txtGetCategory = view.findViewById(R.id.txtGetCategory);
        carouselView = view.findViewById(R.id.carouselView);
        carouselView1 = view.findViewById(R.id.carouselView1);

        loadImages();

        carouselView.setImageListener(imageListener);
        carouselView1.setImageListener(imageListener);

        txtshowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ShowAll.class);
                startActivity(i);
            }
        });



       // Toast.makeText(getContext(),array_list.size(), Toast.LENGTH_SHORT).show();

       /* String cat = rs.getString(rs.getColumnIndex(DBHelper.CATEGORIES_COLUMN_NAME));

        if (!rs.isClosed())  {
            rs.close();
        }

        txtGetCategory.setText(cat);
        txtGetCategory.setFocusable(false);
        txtGetCategory.setClickable(false);
*/

        return view;

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Picasso.with(getContext()).load(image.get(position)).placeholder(R.drawable.gradient).error(R.drawable.gradient).fit().centerCrop().into(imageView);

        }
    };
    ViewListener viewListener = new ViewListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

            carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

            return customView;
        }
    };

    private void loadImages() {

        image = new ArrayList<String>();

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {


            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(GET_SLIDER_URL)
                        .build();

                try {

                    okhttp3.Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        image.add(object.getString("image"));
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
                carouselView.setPageCount(image.size());
                carouselView1.setPageCount(image.size());

            }
        };

        task.execute();

    }

}

