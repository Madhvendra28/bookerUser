package com.bookkr.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WalletPreviewDetailActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener, AdapterView.OnItemSelectedListener{

    Toolbar mToolbarView;
    SearchableSpinner spinnerWallet;
    RadioButton rbtnSelf, rbtnOther;
    TextInputLayout textBankingName, textPhoneNo, textUpiID;
    EditText edtBankingName, edtPhoneNo, edtUpiID;
    ImageView imgQRCode, imgAddUPId;
    Button btnAddWallet;
    private int requestFor = 1;
    LinearLayout baseLayout, layoutMultiUPI;
    TextView tvIdEndWith;
    private LayoutInflater inflater;

    private ProgressDialog progress;
    private final String TAG = "Address Generator Add";
    List<Wallet> wallets;

    int accountType = 1;
    private List<View> mLayoutList = new ArrayList<>();

    AppCompatActivity activity;
    private File root;
    private File imageQRCode;
    private File mypath;
    MaterialCardView cardQrCode;

    boolean updatingImage = false;

    boolean isDataUpdatedSuccessFully = false;

    String id ,wallet_id;
    EditText edtUpiIDAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_preview_detail);
        activity = this;
        mToolbarView = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("wallet_id");

        spinnerWallet = findViewById(R.id.spinnerWallet);
        rbtnSelf = findViewById(R.id.rbtnSelf);
        rbtnOther = findViewById(R.id.rbtnOther);
        textBankingName = findViewById(R.id.textBankingName);
        textPhoneNo = findViewById(R.id.textPhoneNo);
        textUpiID = findViewById(R.id.textUpiID);
        edtBankingName = findViewById(R.id.edtBankingName);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtUpiID = findViewById(R.id.edtUpiID);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnAddWallet = findViewById(R.id.btnAddWallet);
        baseLayout = findViewById(R.id.baseLayout);
        tvIdEndWith = findViewById(R.id.tvIdEndWith);
        layoutMultiUPI = findViewById(R.id.layoutMultiUPI);
        imgAddUPId = findViewById(R.id.imgAddUPId);
        cardQrCode = findViewById(R.id.cardQrCode);
        spinnerWallet.setTitle("Select Wallet");
        spinnerWallet.setOnItemSelectedListener(this);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        edtBankingName.setText(ShPrefUserDetails.getName(activity));
        edtBankingName.setEnabled(false);
        getWalletList();

        rbtnSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountType = 1;
                edtBankingName.setText(ShPrefUserDetails.getName(activity));
                edtBankingName.setEnabled(false);
            }
        });

        rbtnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountType = 2;
                edtBankingName.setText("");
                edtBankingName.setEnabled(true);
            }
        });

        imgAddUPId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreUPId();
            }
        });

        cardQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        root = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!root.exists()) {
            root.mkdirs();
        }

        getBankDetail();


    }

    private void getBankDetail() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                // Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            requestFor = 1;
         /*   progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();*/

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getUserSingleWallet() + "/" + id + "/1");

            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());

            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri() + "?" + parameter.getEncodedParams(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (progress != null) {
                                    progress.dismiss();
                                }
                                Log.d(TAG, "server response => " + response);

                                if (response != null) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.success)) {
                                        try {

                                            JSONObject jsonData = jsonObject.getJSONObject("data");

                                            if (jsonData.length() != 0) {
                                                wallet_id = jsonData.isNull("wallet_id") ? "" : jsonData.getString("wallet_id");
                                                String wallet_name_id = jsonData.isNull("wallet_name_id") ? "" : jsonData.getString("wallet_name_id");
                                                String wallet_name = jsonData.isNull("wallet_name") ? "" : jsonData.getString("wallet_name");
                                                String add_type = jsonData.isNull("add_type") ? "" : jsonData.getString("add_type");
                                                String banking_name = jsonData.isNull("banking_name") ? "" : jsonData.getString("banking_name");
                                                String phone_no = jsonData.isNull("phone_no") ? "" : jsonData.getString("phone_no");
                                                String upi_id = jsonData.isNull("upi_id") ? "" : jsonData.getString("upi_id");
                                                String qr_code_image = jsonData.isNull("qr_code_image") ? "" : jsonData.getString("qr_code_image");
                                                String qr_code_full_image = jsonData.isNull("qr_code_full_image") ? "" : jsonData.getString("qr_code_full_image");

                                                edtBankingName.setText("" + banking_name);
                                                edtPhoneNo.setText("" + phone_no);

                                                if (add_type.equalsIgnoreCase("1")) {
                                                    rbtnSelf.setChecked(true);
                                                    rbtnOther.setChecked(false);
                                                } else if (add_type.equalsIgnoreCase("2")) {
                                                    rbtnSelf.setChecked(false);
                                                    rbtnOther.setChecked(true);
                                                }

                                                int positionItem = 0;
                                                for (int i = 0; i < wallets.size(); i++) {
                                                    if (wallets.get(i).getId().equalsIgnoreCase(wallet_name_id)) {
                                                        positionItem = i;
                                                    }
                                                }
                                                spinnerWallet.setSelection(positionItem + 1);

                                                String[] arrSplit = upi_id.split(",");
                                                for (int i = 0; i < arrSplit.length; i++) {
                                                    if((i==0)){
                                                        edtUpiID.setText(arrSplit[i]);
                                                        edtUpiID.setEnabled(false);
                                                    }else{
                                                        addMoreUPId();
                                                        edtUpiIDAdd.setText(arrSplit[i]);
                                                        edtUpiIDAdd.setEnabled(false);

                                                    }
                                                }

                                                try {
                                                    Picasso.get().load(qr_code_full_image.replace("https", "http")).into(imgQRCode);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                      /*  Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();*/

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                       /* Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();*/
                                    }
                                } else {
                                    /*     giveErrorOnError();*/
                                }

                            } catch (Exception e) {
//                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
//                    giveErrorOnError();
                    volleyError.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        Map<String, String> headers = super.getHeaders();
                        if (headers != null) {
                            if (headers.size() == 0) {
                                headers = new HashMap<>();
                            }
                        } else {
                            headers = new HashMap<>();
                        }

                        headers.put(AppURLParams.Authorization, token + "");
                        return headers;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return super.getHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(this, request);

        } catch (Exception e) {
//            giveErrorOnError();
            e.printStackTrace();
        }
    }

    public void captureImage() {
        try {
            String imageName = null, title = "";
            String s = Calendar.getInstance().getTimeInMillis() + "";
            mypath = null;

            imageName = s + "_2.jpg";
            imageQRCode = new File(root, imageName);
            mypath = imageQRCode;
            title = getString(R.string.profile_image);

            CropImage.startPickImageActivity(this);

        } catch (Exception e) {
            mypath = null;
            //Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Uri profileselectedUri = null;

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    imgQRCode.setImageURI(imageUri);
                    profileselectedUri = imageUri;
                    imageQRCode = new File(profileselectedUri.getPath());
                    updatingImage = true;
//                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {
                    startCropImageActivity(imageUri);
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    imgQRCode.setImageURI(result.getUri());

                    profileselectedUri = result.getUri();
                    imageQRCode = new File(profileselectedUri.getPath());
                    updatingImage = true;
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                }
            }

        } catch (Exception e) {
            // Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //if (view.getId() == R.id.spinnerWallet) {
        edtUpiID.setText(edtUpiID.getText().toString().split("@")[0] + wallets.get(i).getId_end());
        for (int j = 0; j < mLayoutList.size(); j++) {
            EditText edtUpiID = mLayoutList.get(j).findViewById(R.id.edtUpiID);
            edtUpiID.setText(edtUpiID.getText().toString().split("@")[0] + wallets.get(i).getId_end());
        }
        //  }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void addMoreUPId() {
        final View view = (View) inflater.inflate(R.layout.layout_add_upi_wallet, null);
        ImageView imgDelete = view.findViewById(R.id.imgDelete);
       // edtUpiIDAdd.setEnabled(false);
        edtUpiIDAdd = view.findViewById(R.id.edtUpiID);
        edtUpiIDAdd.setText(edtUpiIDAdd.getText().toString().split("@")[0] + wallets.get(spinnerWallet.getSelectedItemPosition()).getId_end());
        edtUpiID.setEnabled(false);

        imgDelete.setEnabled(false);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMultiUPI.removeView(view);
                mLayoutList.remove(view);
            }
        });

        mLayoutList.add(view);
        int tagIndex = mLayoutList.size() - 1;
        imgDelete.setTag(tagIndex);
        layoutMultiUPI.addView(view);
    }

    class Wallet {
        private String id;
        private String id_end;
        private String image;
        private String bank_name;

        public Wallet(String id, String id_end, String image, String bank_name) {
            this.id = id;
            this.id_end = id_end;
            this.image = image;
            this.bank_name = bank_name;
        }

        public Wallet() {
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId_end() {
            return id_end;
        }

        public void setId_end(String id_end) {
            this.id_end = id_end;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    private void getWalletList() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            if (wallets != null) {
                return;
            }

            requestFor = 1;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getBankAndWalletList());
            getDataListData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void addWallet(View view) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                ConnectionManager.createDialog(this);
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(baseLayout, getString(R.string.error_user_id_not_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            AppUtils.hideKeyboard(this);

            textBankingName.setError(null);
            textPhoneNo.setError(null);
            textUpiID.setError(null);

            String bank_name = edtBankingName.getText() + "";
            String phone_no = edtPhoneNo.getText().toString();
            String upi_id = edtUpiID.getText().toString();
            String add_type = "" + accountType;
            String type = "1";
            String wallet_name_id = wallets.get(spinnerWallet.getSelectedItemPosition()).getId();

            if (spinnerWallet.getSelectedItemPosition() == 0) {
                Snackbar.make(baseLayout, "Select Wallet", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (bank_name.equals("")) {
                textBankingName.setError(getString(R.string.error_bank_name_required));
                edtBankingName.requestFocus();
                Snackbar.make(baseLayout, getString(R.string.error_bank_name_required), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (phone_no.equalsIgnoreCase("") && edtUpiID.getText().toString().split("@")[0].equalsIgnoreCase("")) {
                Snackbar.make(baseLayout, "At least one from Phone No or UPI ID is Required", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (edtUpiID.getText().toString().split("@")[0].equals("")) {
                upi_id = "";
            }

          /*  if (phone_no.equals("") || edtUpiID.getText().toString().equals("")) {
                textPhoneNo.setError("Phone No is Required");
                edtPhoneNo.requestFocus();
                Snackbar.make(baseLayout, "Phone No is Required", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (edtUpiID.getText().toString().equals("")) {
                textUpiID.setError("UPI ID is Required");
                edtUpiID.requestFocus();
                Snackbar.make(baseLayout, "UPI ID is Required", Snackbar.LENGTH_LONG).show();
                return;
            }
*/
            for (int i = 0; i < mLayoutList.size(); i++) {
                TextInputLayout textUpiID1 = mLayoutList.get(i).findViewById(R.id.textUpiID);
                EditText edtUpiID1 = mLayoutList.get(i).findViewById(R.id.edtUpiID);
                // TextView tvIdEndWith1 = mLayoutList.get(i).findViewById(R.id.tvIdEndWith);
                textUpiID1.setError(null);
                if (edtUpiID1.getText().toString().split("@")[0].equals("") || edtUpiID1.getText().toString().equals("")) {
                    textUpiID1.setError("UPI ID is Required");
                    edtUpiID1.requestFocus();
                    Snackbar.make(baseLayout, "UPI ID is Required", Snackbar.LENGTH_LONG).show();
                    return;
                }

                upi_id += "," + edtUpiID1.getText().toString();
            }


          /*  if (updatingImage) {
                if (imageQRCode != null) {
                    if (!imageQRCode.exists()) {
                        //Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    //Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                    return;
                }
            } else {
                Toast.makeText(activity, "QR Image Required", Toast.LENGTH_SHORT).show();
                return;
            }*/


            requestFor = 2;
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.progress_getting_data));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            RequestParameter parameter = new RequestParameter();
            parameter.setUri(AppURL.getUserAddWallet());
            parameter.setParam("type", type);
            parameter.setParam("add_type", add_type);
            parameter.setParam("banking_name", bank_name);
            parameter.setParam("phone_no", phone_no);
            parameter.setParam("wallet_name_id", wallet_name_id);
            parameter.setParam("upi_id", upi_id);
            parameter.setParam("wallet_id", wallet_id);

            if (updatingImage) {
                if (imageQRCode != null && imageQRCode.exists()) {
                    String newPath = ImageProcessing.compressImage(this, imageQRCode.toString());
                    parameter.setParam(AppURLParams.qr_code_image, newPath);
                    Log.d(TAG, "Old Path =>" + imageQRCode.toString() + "||New Path =>" + newPath);

                } else {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    Toast.makeText(activity, "Error to attach QR Image, Try again.", Toast.LENGTH_SHORT).show();
                    // Snackbar.make(coordinatorLayout, getString(R.string.error_attach_profile_image), Snackbar.LENGTH_LONG).show();
                    return;
                }

            } else {
                parameter.setParam(AppURLParams.qr_code_image, "");
            }

            new UploadFileHTTPAsync().execute(parameter);
            // addWalletDataWithImage(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
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

                final String token = ShPrefUserDetails.getToken(WalletPreviewDetailActivity.this);
                httppost.addHeader(AppURLParams.Authorization, token);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart("type", new StringBody(parameter.getParam("type")));
                multipartEntity.addPart("add_type", new StringBody(parameter.getParam("add_type")));
                multipartEntity.addPart("banking_name", new StringBody(parameter.getParam("banking_name")));
                multipartEntity.addPart("phone_no", new StringBody(parameter.getParam("phone_no")));
                multipartEntity.addPart("wallet_name_id", new StringBody(parameter.getParam("wallet_name_id")));
                multipartEntity.addPart("upi_id", new StringBody(parameter.getParam("upi_id")));
                multipartEntity.addPart("wallet_id", new StringBody(parameter.getParam("wallet_id")));
                if (!parameter.getParam(AppURLParams.qr_code_image).equals("")) {
                    multipartEntity.addPart(AppURLParams.qr_code_image, new FileBody(new File(parameter.getParam(AppURLParams.qr_code_image))));
                    Log.d(TAG, AppURLParams.qr_code_image + "  ----  " + parameter.getParam(AppURLParams.qr_code_image));
                } else {
                    multipartEntity.addPart(AppURLParams.qr_code_image, new StringBody(parameter.getParam(AppURLParams.qr_code_image)));
                }
               /* multipartEntity.addPart(AppURLParams.qr_code_image, new FileBody(new File(parameter.getParam(AppURLParams.qr_code_image))));
                Log.d(TAG, AppURLParams.qr_code_image + "  ----  " + parameter.getParam(AppURLParams.qr_code_image));
*/
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
                    } else {
                        Toast.makeText(activity, jsonObject.getString(AppURLParams.message), Toast.LENGTH_SHORT).show();
                        // Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
                    //  Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                if (progress != null) {
                    progress.dismiss();
                }
                Toast.makeText(activity, getString(R.string.error_try_later), Toast.LENGTH_SHORT).show();
//                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
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

    private void getDataListData(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.GET, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        Map<String, String> headers = super.getHeaders();
                        if (headers != null) {
                            if (headers.size() == 0) {
                                headers = new HashMap<>();
                            }
                        } else {
                            headers = new HashMap<>();
                        }

                        headers.put(AppURLParams.Authorization, token + "");
                        return headers;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return super.getHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(this, request);
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addWalletData(RequestParameter parameter) {
        try {
            Log.d(TAG, parameter.getUri() + "?" + parameter.getEncodedParams());
            final LinkedHashMap<String, String> params = parameter.getParams();
            final String token = ShPrefUserDetails.getToken(this);
            StringRequest request = new StringRequest(Request.Method.POST, parameter.getUri(), this, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    try {
                        Map<String, String> headers = super.getHeaders();
                        if (headers != null) {
                            if (headers.size() == 0) {
                                headers = new HashMap<>();
                            }
                        } else {
                            headers = new HashMap<>();
                        }

                        headers.put(AppURLParams.Authorization, token + "");
                        return headers;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return super.getHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(this, request);
        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response) {
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
                    Snackbar.make(baseLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();

                } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                    Snackbar.make(baseLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            if (progress != null)
                progress.dismiss();
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            volleyError.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            switch (requestFor) {
                case 1:
                    try {
                        JSONObject data = jsonObject.getJSONObject(AppURLParams.data);
                        JSONArray jsonArray = data.getJSONArray("wallet");
                        wallets = new ArrayList<>();
                        wallets.add(new Wallet("0", "", "", "---Select Wallet---"));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Wallet wallet = new Wallet();
                            wallet.setId(obj.isNull("id") ? "" : obj.getString("id"));
                            wallet.setId_end(obj.isNull("id_end") ? "" : obj.getString("id_end"));
                            wallet.setBank_name(obj.isNull("bank_name") ? "" : obj.getString("bank_name"));
                            wallet.setImage(obj.isNull("image") ? "" : obj.getString("image"));
                            wallets.add(wallet);
                        }

                        if (wallets != null && wallets.size() > 0) {
                            setDataInSpinnerForStates();

                        } else {
                            Snackbar.make(baseLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                    } catch (Exception e) {
                        Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                        return;
                    }
                    break;

                case 2:
                    redirectUser();
            }

        } catch (Exception e) {
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInSpinnerForStates() {
        String[] walletArray = new String[wallets.size()];
        // walletArray[0] = "---Select Wallet---";
        for (int i = 0; i < wallets.size(); i++) {
            walletArray[i] = wallets.get(i).getBank_name();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, walletArray);
        spinnerWallet.setAdapter(adapter);
    }

    private void redirectUser() {
        try {
            isDataUpdatedSuccessFully = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ack_data_saved);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }*/
        switch (item.getItemId()) {
            case R.id.nav_delete:
                //startActivity(new Intent(this, About.class));
                return true;
            case R.id.nav_edit:
                startActivity(new Intent(this, WalletEditDetailActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDataUpdatedSuccessFully) {
            setResult(101);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }
}