package com.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.bookkr.user.R;
import com.bookkr.user.WalletPreviewDetailActivity;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletDetailsFragment extends Fragment {

    RecyclerView walletList;
    LinearLayoutManager layoutManager;
    LinearLayout llSelf, llOther;
    TextView tvSelf, tvOther;
    List<ModelWallet> models;
    WalletListAdapter adapter;
    public static final int PERMISSION_WRITE = 0;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_wallet_details, container, false);
        walletList = root.findViewById(R.id.walletList);
        llSelf = root.findViewById(R.id.llSelf);
        llOther = root.findViewById(R.id.llOther);
        tvSelf = root.findViewById(R.id.tvSelf);
        tvOther = root.findViewById(R.id.tvOther);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);

        llSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelWallet> modelWallets = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelWallet> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelWallet modelWallet = models.get(i);
                        String add_type = modelWallet.getAdd_type();
                        if (add_type.equalsIgnoreCase("1")) {
                            list.add(modelWallet);
                        }
                    }
                    modelWallets = list;
                } else {

                }
                adapter = new WalletListAdapter(modelWallets, getActivity());
                walletList.setAdapter(adapter);

            }
        });
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llOther.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llSelf.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelWallet> modelWallets = new ArrayList<>();
                if (models != null && models.size() > 0) {
                    ArrayList<ModelWallet> list = new ArrayList<>();
                    for (int i = 0; i < models.size(); i++) {
                        ModelWallet modelWallet = models.get(i);
                        String add_type = modelWallet.getAdd_type();
                        if (add_type.equalsIgnoreCase("2")) {
                            list.add(modelWallet);
                        }
                    }
                    modelWallets = list;
                } else {

                }
                adapter = new WalletListAdapter(modelWallets, getActivity());
                walletList.setAdapter(adapter);
            }
        });

        llSelf.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));

        final String token = ShPrefUserDetails.getToken(getActivity());
        // Log
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        getWalletDetail();
        return root;
    }


    class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.ViewHolder> {
        String fileUri;

        List<ModelWallet> models;
        Context context;

        public WalletListAdapter(List<ModelWallet> models, Context context) {
            this.models = models;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallet_details_item, parent, false);
            return new WalletListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvName.setText("Name : " + models.get(position).getBanking_name());
            holder.tvUpiId.setText("UPI Id :" + models.get(position).getUpi_id());
            holder.tvNo.setText("No : " + models.get(position).getPhone_no());
            Picasso.get().load(models.get(position).getImage().replace("https", "http")).into(holder.imgUpi);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), WalletPreviewDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("wallet_id", "" + models.get(position).getWallet_id());
                    extras.putString("wallet_name_id", "" + models.get(position).getWallet_name_id());
                    extras.putString("add_type", "" + models.get(position).getAdd_type());
                    extras.putString("phone_no", "" + models.get(position).getPhone_no());
                    extras.putString("wallet_name", "" + models.get(position).getWallet_name());
                    extras.putString("banking_name", "" + models.get(position).getBanking_name());
                    extras.putString("upi_id", "" + models.get(position).getUpi_id());
                    extras.putString("image", "" + models.get(position).getImage());
                    extras.putString("qr_code_full_image", "" + models.get(position).getQr_code_full_image());
                    extras.putString("share_data", "" + models.get(position).getShare_data());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            holder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermission()) {
                        shareImage(models.get(position).getQr_code_full_image().replace("https", "http"), models.get(position).getShare_data());
                    }
                }
            });
        }

        public void shareImage(String url, final String shareData) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    Picasso.get().load(url).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            try {
                                File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                                if (!mydir.exists()) {
                                    mydir.mkdirs();
                                }
                                fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                                FileOutputStream outputStream = new FileOutputStream(fileUri);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), BitmapFactory.decodeFile(fileUri), null, null));
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT,/*"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +*/shareData);
                            shareIntent.setType("image/png");
                            startActivity(Intent.createChooser(shareIntent, "Share with"));

    //                    Intent share = new Intent(Intent.ACTION_SEND);
    //                    share.setType("*/*");   // share.setType("image/*");
    //                    share.putExtra(Intent.EXTRA_STREAM, uri);
    //                    share.putExtra(Intent.EXTRA_TEXT, shareData);
    //                    activity.startActivity(Intent.createChooser(share, "Share Store"));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

                } else {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,/*"Hey please check this application " + "https://play.google.com/store/apps/details?id=" +*/shareData);
                    shareIntent.setType("text/*");
                    startActivity(Intent.createChooser(shareIntent, "Share with"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //runtime storage permission
        public boolean checkPermission() {
            int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
                return false;
            }
            return true;
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgUpi, imgShare;
            TextView tvName, tvNo, tvUpiId;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                imgUpi = itemView.findViewById(R.id.imgUpi);
                tvNo = itemView.findViewById(R.id.tvNo);
                tvUpiId = itemView.findViewById(R.id.tvUpiId);
                imgShare = itemView.findViewById(R.id.imgShare);
            }
        }
    }

    class ModelWallet {
        String wallet_id, wallet_name_id, add_type, phone_no;
        String wallet_name, banking_name, upi_id, image, qr_code_full_image, share_data;

        public ModelWallet(String wallet_id, String wallet_name_id, String add_type, String phone_no, String wallet_name, String banking_name, String upi_id, String image, String qr_code_full_image, String share_data) {
            this.wallet_id = wallet_id;
            this.wallet_name_id = wallet_name_id;
            this.add_type = add_type;
            this.phone_no = phone_no;
            this.wallet_name = wallet_name;
            this.banking_name = banking_name;
            this.upi_id = upi_id;
            this.image = image;
            this.qr_code_full_image = qr_code_full_image;
            this.share_data = share_data;
        }

        public String getShare_data() {
            return share_data;
        }

        public void setShare_data(String share_data) {
            this.share_data = share_data;
        }

        public String getWallet_id() {
            return wallet_id;
        }

        public void setWallet_id(String wallet_id) {
            this.wallet_id = wallet_id;
        }

        public String getWallet_name_id() {
            return wallet_name_id;
        }

        public void setWallet_name_id(String wallet_name_id) {
            this.wallet_name_id = wallet_name_id;
        }

        public String getAdd_type() {
            return add_type;
        }

        public void setAdd_type(String add_type) {
            this.add_type = add_type;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getWallet_name() {
            return wallet_name;
        }

        public void setWallet_name(String wallet_name) {
            this.wallet_name = wallet_name;
        }

        public String getBanking_name() {
            return banking_name;
        }

        public void setBanking_name(String banking_name) {
            this.banking_name = banking_name;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getQr_code_full_image() {
            return qr_code_full_image;
        }

        public void setQr_code_full_image(String qr_code_full_image) {
            this.qr_code_full_image = qr_code_full_image;
        }
    }

    private void getWalletDetail() {
        dialog.show();
        try {
            if (!ConnectionManager.isOnline(getActivity())) {
                ConnectionManager.createDialog(getActivity());
                Log.d("Network state", ConnectionManager.isOnline(getActivity()) + "");
                return;
            }

            final String token = ShPrefUserDetails.getToken(getActivity());
            StringRequest request = new StringRequest(Request.Method.GET, AppURL.getUserAddWallet() + "/1",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Tag", "server response => " + response);

                                final JSONObject result = new JSONObject(response);
                                int status = result.getInt("status");

                                if (status == 200) {
                                    if (!result.isNull("data")) {
                                        JSONArray walletListArray = result.getJSONArray("data");
                                        models = new ArrayList<>();

                                        for (int i = 0; i < walletListArray.length(); i++) {
                                            JSONObject obj = walletListArray.getJSONObject(i);
                                            String wallet_id = obj.isNull("wallet_id") ? "" : obj.getString("wallet_id");
                                            String wallet_name_id = obj.isNull("wallet_name_id") ? "" : obj.getString("wallet_name_id");
                                            String wallet_name = obj.isNull("wallet_name") ? "" : obj.getString("wallet_name");
                                            String banking_name = obj.isNull("banking_name") ? "" : obj.getString("banking_name");
                                            String phone_no = obj.isNull("phone_no") ? "" : obj.getString("phone_no");
                                            String add_type = obj.isNull("add_type") ? "" : obj.getString("add_type");
                                            String upi_id = obj.isNull("upi_id") ? "" : obj.getString("upi_id");
                                            String qr_code_full_image = obj.isNull("qr_code_full_image") ? "" : obj.getString("qr_code_full_image");
                                            String image = obj.isNull("image") ? "" : obj.getString("image");
                                            String share_data = obj.isNull("share_data") ? "" : obj.getString("share_data");

                                            models.add(new ModelWallet(wallet_id, wallet_name_id, add_type, phone_no, wallet_name, banking_name, upi_id, image, qr_code_full_image, share_data));
                                        }

                                        List<ModelWallet> modelWallets = new ArrayList<>();
                                        if (models != null && models.size() > 0) {
                                            ArrayList<ModelWallet> list = new ArrayList<>();
                                            for (int i = 0; i < models.size(); i++) {
                                                ModelWallet modelWallet = models.get(i);
                                                String add_type = modelWallet.getAdd_type();
                                                if (add_type.equalsIgnoreCase("1")) {
                                                    list.add(modelWallet);
                                                }
                                            }
                                            modelWallets = list;
                                        } else {

                                        }
                                        adapter = new WalletListAdapter(modelWallets, getActivity());
                                   /*     walletList.setAdapter(adapter);
                                        adapter = new WalletListAdapter(models, getActivity());*/
                                        walletList.setLayoutManager(layoutManager);
                                        walletList.setAdapter(adapter);
                                        //Toast.makeText(TransactionHistoryActivity.this, "Sucess"+models, Toast.LENGTH_SHORT).show();
                                    } else {
                                        //imgNoFound.setVisibility(View.VISIBLE);
                                        // Toast.makeText(TransactionHistoryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                    }
                                dialog.dismiss();
                                } else if (status == 400) {
                                    dialog.dismiss();

                                    //  imgNoFound.setVisibility(View.VISIBLE);
                                    //Toast.makeText(TransactionHistoryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                                } else if (status == 401) {
                                    dialog.dismiss();

                                    //  imgNoFound.setVisibility(View.VISIBLE);
                                    // Snackbar.make(relativeLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();

                                    // startActivity(new Intent(TransactionHistoryActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    //Toast.makeText(CropsSubCategoryActivity.this, "Unauthorized user ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                dialog.dismiss();

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    dialog.dismiss();

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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "1");
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(150000, 3, 3));
            GetServerData.addRequestToQueue(getActivity(), request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}