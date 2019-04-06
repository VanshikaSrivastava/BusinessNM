package tieup.business_network.appmart.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tieup.business_network.appmart.ConnectivityReceiver;
import tieup.business_network.appmart.R;
import tieup.business_network.appmart.Validation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static tieup.business_network.appmart.Utils.ServerAddress.UPLOAD_POST_IMAGE_URL;

public class AddPost extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgPost;
    private Bitmap bitmap;
    private Uri filePath;
    String uploadImage;
    ProgressDialog loading;
    boolean success;
    RelativeLayout postLayout;
    Button btnPublish, btnCancel;
    EditText edtPostTitle, edtDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        imgPost = findViewById(R.id.imgPost);
        edtPostTitle = findViewById(R.id.edtPostTitle);
        edtDescription = findViewById(R.id.edtDescription);
        btnPublish = findViewById(R.id.btnPublish);
        btnCancel = findViewById(R.id.btnCancel);
        postLayout = findViewById(R.id.postLayout);


        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    if (checkConnectivity()) {
                        addPostRequest();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPost.this, Home.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                imgPost.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPostRequest() {

        loading = ProgressDialog.show(AddPost.this, "Uploading...", null, true, true);

        SharedPreferences SavedDetails = PreferenceManager.getDefaultSharedPreferences(AddPost.this);
        final String user_id = SavedDetails.getString("user_id", "");

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = new Date();

        final SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        final Date time = new Date();

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {


            @Override
            protected Void doInBackground(Integer... integers) {
                try {
                    uploadImage = getStringImage(bitmap);
                } catch (NullPointerException e) {
                    uploadImage = "";
                }
                OkHttpClient client = new OkHttpClient();

                @SuppressLint("WrongThread") RequestBody requestBody = new FormBody.Builder()
                        .add("user_id", user_id)
                        .add("post_title", edtPostTitle.getText().toString())
                        .add("post_description", edtDescription.getText().toString())
                        .add("post_image", uploadImage)
                        .add("post_datee", sdf.format(date))
                        .add("post_time", sdf1.format(time))
                        .build();

                Request request = new Request.Builder()
                        .url(UPLOAD_POST_IMAGE_URL)
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
            protected void onPostExecute(Void avoid) {
                loading.dismiss();
                Toast.makeText(AddPost.this, "Upload Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddPost.this,Home.class);
                startActivity(i);
            }
        };

        task.execute();
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(edtPostTitle,true)) ret = false;
        if (!Validation.hasText(edtDescription,true)) ret = false;

        return ret;
    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

            Snackbar.make(postLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
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
