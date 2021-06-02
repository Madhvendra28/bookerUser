package com.bookkr.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.ImageProcessing;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MoneyRequestCreateActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText money_request_edittext_txnid, money_request_edittext_amount;
    private ImageView money_request_imageview_transaction_image_close, money_request_imageview_transaction_image;

    private ProgressDialog progress;
    private final String TAG = "MoneyRequestCreate";
    private int requestFor = -1;

    private File root;
    private File mypath;
    private File imageDoc;
    private ImageView captureImageForImageView, captureImageForCloseImageView;
    private boolean transactionAdded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_request_create);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            money_request_edittext_txnid = findViewById(R.id.money_request_edittext_txnid);
            money_request_edittext_amount = findViewById(R.id.money_request_edittext_amount);

            money_request_imageview_transaction_image = findViewById(R.id.money_request_imageview_transaction_image);
            money_request_imageview_transaction_image_close = findViewById(R.id.money_request_imageview_transaction_image_close);

            root = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (!root.exists()) {
                root.mkdirs();
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void captureImage(View view) {
        try {
            int id = view.getId();
            String imageName = null, title = "";
            String s = Calendar.getInstance().getTimeInMillis() + "";
            mypath = null;

            captureImageForImageView = money_request_imageview_transaction_image;
            captureImageForCloseImageView = money_request_imageview_transaction_image_close;
            imageName = s + "_2.jpg";
            imageDoc = new File(root, imageName);
            mypath = imageDoc;
            title = getString(R.string.transaction_image);
            CropImage.startPickImageActivity(this);

        } catch (Exception e) {
            mypath = null;
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void clearImage(View view) {
        try {
            if (imageDoc != null) {
                AppUtils.removeImageFromStorage(this, imageDoc.toString());
                imageDoc = null;
            }
            money_request_imageview_transaction_image.setImageResource(R.drawable.ic_arrow_up_gray);
            money_request_imageview_transaction_image_close.setVisibility(View.GONE);

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void createMoneyRequest(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            AppUtils.hideKeyboard(this);
            money_request_edittext_txnid.setError(null);
            money_request_edittext_amount.setError(null);

            String txnId = money_request_edittext_txnid.getText() + "";
            String amount = money_request_edittext_amount.getText().toString();

            if (txnId.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_transaction_id), Snackbar.LENGTH_SHORT).show();
                money_request_edittext_txnid.setError(getString(R.string.error_empty_transaction_id));
                money_request_edittext_txnid.requestFocus();
                return;
            }

            if (amount.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_amount), Snackbar.LENGTH_SHORT).show();
                money_request_edittext_amount.setError(getString(R.string.error_empty_amount));
                money_request_edittext_amount.requestFocus();
                return;
            }

            if (imageDoc != null) {
                if (!imageDoc.exists()) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_attach_transaction_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_attach_transaction_image), Snackbar.LENGTH_LONG).show();
                return;
            }

            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_sending_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getAppURL() + AppURL.getMoneyRequest());
            parameter.setParam(AppURLParams.amount, amount + "");
            parameter.setParam(AppURLParams.transaction_id, txnId + "");

            if (imageDoc != null && imageDoc.exists()) {
                String newPath = ImageProcessing.compressImage(this, imageDoc.toString());
                parameter.setParam(AppURLParams.image, newPath);
                Log.d(TAG, "Old Path =>" + imageDoc.toString() + "||New Path =>" + newPath);

            } else {
                if (progress != null) {
                    progress.dismiss();
                }
                Snackbar.make(coordinatorLayout, getString(R.string.error_attach_transaction_image), Snackbar.LENGTH_LONG).show();
                return;
            }

            new UploadFileHTTPAsync().execute(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class UploadFileHTTPAsync extends AsyncTask<RequestParameter, Void, String> {

        private HttpClient mHttpClient;

        @Override
        protected String doInBackground(RequestParameter... reqParams) {

            try {
                RequestParameter parameter = reqParams[0];
                Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpParams params = new BasicHttpParams();
                params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                mHttpClient = new DefaultHttpClient(params);
                HttpPost httppost = new HttpPost(parameter.getUri());

                final String token = ShPrefUserDetails.getToken(MoneyRequestCreateActivity.this);
                httppost.addHeader(AppURLParams.Authorization, token);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart(AppURLParams.amount, new StringBody(parameter.getParam(AppURLParams.amount)));
                multipartEntity.addPart(AppURLParams.transaction_id, new StringBody(parameter.getParam(AppURLParams.transaction_id)));

                multipartEntity.addPart(AppURLParams.image, new FileBody(new File(parameter.getParam(AppURLParams.image))));
                Log.d(TAG, AppURLParams.image + "  ----  " + parameter.getParam(AppURLParams.image));

                httppost.setEntity(multipartEntity);
                HttpResponse response = mHttpClient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                String responseString = EntityUtils.toString(r_entity);
                Log.d(TAG, "Image Uploading Server Response\t" + responseString);

                return responseString;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                if (progress != null) {
                    progress.dismiss();
                }
                Log.d(TAG, "server response => " + response);

                if (response != null) {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.success)) {
                        processResponse(jsonObject);

                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();

                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                if (progress != null) {
                    progress.dismiss();
                }
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            transactionAdded = true;
            redirectUserProfile();

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void redirectUserProfile() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ack_money_request_added);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri profileselectedUri = null;
            Uri documentselectedUri = null;

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    captureImageForImageView.setImageURI(imageUri);
                    captureImageForCloseImageView.setVisibility(View.VISIBLE);
                    captureImageForCloseImageView.bringToFront();

                    documentselectedUri = imageUri;
                    imageDoc = new File(documentselectedUri.getPath());
                } else {
                    startCropImageActivity(imageUri);
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    captureImageForImageView.setImageURI(result.getUri());
                    captureImageForCloseImageView.setVisibility(View.VISIBLE);
                    captureImageForCloseImageView.bringToFront();

                    documentselectedUri = result.getUri();
                    imageDoc = new File(documentselectedUri.getPath());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    public void onBackPressed() {
        if (transactionAdded) {
            setResult(302);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
