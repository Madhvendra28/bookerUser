package com.bookkr.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.ShipOrderHistoryModalVariantAdapter;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.fragment.UpdateShipOrderDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.model.ModalVariant;
import com.model.ShipOrderDetails;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextView ship_order_details_textview_requirement_id, ship_order_details_textview_dealer_name, ship_order_details_textview_site_name,
            ship_order_details_textview_order_date, ship_order_details_textview_expected_date, ship_order_details_textview_modal_name,
            ship_order_details_textview_payment_mode, ship_order_details_textview_name_on_order, ship_order_details_textview_order_value,
            ship_order_details_textview_courier, ship_order_details_textview_tracking_id, ship_order_details_textview_tracking_link,
            ship_order_details_textview_address, ship_order_details_textview_secret_note, ship_order_details_textview_status,
            ship_order_details_textview_courier_person_mobile, ship_order_details_textview_delivery_pin,
            ship_order_details_textview_otp_for_delivery, ship_order_details_textview_online_pay_available, ship_order_details_textview_comment;

    private ImageView ship_order_details_textview_address_image;
    private RecyclerView ship_order_details_recyclerview_variants;
    private LinearLayout ship_order_details_LL_address_image, ship_order_details_LL_ofd_details;

    private ProgressDialog progress;
    private final String TAG = "OrderHistory";
    private int requestFor = -1;

    private boolean shippingUpdated = false;
    private ShipOrderDetails shipOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            ship_order_details_textview_requirement_id = findViewById(R.id.ship_order_details_textview_requirement_id);
            ship_order_details_textview_dealer_name = findViewById(R.id.ship_order_details_textview_dealer_name);
            ship_order_details_textview_site_name = findViewById(R.id.ship_order_details_textview_site_name);
            ship_order_details_textview_order_date = findViewById(R.id.ship_order_details_textview_order_date);
            ship_order_details_textview_expected_date = findViewById(R.id.ship_order_details_textview_expected_date);
            ship_order_details_textview_modal_name = findViewById(R.id.ship_order_details_textview_modal_name);
            ship_order_details_textview_payment_mode = findViewById(R.id.ship_order_details_textview_payment_mode);
            ship_order_details_textview_name_on_order = findViewById(R.id.ship_order_details_textview_name_on_order);
            ship_order_details_textview_order_value = findViewById(R.id.ship_order_details_textview_order_value);
            ship_order_details_textview_courier = findViewById(R.id.ship_order_details_textview_courier);
            ship_order_details_textview_tracking_id = findViewById(R.id.ship_order_details_textview_tracking_id);
            ship_order_details_textview_tracking_link = findViewById(R.id.ship_order_details_textview_tracking_link);
            ship_order_details_textview_address = findViewById(R.id.ship_order_details_textview_address);
            ship_order_details_textview_secret_note = findViewById(R.id.ship_order_details_textview_secret_note);
            ship_order_details_textview_status = findViewById(R.id.ship_order_details_textview_status);

            ship_order_details_LL_ofd_details = findViewById(R.id.ship_order_details_LL_ofd_details);
            ship_order_details_textview_courier_person_mobile = findViewById(R.id.ship_order_details_textview_courier_person_mobile);
            ship_order_details_textview_delivery_pin = findViewById(R.id.ship_order_details_textview_delivery_pin);
            ship_order_details_textview_otp_for_delivery = findViewById(R.id.ship_order_details_textview_otp_for_delivery);
            ship_order_details_textview_online_pay_available = findViewById(R.id.ship_order_details_textview_online_pay_available);
            ship_order_details_textview_comment = findViewById(R.id.ship_order_details_textview_comment);

            ship_order_details_textview_address_image = findViewById(R.id.ship_order_details_textview_address_image);
            ship_order_details_recyclerview_variants = findViewById(R.id.ship_order_details_recyclerview_variants);
            ship_order_details_LL_address_image = findViewById(R.id.ship_order_details_LL_address_image);

            shipOrderDetails = OrderHistoryActivity.getActivity().getSelectedShipOrderDetails();
            if (shipOrderDetails == null) {
                giveErrorOnError();
                return;
            }

            if (shipOrderDetails != null) {
                setDataInViews();
            } else {
                giveErrorOnNoData();
                return;
            }

        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.coordinatorLayout), getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void submitUpdateShipping(View view) {
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
            UpdateShipOrderDialogFragment claimDialogFragment = new UpdateShipOrderDialogFragment(AppURLParams.statusVal2);
            claimDialogFragment.show(getSupportFragmentManager(), AppURLParams.shipOrderDialogFragment);

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void processResponse(JSONObject jsonObject) {
        try {
            Log.d(TAG, jsonObject + "");

            switch (requestFor) {
                case 2:
                    shippingUpdated = true;
                    redirectUserProfile();
                    break;
            }

        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setDataInViews() {
        try {
            if (shipOrderDetails != null) {
                ship_order_details_textview_requirement_id.setText("#" + shipOrderDetails.getRequirement_id());
                ship_order_details_textview_dealer_name.setText(shipOrderDetails.getDealer_name() + "");
                ship_order_details_textview_site_name.setText(shipOrderDetails.getSite_name() + "");
                ship_order_details_textview_order_date.setText(AppUtils.convertDateWithoutTime(shipOrderDetails.getCreate_date()));
                ship_order_details_textview_expected_date.setText(AppUtils.convertDateWithoutTime(shipOrderDetails.getExpected_date()));
                ship_order_details_textview_modal_name.setText(shipOrderDetails.getModel() + "");

                String payMode = shipOrderDetails.getPayment_mode();
                if (payMode.equals(AppURLParams.statusVal1)) {
                    ship_order_details_textview_payment_mode.setText(getString(R.string.cod));

                } else if (payMode.equals(AppURLParams.statusVal1)) {
                    ship_order_details_textview_payment_mode.setText(getString(R.string.pre_paid));

                } else if (payMode.equals(AppURLParams.statusVal1)) {
                    ship_order_details_textview_payment_mode.setText(getString(R.string.pay_fail));

                } else if (payMode.equals(AppURLParams.statusVal1)) {
                    ship_order_details_textview_payment_mode.setText(getString(R.string.otp_verified));
                }

                ship_order_details_textview_name_on_order.setText(shipOrderDetails.getName_on_order() + "");
                ship_order_details_textview_order_value.setText(getString(R.string.rupees) + " " + shipOrderDetails.getOrder_value());
                ship_order_details_textview_courier.setText(shipOrderDetails.getCourier() + "");
                ship_order_details_textview_tracking_id.setText(shipOrderDetails.getTracking_id() + "");
                ship_order_details_textview_tracking_link.setText(shipOrderDetails.getTracking_link() + "");
                ship_order_details_textview_address.setText(shipOrderDetails.getAddress() + "");
                ship_order_details_textview_secret_note.setText(shipOrderDetails.getSecret_note() + "");

                setDataModalVariantRecyclerAdapter();


                String status = shipOrderDetails.getStatus();
                if (status.equalsIgnoreCase(AppURLParams.statusVal0)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_shipped) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal1)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_reached_dc) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal2)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_ofd) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal3)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_undelivered) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal4)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_rejected) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal5)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_rto) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal6)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_delivered) + "");

                } else if (status.equalsIgnoreCase(AppURLParams.statusVal7)) {
                    ship_order_details_textview_status.setText(getString(R.string.status_donot_accept) + "");

                } /*else if (status.equalsIgnoreCase(AppURLParams.statusVal8)) {
//                    adapter_textview_status.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_button_disc_red));
//                    adapter_textview_status.setTextColor(activity.getResources().getColor(R.color.textColorRed));
                    ship_order_details_textview_status.setText(getString(R.string.status_missing_info) + "");

                }*/ else {
                    ship_order_details_textview_status.setText(getString(R.string.na) + "");
                }

                if (status.equalsIgnoreCase(AppURLParams.statusVal2)) {
                    ship_order_details_LL_ofd_details.setVisibility(View.VISIBLE);
                    ship_order_details_textview_courier_person_mobile.setText(shipOrderDetails.getCourier_boy_no() + "");
                    ship_order_details_textview_delivery_pin.setText(shipOrderDetails.getPin() + "");
                    ship_order_details_textview_otp_for_delivery.setText(shipOrderDetails.getOtp_for_delivery() + "");
                    ship_order_details_textview_comment.setText(shipOrderDetails.getComment() + "");

                    if (shipOrderDetails.getIs_online_pay().equals(AppURLParams.statusVal1)) {
                        ship_order_details_textview_online_pay_available.setText(getString(R.string.yes) + "");
                    } else {
                        ship_order_details_textview_online_pay_available.setText(getString(R.string.no) + "");
                    }

                } else {
                    ship_order_details_LL_ofd_details.setVisibility(View.GONE);
                }

                try {
                    Log.d(TAG, shipOrderDetails.getAddress_image() + "");
                    if (!shipOrderDetails.getAddress_image().equals("")) {
                        ImageRequest imageRequest = new ImageRequest(shipOrderDetails.getAddress_image() + "",
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            ship_order_details_LL_address_image.setVisibility(View.VISIBLE);
                                            ship_order_details_textview_address_image.setImageBitmap(bitmap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                AppURLParams.imageWidthMerchant, AppURLParams.imageHeightMerchant, null, null);
                        GetServerData.addRequestToQueue(this, imageRequest);
                    } else {
                        ship_order_details_LL_address_image.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataModalVariantRecyclerAdapter() {
        try {
            ship_order_details_recyclerview_variants.invalidate();
            ArrayList<ModalVariant> modalVariantArrayList = shipOrderDetails.getModalVariantArrayList();

            if (modalVariantArrayList != null && modalVariantArrayList.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < modalVariantArrayList.size(); i++) {
                    ModalVariant modalVariant = modalVariantArrayList.get(i);
                    list.add(modalVariant.getVariant_name());
                }

                ship_order_details_recyclerview_variants.setVisibility(View.VISIBLE);
                ShipOrderHistoryModalVariantAdapter adapter = new ShipOrderHistoryModalVariantAdapter(this, list, modalVariantArrayList);
                ship_order_details_recyclerview_variants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                ship_order_details_recyclerview_variants.setAdapter(adapter);

            } else {
                ship_order_details_recyclerview_variants.setVisibility(View.GONE);
                giveErrorOnNoData();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void redirectUserProfile() {
        try {
            shippingUpdated = true;
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

    private void giveErrorOnNoData() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (shippingUpdated) {
            setResult(51);
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
