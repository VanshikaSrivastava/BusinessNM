package tieup.business_network.appmart.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import tieup.business_network.appmart.BuildConfig;
import tieup.business_network.appmart.CircleTransform;
import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.fragments.HomeFragment;
import tieup.business_network.appmart.MyApplication;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.fragments.BusinessProfile;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.Utils.ServerAddress.UPLOAD_KEY;
import static tieup.business_network.appmart.Utils.ServerAddress.UPLOAD_PROFILE_URL;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    Fragment fragment;
    TextView txtUserName, txtUserMail, txtTimeline, txtHome;
    SharedPreferences savedDetails;
    ImageView imgProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    LinearLayout linelayout;

    String uploadImage,user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linelayout = findViewById(R.id.linelayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        txtUserName = headerLayout.findViewById(R.id.txtUserName);
        txtUserMail = headerLayout.findViewById(R.id.txtUserMail);
        imgProfile = headerLayout.findViewById(R.id.imgProfile);

        checkConnectivity();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        savedDetails = PreferenceManager.getDefaultSharedPreferences(Home.this);
        user_id = savedDetails.getString("user_id", "");
        txtUserName.setText(savedDetails.getString("user_name", ""));
        txtUserMail.setText(savedDetails.getString("user_emailid", ""));

        String myImageUrl = savedDetails.getString("user_profile", "");
        if (myImageUrl.isEmpty()) {
            imgProfile.setImageResource(R.drawable.ic_account_circle_white_64dp);
        } else {

            Picasso.with(this).invalidate(myImageUrl);
            Picasso.with(this).load(myImageUrl).networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).fit().centerCrop().error(R.drawable.gradient).into(imgProfile);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        txtTimeline = view.findViewById(R.id.txtTimeline);
        txtHome = view.findViewById(R.id.txtHome);
        txtTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Timeline.class);
                startActivity(i);
            }
        });

        fragment = new HomeFragment();
        loadFragment();

    }
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount()<=1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Confirm Exit");
            //  builder.setIcon(R.drawable.icon_exit);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business_profile) {
            fragment = new BusinessProfile();
            loadFragment();
        } else if (id == R.id.nav_post) {
            Intent intent = new Intent(Home.this, AddPost.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
            alertDialogBuilder.setMessage("Are you sure,You wanted to Logout");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences savedUser = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editer = savedUser.edit();
                            editer.clear();
                            editer.commit();
                            Intent i = new Intent(Home.this, Splash.class);
                            startActivity(i);
                            finish();
                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else if (id == R.id.nav_rateus) {

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        } else if (id == R.id.nav_share) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;


    }

    public void UploadImage() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Home.this, "Uploading...", null, true, true);
            }

            @Override
            protected Void doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();

                uploadImage = getStringImage(bitmap);

                RequestBody requestBody = new FormBody.Builder()
                        .add("user_id", user_id)
                        .add(UPLOAD_KEY, uploadImage)
                        .build();

                Request request = new Request.Builder()
                        .url(UPLOAD_PROFILE_URL)
                        .post(requestBody)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    JSONObject object = new JSONObject(response.body().string());

                    object.getBoolean("success");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return null;
            }


            @Override
            protected void onPostExecute(Void avoid) {
                loading.dismiss();
                Toast.makeText(Home.this, "Upload Successfully", Toast.LENGTH_LONG).show();
            }

        };

        task.execute();

    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                imgProfile.setImageBitmap(bitmap);

                UploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void loadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.secondContainer, fragment)
                .addToBackStack(String.valueOf(fm))
                .commit();

    }

    public void setActionBarTitle(String title) {
        txtHome.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityReceiver(this);
    }

    public void checkConnectivity() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    public void showSnack(boolean isConnected) {
        if (isConnected) {
         //   Snackbar.make(linelayout, getString(R.string.internet_connected), Snackbar.LENGTH_LONG).show();
        } else {

            Snackbar.make(linelayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
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
