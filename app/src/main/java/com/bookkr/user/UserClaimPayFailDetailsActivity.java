package com.bookkr.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.adapter.PayFailModalVariantRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.model.ModalVariant;
import com.model.PayFailData;
import com.model.SiteData;
import com.model.UserClaim;
import com.model.confirmclaim.MdModel;
import com.model.confirmclaim.Variant;
import com.model.payfailModel.Data;
import com.model.payfailModel.PayFailResponse;
import com.model.payfailModel.VariantDatum;
import com.preferences.SessionManager;
import com.preferences.ShPrefUserDetails;
import com.retrofit.APIClient;
import com.utils.AppURLParams;
import com.utils.AppUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserClaimPayFailDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private CoordinatorLayout coordinatorLayout;
    private TextView user_claim_textview_dealer_can_pay, user_claim_textview_left_slot, user_claim_textview_site_name, user_claim_textview_modal_name, user_claim_textview_total_amount;
    private EditText user_claim_edittext_login_id, user_claim_edittext_password, user_claim_edittext_other_number, user_claim_textview_time_left;
    MdModel ob;

    Data myrdata;
    private Spinner user_claim_spinner_otp_option, user_claim_spinner_nos_order;
    private RadioButton user_claim_radiobutton_my_number, user_claim_radiobutton_other_mobile, user_claim_radiobutton_cod_yes, user_claim_radiobutton_cod_no, user_claim_radiobutton_cod_idk;
    private RecyclerView user_claim_recycleview_variant;

    private ProgressDialog progress;
    private final String TAG = "PayFailDetails";

    private int requestFor = -1, clickedFor = 0;

    private String otpSendOn = "", nosOrders = "", otpOnWhatsapp = "", codAvailable = "";
    private boolean payFailAdded = false;
    SessionManager sessionManager;
    private Data userClaim;
    private SiteData selectedSiteData;
//    RecyclerView lin;
//    Button addModelbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_claim_pay_fail_details);

//        lin = findViewById(R.id.user_claim_recycleview_variant);
//        lin.removeAllViews();
//        addModelbutton=findViewById(R.id.addModelbutton);
//
//        addModelbutton.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
//
//                TextView txtName = new TextView(UserClaimPayFailDetailsActivity.this);
//                txtName.setId(20);
//                txtName.setText("new text");
//
//                lin.addView(txtName);
//
//            }
//        });

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            sessionManager=new SessionManager(this);
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            user_claim_textview_dealer_can_pay = findViewById(R.id.user_claim_textview_dealer_can_pay);
            user_claim_textview_left_slot = findViewById(R.id.user_claim_textview_left_slot);
            user_claim_textview_site_name = findViewById(R.id.user_claim_textview_site_name);
            user_claim_textview_modal_name = findViewById(R.id.user_claim_textview_modal_name);
            user_claim_textview_total_amount = findViewById(R.id.user_claim_textview_total_amount);

            user_claim_edittext_login_id = findViewById(R.id.user_claim_edittext_login_id);
            user_claim_edittext_password = findViewById(R.id.user_claim_edittext_password);
            user_claim_edittext_other_number = findViewById(R.id.user_claim_edittext_other_number);
            user_claim_textview_time_left = findViewById(R.id.user_claim_textview_time_left);

            user_claim_spinner_otp_option = findViewById(R.id.user_claim_spinner_otp_option);
            user_claim_spinner_nos_order = findViewById(R.id.user_claim_spinner_nos_order);
            user_claim_recycleview_variant = findViewById(R.id.user_claim_recycleview_variant);

            user_claim_radiobutton_my_number = findViewById(R.id.user_claim_radiobutton_my_number);
            user_claim_radiobutton_other_mobile = findViewById(R.id.user_claim_radiobutton_other_mobile);
            user_claim_radiobutton_cod_yes = findViewById(R.id.user_claim_radiobutton_cod_yes);
            user_claim_radiobutton_cod_no = findViewById(R.id.user_claim_radiobutton_cod_no);
            user_claim_radiobutton_cod_idk = findViewById(R.id.user_claim_radiobutton_cod_idk);

            user_claim_spinner_otp_option.setOnItemSelectedListener(this);
            user_claim_spinner_nos_order.setOnItemSelectedListener(this);

            user_claim_radiobutton_my_number.setOnCheckedChangeListener(this);
            user_claim_radiobutton_other_mobile.setOnCheckedChangeListener(this);
            user_claim_radiobutton_cod_yes.setOnCheckedChangeListener(this);
            user_claim_radiobutton_cod_no.setOnCheckedChangeListener(this);
            user_claim_radiobutton_cod_idk.setOnCheckedChangeListener(this);
            String userId = ShPrefUserDetails.getToken(this);
            Call<PayFailResponse> call = APIClient.getInterface().getPayfailData(userId,"21");

             call.enqueue(new Callback<PayFailResponse>() {
                 @Override
                 public void onResponse(Call<PayFailResponse> call, Response<PayFailResponse> response) {
                     Log.d("serajpayfaildata","response payfail status : "+response.isSuccessful());
                     if (response.isSuccessful()){
                         PayFailResponse payFailResponse=response.body();
                         userClaim = payFailResponse.getData();
                         if (userClaim == null) {
                             Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                             return;
                         } else {
                             myrdata=payFailResponse.getData()
                             setDataInViews(payFailResponse.getData());
                         }
                         Log.d("serajpayfaildata","response status : "+payFailResponse.getData().getSiteLogo());
                     }
                    // PayFailResponse payFailResponse=response.body();

                 }

                 @Override
                 public void onFailure(Call<PayFailResponse> call, Throwable t) {
                      Log.d("serajpayfaildata","error : "+t.getLocalizedMessage());
                 }
             });



           // userClaim = ob;//sessionManager.getModel();
//            selectedSiteData = UserClaimConfirmActivity.getActivity().getSelectedSiteData();



        } catch (Exception e) {
            Log.d("serajpayfaildata","pay fail error 1"+e.getLocalizedMessage());
            Snackbar.make(findViewById(R.id.coordinatorLayout), "md "+getString(R.string.error_try_later)+" "+e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInViews(Data mydata) {
        try {
            if (mydata != null) {
                Log.d("serajpayfaildata","Setting data error");
                user_claim_textview_dealer_can_pay.setText(""+mydata.getDealerCanPay());
                String totalQuantity= ShPrefUserDetails.getStringData("totalquantity",this);
                user_claim_textview_left_slot.setText(mydata.getLeftSlot());
                String sitename=ShPrefUserDetails.getStringData("sitename",this);
                user_claim_textview_site_name.setText(sitename);
                user_claim_textview_modal_name.setText(""+mydata.getModelData().get(0).getModelName());

                otpOnWhatsapp = AppURLParams.statusVal0;
                codAvailable = AppURLParams.statusVal1;
                 Log.d("mdpayfail","pay fail user claim not emplty");
                List<VariantDatum> modalVariantArrayList = mydata.getModelData().get(0).getVariantData();
                if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                    user_claim_recycleview_variant.setVisibility(View.VISIBLE);

                    user_claim_recycleview_variant.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    PayFailModalVariantRecyclerAdapter adapter = new PayFailModalVariantRecyclerAdapter(this, modalVariantArrayList);
                    user_claim_recycleview_variant.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    user_claim_recycleview_variant.setVisibility(View.GONE);
                }



            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_user_profile), Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Log.d("serajpayfaildata","pay fail error 2 " +e.getLocalizedMessage());
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later)+" "+e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void updateAmountToPaid() {
        try {
            int total = 0;
            List<Variant> variantDataArrayList = ob.getVariant();
            if (variantDataArrayList != null && variantDataArrayList.size() > 0) {
                for (int i = 0; i < variantDataArrayList.size(); i++) {
                    Variant siteVariantData = variantDataArrayList.get(i);
                   // Log.d(TAG, siteVariantData.getPayfail_quantity() + "|" + siteVariantData.getVariant_price());
                    if (!siteVariantData.getPayfailquantity().equals("")) {
                        total += Integer.parseInt(siteVariantData.getPayfailquantity()) * Integer.parseInt("5");
                    }
                }
            }

            user_claim_textview_total_amount.setText(getString(R.string.rupees) + " " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            int id = adapterView.getId();
            if (id == R.id.user_claim_spinner_otp_option) {
                otpSendOn = user_claim_spinner_otp_option.getSelectedItem() + "";

            } else if (id == R.id.user_claim_spinner_nos_order) {
                nosOrders = user_claim_spinner_nos_order.getSelectedItem() + "";

            }
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (isChecked) {
                int id = buttonView.getId();
                switch (id) {
                    case R.id.user_claim_radiobutton_my_number:
                        otpOnWhatsapp = AppURLParams.statusVal0;
                        user_claim_edittext_other_number.setText("");
                        user_claim_edittext_other_number.setVisibility(View.GONE);
                        break;

                    case R.id.user_claim_radiobutton_other_mobile:
                        otpOnWhatsapp = AppURLParams.statusVal1;
                        user_claim_edittext_other_number.setText("");
                        user_claim_edittext_other_number.setVisibility(View.VISIBLE);
                        break;

                    case R.id.user_claim_radiobutton_cod_yes:
                        codAvailable = AppURLParams.statusVal1;
                        break;

                    case R.id.user_claim_radiobutton_cod_no:
                        codAvailable = AppURLParams.statusVal2;
                        break;

                    case R.id.user_claim_radiobutton_cod_idk:
                        codAvailable = AppURLParams.statusVal3;
                        break;
                }
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void submitPayFailData(View view) {
        try {
            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
                return;
            }

            AppUtils.hideKeyboard(this);
            user_claim_edittext_login_id.setError(null);
            user_claim_edittext_password.setError(null);
            user_claim_edittext_other_number.setError(null);
            user_claim_textview_time_left.setError(null);

            String loginId = user_claim_edittext_login_id.getText() + "";
            String password = user_claim_edittext_password.getText().toString();
            String otherNumber = user_claim_edittext_other_number.getText() + "";
            String timeMins = user_claim_textview_time_left.getText() + "";

            if (loginId.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_login_id), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_login_id.setError(getString(R.string.error_empty_login_id));
                user_claim_edittext_login_id.requestFocus();
                return;
            }

            if (password.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_password), Snackbar.LENGTH_SHORT).show();
                user_claim_edittext_password.setError(getString(R.string.error_empty_password));
                user_claim_edittext_password.requestFocus();
                return;
            }

            if (otpSendOn.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_otp_method), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (otpOnWhatsapp.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_whatsapp_otp_method), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (otpOnWhatsapp.equals(AppURLParams.statusVal1)) {
                if (otherNumber.equals("")) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_empty_whatsapp_number), Snackbar.LENGTH_LONG).show();
                    return;
                }
            } else {
                otherNumber = ShPrefUserDetails.getWhatsapp_no(this);
            }

            if (nosOrders.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_nos_order), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (timeMins.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_empty_time_left_mins), Snackbar.LENGTH_LONG).show();
                return;
            }

            if (codAvailable.equals("")) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_select_cod_available), Snackbar.LENGTH_SHORT).show();
                return;
            }

            List<VariantDatum> siteVariantDataArrayList = mydata;
            int totalPayFailQuantity = 0;
            if (siteVariantDataArrayList != null && siteVariantDataArrayList.size() > 0) {
                for (int i = 0; i < siteVariantDataArrayList.size(); i++) {
                    Variant siteVariantData = siteVariantDataArrayList.get(i);
                    if (siteVariantData.getPayfailquantity().equals("")) {
                        Snackbar.make(coordinatorLayout, getString(R.string.error_empty_payfail_quantity), Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    int qty = Integer.parseInt(siteVariantData.getPayfailquantity());
                    totalPayFailQuantity += qty;
                }

                if (totalPayFailQuantity == 0) {
                    Snackbar.make(coordinatorLayout, getString(R.string.error_empty_payfail_quantity), Snackbar.LENGTH_SHORT).show();
                    return;
                }

            } else {
                Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
                return;
            }

            int total = 0;
            List<Variant> variantDataArrayList = ob.getVariant();
            if (variantDataArrayList != null && variantDataArrayList.size() > 0) {
                for (int i = 0; i < variantDataArrayList.size(); i++) {
                    Variant siteVariantData = variantDataArrayList.get(i);
                    Log.d(TAG, siteVariantData.getPayfailquantity() + "|" + "5");
                    if (!siteVariantData.getPayfailquantity().equals("")) {
                        total += Integer.parseInt(siteVariantData.getPayfailquantity()) * Integer.parseInt("5");
                    }
                }
            }

            PayFailData payFailData = null;
            if (selectedSiteData.getPayFailData() != null) {
                payFailData = selectedSiteData.getPayFailData();
            } else {
                payFailData = new PayFailData();
            }

            payFailData.setUsername(loginId);
            payFailData.setPassword(password);
            payFailData.setOtp_send_on(otpSendOn);
            payFailData.setWhatsapp_no(otherNumber);
            payFailData.setNo_of_orders(nosOrders);
            payFailData.setTime_left(timeMins);
            payFailData.setIs_cod_available(codAvailable);
            payFailData.setTotal_amount(total + "");
            selectedSiteData.setPayFailData(payFailData);

            payFailAdded = true;
            onBackPressed();

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (payFailAdded) {
            setResult(81);
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

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }
}
