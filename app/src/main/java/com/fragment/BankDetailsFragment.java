package com.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.BankDetailPreviewActivity;
import com.bookkr.user.R;
import com.bookkr.user.WalletPreviewDetailActivity;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDetailsFragment extends Fragment {
    RecyclerView bankList;
    LinearLayoutManager layoutManager;
    LinearLayout llSelf, llOther, llFamily, llFriends, llGFS;
    List<ModelBank> models;
    BankListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_bank_details, container, false);
        bankList = root.findViewById(R.id.bankList);
        llSelf = root.findViewById(R.id.llSelf);
        llOther = root.findViewById(R.id.llOthers);
        llFamily = root.findViewById(R.id.llFamily);
        llFriends = root.findViewById(R.id.llFriends);
        llGFS = root.findViewById(R.id.llGFS);

        llSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelBank modelBank = models.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("1")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, getActivity());
                bankList.setAdapter(adapter);

            }
        });

        llFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelBank modelBank = models.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("2")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, getActivity());
                bankList.setAdapter(adapter);

            }
        });


        llFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelBank modelBank = models.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("3")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, getActivity());
                bankList.setAdapter(adapter);

            }
        });

        llGFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelBank modelBank = models.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("4")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, getActivity());
                bankList.setAdapter(adapter);

            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOther.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelBank modelBank = models.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("5")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, getActivity());
                bankList.setAdapter(adapter);

            }
        });
        llSelf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        ;

        final String token = ShPrefUserDetails.getToken(getActivity());
        // Log
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        getBankDetail();
        return root;
    }


    class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {

        List<ModelBank> models;
        Context context;

        public BankListAdapter(List<ModelBank> models, Context context) {
            this.models = models;
            this.context = context;
        }

        @NonNull
        @Override
        public BankListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bank_details_item, parent, false);
            return new BankListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BankListAdapter.ViewHolder holder, final int position) {
            holder.tvName.setText("Name : " + models.get(position).getAccount_holder_name());
            holder.tvAccountNo.setText("Acc No : " + models.get(position).getAccount_number());
            holder.tvIFSC.setText("IFSC : " + models.get(position).getIfsc_code());
            if (models.get(position).getIs_other_deposit().equalsIgnoreCase("0")) {
                holder.tvDepositLimit.setText("Dp limit left : No ");
            } else {
                holder.tvDepositLimit.setText("Dp limit left : Yes ");
            }
            //holder.tvDepositLimit.setText("Dp limit left : " + models.get(position).getDeposit_limit());
            try {
                Picasso.get().load(models.get(position).getImage().replace("https", "http")).into(holder.imgBank);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BankDetailPreviewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("bank_id", "" + models.get(position).getBank_id());
                    extras.putString("bank_name_id", "" + models.get(position).getBank_name_id());
                    extras.putString("add_type", "" + models.get(position).getAdd_type());
                    extras.putString("account_number", "" + models.get(position).getAccount_number());
                    extras.putString("bank_name", "" + models.get(position).getBank_name());
                    extras.putString("ifsc_code", "" + models.get(position).getIfsc_code());
                    extras.putString("image", "" + models.get(position).getImage());
                    extras.putString("is_other_deposit", "" + models.get(position).getIs_other_deposit());
                    extras.putString("is_above", "" + models.get(position).getIs_above());
                    extras.putString("note", "" + models.get(position).getNote());
                    extras.putString("account_holder_name", "" + models.get(position).getAccount_holder_name());
                    extras.putString("account_type", "" + models.get(position).getAccount_type());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBank;
            TextView tvName, tvAccountNo, tvIFSC, tvDepositLimit;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                imgBank = itemView.findViewById(R.id.imgBank);
                tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
                tvIFSC = itemView.findViewById(R.id.tvIFSC);
                tvDepositLimit = itemView.findViewById(R.id.tvDepositLimit);
            }
        }
    }

    class ModelBank {
        String bank_id, bank_name_id, add_type, account_number, deposit_limit, is_above, is_other_deposit;
        String bank_name, account_holder_name, ifsc_code, account_type, image, note, share_data;

        public ModelBank(String bank_id, String bank_name_id, String add_type, String account_number, String deposit_limit, String is_above, String is_other_deposit, String bank_name, String account_holder_name, String ifsc_code, String account_type, String image, String note, String share_data) {
            this.bank_id = bank_id;
            this.bank_name_id = bank_name_id;
            this.add_type = add_type;
            this.account_number = account_number;
            this.deposit_limit = deposit_limit;
            this.is_above = is_above;
            this.is_other_deposit = is_other_deposit;
            this.bank_name = bank_name;
            this.account_holder_name = account_holder_name;
            this.ifsc_code = ifsc_code;
            this.account_type = account_type;
            this.image = image;
            this.note = note;
            this.share_data = share_data;
        }

        public String getShare_data() {
            return share_data;
        }

        public void setShare_data(String share_data) {
            this.share_data = share_data;
        }

        public String getBank_id() {
            return bank_id;
        }

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }

        public String getBank_name_id() {
            return bank_name_id;
        }

        public void setBank_name_id(String bank_name_id) {
            this.bank_name_id = bank_name_id;
        }

        public String getAdd_type() {
            return add_type;
        }

        public void setAdd_type(String add_type) {
            this.add_type = add_type;
        }

        public String getAccount_number() {
            return account_number;
        }

        public void setAccount_number(String account_number) {
            this.account_number = account_number;
        }

        public String getDeposit_limit() {
            return deposit_limit;
        }

        public void setDeposit_limit(String deposit_limit) {
            this.deposit_limit = deposit_limit;
        }

        public String getIs_above() {
            return is_above;
        }

        public void setIs_above(String is_above) {
            this.is_above = is_above;
        }

        public String getIs_other_deposit() {
            return is_other_deposit;
        }

        public void setIs_other_deposit(String is_other_deposit) {
            this.is_other_deposit = is_other_deposit;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getAccount_holder_name() {
            return account_holder_name;
        }

        public void setAccount_holder_name(String account_holder_name) {
            this.account_holder_name = account_holder_name;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public void setIfsc_code(String ifsc_code) {
            this.ifsc_code = ifsc_code;
        }

        public String getAccount_type() {
            return account_type;
        }

        public void setAccount_type(String account_type) {
            this.account_type = account_type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    private void getBankDetail() {
        try {
            if (!ConnectionManager.isOnline(getActivity())) {
                ConnectionManager.createDialog(getActivity());
                Log.d("Network state", ConnectionManager.isOnline(getActivity()) + "");
                return;
            }

            final String token = ShPrefUserDetails.getToken(getActivity());
            StringRequest request = new StringRequest(Request.Method.GET, AppURL.getUserAddWallet() + "/2",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Tag", "server response => " + response);

                                final JSONObject result = new JSONObject(response);
                                int status = result.getInt("status");

                                if (status == 200) {
                                    if (!result.isNull("data")) {
                                        JSONArray jsonArray = result.getJSONArray("data");
                                        models = new ArrayList<>();

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String bank_id = obj.isNull("bank_id") ? "" : obj.getString("bank_id");
                                            String bank_name_id = obj.isNull("bank_name_id") ? "" :obj.getString("bank_name_id");
                                            String add_type = obj.isNull("add_type") ? "" :obj.getString("add_type");
                                            String account_number = obj.isNull("account_number") ? "" :obj.getString("account_number");
                                            String deposit_limit = obj.isNull("deposit_limit") ? "" :obj.getString("deposit_limit");
                                            String is_above = obj.isNull("is_above") ? "" :obj.getString("is_above");
                                            String is_other_deposit = obj.isNull("is_other_deposit") ? "" :obj.getString("is_other_deposit");
                                            String bank_name = obj.isNull("bank_name") ? "" :obj.getString("bank_name");
                                            String ifsc_code = obj.isNull("ifsc_code") ? "" :obj.getString("ifsc_code");
                                            String account_holder_name = obj.isNull("account_holder_name") ? "" :obj.getString("account_holder_name");
                                            String account_type = obj.isNull("account_type") ? "" :obj.getString("account_type");
                                            String image = obj.isNull("image") ? "" :obj.getString("image");
                                            String note = obj.isNull("note") ? "" :obj.getString("note");
                                            String share_data = obj.isNull("share_data") ? "" :obj.getString("share_data");
                                            models.add(new ModelBank(bank_id, bank_name_id, add_type, account_number, deposit_limit, is_above, is_other_deposit, bank_name, account_holder_name, ifsc_code, account_type, image, note, share_data));
                                        }

                                        List<ModelBank> modelBanks = new ArrayList<>();
                                        if (models != null && models.size() > 0) {
                                            ArrayList<ModelBank> list = new ArrayList<>();
                                            for (int i = 0; i < models.size(); i++) {
                                                ModelBank modelBank = models.get(i);
                                                String add_type = modelBank.getAdd_type();
                                                if (add_type.equalsIgnoreCase("1")) {
                                                    list.add(modelBank);
                                                }
                                            }
                                            modelBanks = list;
                                        } else {

                                        }
                                        BankListAdapter adapter = new BankListAdapter(modelBanks, getActivity());
                                        // BankListAdapter adapter = new BankListAdapter(models, getActivity());
                                        bankList.setLayoutManager(layoutManager);
                                        bankList.setAdapter(adapter);
                                        //Toast.makeText(TransactionHistoryActivity.this, "Sucess"+models, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //imgNoFound.setVisibility(View.VISIBLE);
                                        // Toast.makeText(TransactionHistoryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (status == 400) {
                                    //  imgNoFound.setVisibility(View.VISIBLE);
                                    //Toast.makeText(TransactionHistoryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                } else if (status == 401) {
                                    //  imgNoFound.setVisibility(View.VISIBLE);
                                    // Snackbar.make(relativeLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();

                                    // startActivity(new Intent(TransactionHistoryActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    //Toast.makeText(CropsSubCategoryActivity.this, "Unauthorized user ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    //imgNoFound.setVisibility(View.VISIBLE);
                    //  Snackbar.make(relativeLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                }
            }) {
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

               /* @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "1");
                    return params;
                }*/
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(getActivity(), request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}