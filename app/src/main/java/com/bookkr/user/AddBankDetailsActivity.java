package com.bookkr.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.FindIFSCDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddBankDetailsActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener, AdapterView.OnItemSelectedListener {
    private final String TAG = "Add Bank Details";
    Toolbar mToolbarView;
    SearchableSpinner spinnerBank, spinnerAccountOf, spinnerAccountType;
    TextInputLayout textAccountHolderName, textAccountNo, textIFSCCode, textDepositLimit;
    EditText edtAccountHolderName, edtAccountNo, edtIFSCCode, edtDepositLimit, edtNote;
    RadioButton rbtnDepositAboveYes, rbtnDepositAboveNo, rbtnOtherDepositYes, rbtnOtherDepositNo;
    List<ModelBank> modelBanks;
    LinearLayout baseLayout;
    int is_above = 0;
    int is_other_deposit = 0;
    ArrayList<String> accountOfList;
    ArrayList<String> accountTypeList;
    int accountType = 1;
    boolean isDataUpdatedSuccessFully = false;
    AppCompatActivity activity;
    TextView tvFindIFSC, tvCheckIFSC;
    private int requestFor = 1;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_details);
        mToolbarView = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;
        spinnerBank = findViewById(R.id.spinnerBank);
        spinnerAccountOf = findViewById(R.id.spinnerAccountOf);
        spinnerAccountType = findViewById(R.id.spinnerAccountType);
        textAccountHolderName = findViewById(R.id.textAccountHolderName);
        textAccountNo = findViewById(R.id.textAccountNo);
        textIFSCCode = findViewById(R.id.textIFSCCode);
        textDepositLimit = findViewById(R.id.textDepositLimit);
        edtAccountHolderName = findViewById(R.id.edtAccountHolderName);
        edtAccountNo = findViewById(R.id.edtAccountNo);
        edtIFSCCode = findViewById(R.id.edtIFSCCode);
        edtDepositLimit = findViewById(R.id.edtDepositLimit);
        edtNote = findViewById(R.id.edtNote);
        rbtnDepositAboveYes = findViewById(R.id.rbtnDepositAboveYes);
        rbtnDepositAboveNo = findViewById(R.id.rbtnDepositAboveNo);
        rbtnOtherDepositYes = findViewById(R.id.rbtnOtherDepositYes);
        rbtnOtherDepositNo = findViewById(R.id.rbtnOtherDepositNo);
        baseLayout = findViewById(R.id.baseLayout);
        tvFindIFSC = findViewById(R.id.tvFindIFSC);
        tvCheckIFSC = findViewById(R.id.tvCheckIFSC);
        accountOfList = new ArrayList<>();
        accountOfList.add("---Select---");
        accountOfList.add("SELF");
        accountOfList.add("FAMILY");
        accountOfList.add("FRIENDS");
        accountOfList.add("GFS");
        accountOfList.add("OTHERS");

        accountTypeList = new ArrayList<>();
        accountTypeList.add("SAVING");
        accountTypeList.add("CURRENT");

        rbtnDepositAboveYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_above = 1;
            }
        });

        rbtnDepositAboveNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_above = 0;

            }
        });
        rbtnOtherDepositYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_other_deposit = 1;
            }
        });
        rbtnOtherDepositNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_other_deposit = 0;
            }
        });

        edtAccountNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtAccountNo.getRight() - edtAccountNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        AppUtils.pasteLink(activity, edtAccountNo);
                        return true;
                    }
                }
                return false;
            }
        });
        edtIFSCCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtIFSCCode.getRight() - edtIFSCCode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        AppUtils.pasteLink(activity, edtIFSCCode);
                        return true;
                    }
                }
                return false;
            }
        });
        ArrayAdapter accountOfAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountOfList);
        spinnerAccountOf.setAdapter(accountOfAdapter);

        spinnerAccountOf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerAccountOf.getSelectedItemPosition() == 1) {
                    edtAccountHolderName.setText(ShPrefUserDetails.getName(AddBankDetailsActivity.this));
                    edtAccountHolderName.setEnabled(false);
                } else {
                    edtAccountHolderName.setText("");
                    edtAccountHolderName.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spannable span = Spannable.Factory.getInstance().newSpannable("Find IFSC Code");
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                try {
                    new FindIFSCDialog(activity).show();
                } catch (Exception e) {
                    Toast.makeText(activity, "Unable to open URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, 0, "Find IFSC Code".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvFindIFSC.setText(span);
        tvFindIFSC.setMovementMethod(LinkMovementMethod.getInstance());

        Spannable span1 = Spannable.Factory.getInstance().newSpannable("Check IFSC Code");
        span1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://ifsc.bankifsccode.com/"));
                    activity.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(activity, "Unable to open URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, 0, "Check IFSC Code".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCheckIFSC.setText(span1);
        tvCheckIFSC.setMovementMethod(LinkMovementMethod.getInstance());

        ArrayAdapter accountTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypeList);
        spinnerAccountType.setAdapter(accountTypeAdapter);
        getBankList();

    }

    public void addBank(View view) {
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

            textAccountHolderName.setError(null);
            textAccountNo.setError(null);
            textIFSCCode.setError(null);
            textDepositLimit.setError(null);

            String account_holder_name = edtAccountHolderName.getText() + "";
            String account_number = edtAccountNo.getText().toString();
            String ifsc_code = edtIFSCCode.getText().toString();
            String account_type = accountTypeList.get(spinnerAccountType.getSelectedItemPosition());
            String type = "2";
            String note = edtNote.getText().toString().trim();
            String deposit_limit = edtDepositLimit.getText().toString().trim();

            if (spinnerBank.getSelectedItemPosition() == 0) {
                Snackbar.make(baseLayout, "Select Bank", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (spinnerAccountOf.getSelectedItemPosition() == 0) {
                Snackbar.make(baseLayout, "Select Account Of", Snackbar.LENGTH_LONG).show();
                return;
            }
            String add_type = "" + (spinnerAccountOf.getSelectedItemPosition());
            String bank_name_id = modelBanks.get(spinnerBank.getSelectedItemPosition() - 1).getId();

            if (account_holder_name.equals("")) {
                textAccountHolderName.setError(getString(R.string.error_bank_name_required));
                edtAccountHolderName.requestFocus();
                Snackbar.make(baseLayout, getString(R.string.error_bank_name_required), Snackbar.LENGTH_LONG).show();
                return;
            }
            if (account_number.equals("")) {
                textAccountNo.setError("Account No is Required");
                edtAccountNo.requestFocus();
                Snackbar.make(baseLayout, "Account No is Required", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (ifsc_code.equals("")) {
                textIFSCCode.setError("IFSC code is Required");
                edtIFSCCode.requestFocus();
                Snackbar.make(baseLayout, "IFSC code is Required", Snackbar.LENGTH_LONG).show();
                return;
            }

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
            parameter.setParam("account_holder_name", account_holder_name);
            parameter.setParam("account_number", account_number);
            parameter.setParam("account_type", account_type);
            parameter.setParam("ifsc_code", ifsc_code);
            parameter.setParam("bank_name_id", bank_name_id);
            parameter.setParam("note", note);
            parameter.setParam("deposit_limit", deposit_limit);
            parameter.setParam("is_above", String.valueOf(is_above));
            parameter.setParam("is_other_deposit", String.valueOf(is_other_deposit));
            addBankData(parameter);

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(baseLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getBankList() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            if (modelBanks != null) {
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
                        JSONArray jsonArray = data.getJSONArray("bank");
                        modelBanks = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ModelBank modelBank = new ModelBank();
                            modelBank.setId(obj.isNull("id") ? "" : obj.getString("id"));
                            modelBank.setBank_name(obj.isNull("bank_name") ? "" : obj.getString("bank_name"));
                            modelBank.setImage(obj.isNull("image") ? "" : obj.getString("image"));
                            modelBanks.add(modelBank);
                        }

                        if (modelBanks != null && modelBanks.size() > 0) {
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
        String[] walletArray = new String[modelBanks.size() + 1];
        walletArray[0] = "---Select---";
        for (int i = 0; i < modelBanks.size(); i++) {
            walletArray[i + 1] = modelBanks.get(i).getBank_name();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, walletArray);
        spinnerBank.setAdapter(adapter);
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
    public void onBackPressed() {
        if (isDataUpdatedSuccessFully) {
            setResult(102);
        }
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    private void addBankData(RequestParameter parameter) {
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

    class ModelBank {
        private String id;
        private String image;
        private String bank_name;

        public ModelBank(String id, String image, String bank_name) {
            this.id = id;
            this.image = image;
            this.bank_name = bank_name;
        }

        public ModelBank() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }
    }

}