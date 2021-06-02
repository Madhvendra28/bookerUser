package com.bookkr.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fragment.BankDetailsFragment;
import com.fragment.WalletDetailsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.ConnectionManager;
import com.utils.GetServerData;
import com.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WalletBankDetailActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    LinearLayout llWallet, llBank;
    Toolbar mToolbarView;
    FloatingActionButton fabAdd;
    private TextView nodata_textview;
    LinearLayout nodata_LL;
    int tabMode = 1;

    private static AppCompatActivity activity;
    private int requestFor = -1;
    private ProgressDialog progress;
    private final String TAG = "Requirement List";

    RelativeLayout layoutWalletFragment, layoutBankFragment;

    //wallet
    RecyclerView walletList;
    LinearLayoutManager layoutManager;
    LinearLayout llSelf1, llOther;
    TextView tvSelf1, tvOther;
    List<ModelWallet> models;
    WalletListAdapter walletListAdapter;
    public static final int PERMISSION_WRITE = 0;

    //bank
    RecyclerView bankList;
    LinearLayout llSelf2, llOthers, llFamily, llFriends, llGFS;
    List<ModelBank> bankmodels;
    BankListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bank_detail);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            nodata_LL = findViewById(R.id.nodata_LL);
            nodata_textview = findViewById(R.id.nodata_textview);

            activity = this;
            llWallet = findViewById(R.id.llWallet);
            llBank = findViewById(R.id.llBank);
            fabAdd = findViewById(R.id.fabAdd);
            layoutWalletFragment = findViewById(R.id.layoutWalletFragment);
            layoutBankFragment = findViewById(R.id.layoutBankFragment);

            //wallet
            walletList = findViewById(R.id.walletList);
            llSelf1 = findViewById(R.id.llSelf1);
            llOther = findViewById(R.id.llOther);
            tvSelf1 = findViewById(R.id.tvSelf);
            tvOther = findViewById(R.id.tvOther);

            bankList = findViewById(R.id.bankList);
            llSelf2 = findViewById(R.id.llSelf2);
            llOthers = findViewById(R.id.llOthers);
            llFamily = findViewById(R.id.llFamily);
            llFriends = findViewById(R.id.llFriends);
            llGFS = findViewById(R.id.llGFS);

            llWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llWallet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    llBank.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                    tabMode = 1;
                    onButtonClick(view);
                }
            });

            llBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llBank.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    llWallet.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                    onButtonClick(view);
                    tabMode = 2;
                }
            });


            llWallet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            llBank.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
            onButtonClick(llWallet);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(activity, fabAdd);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.menu_bank_wallet, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemID = item.getItemId();
                            switch (itemID) {
                                case R.id.action_add_wallet:
                                    startActivityForResult(new Intent(WalletBankDetailActivity.this, AddWalletDetailActivity.class), 101);
                                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                                    break;
                                case R.id.action_add_bank:
                                    startActivityForResult(new Intent(WalletBankDetailActivity.this, AddBankDetailsActivity.class), 102);
                                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                                    break;
                            }
                            //Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                    /*if (tabMode == 1) {
                        startActivityForResult(new Intent(WalletBankDetailActivity.this, AddWalletDetailActivity.class), 101);
                    } else {
                        startActivityForResult(new Intent(WalletBankDetailActivity.this, AddBankDetailsActivity.class), 102);
                    }*/
                }
            });
        } catch (Exception e) {
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == 101) {
                llWallet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llBank.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                tabMode = 1;
                onButtonClick(llWallet);
            }
            if (resultCode == 102) {
                llBank.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llWallet.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                tabMode = 2;
                onButtonClick(llBank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonClick(View v) {
        Fragment fragment;

        switch (v.getId()) {
            case R.id.llWallet:
                setWalletLayoutVisible();
                /*fragment = new WalletDetailsFragment();
                replaceFragment(fragment);*/
                break;
            case R.id.llBank:
                setBankLayoutVisible();
                /*fragment = new BankDetailsFragment();
                replaceFragment(fragment);*/
                break;
        }
    }

    void setWalletLayoutVisible() {
        layoutWalletFragment.setVisibility(View.VISIBLE);
        layoutBankFragment.setVisibility(View.GONE);
        llSelf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
                walletListAdapter = new WalletListAdapter(modelWallets, activity);
                walletList.setAdapter(walletListAdapter);

            }
        });
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llOther.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llSelf1.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
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
                walletListAdapter = new WalletListAdapter(modelWallets, activity);
                walletList.setAdapter(walletListAdapter);
            }
        });

        llSelf1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llOther.setBackgroundColor(getResources().getColor(R.color.bg_color_white));

        layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);

        nodata_LL.setVisibility(View.GONE);
        getWalletDetail();
    }

    void setBankLayoutVisible() {
        layoutBankFragment.setVisibility(View.VISIBLE);
        layoutWalletFragment.setVisibility(View.GONE);
        llSelf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llOthers.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (bankmodels != null && bankmodels.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < bankmodels.size(); i++) {
                        ModelBank modelBank = bankmodels.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("1")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, activity);
                bankList.setAdapter(adapter);

            }
        });

        llFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf2.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOthers.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (bankmodels != null && bankmodels.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < bankmodels.size(); i++) {
                        ModelBank modelBank = bankmodels.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("2")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, activity);
                bankList.setAdapter(adapter);

            }
        });

        llFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf2.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOthers.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (bankmodels != null && bankmodels.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < bankmodels.size(); i++) {
                        ModelBank modelBank = bankmodels.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("3")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, activity);
                bankList.setAdapter(adapter);

            }
        });

        llGFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf2.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOthers.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (bankmodels != null && bankmodels.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < bankmodels.size(); i++) {
                        ModelBank modelBank = bankmodels.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("4")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, activity);
                bankList.setAdapter(adapter);

            }
        });

        llOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelf2.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                List<ModelBank> modelBanks = new ArrayList<>();
                if (bankmodels != null && bankmodels.size() > 0) {
                    ArrayList<ModelBank> list = new ArrayList<>();
                    for (int i = 0; i < bankmodels.size(); i++) {
                        ModelBank modelBank = bankmodels.get(i);
                        String add_type = modelBank.getAdd_type();
                        if (add_type.equalsIgnoreCase("5")) {
                            list.add(modelBank);
                        }
                    }
                    modelBanks = list;
                } else {

                }
                adapter = new BankListAdapter(modelBanks, activity);
                bankList.setAdapter(adapter);

            }
        });
        llSelf2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llOthers.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llFamily.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llFriends.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
        llGFS.setBackgroundColor(getResources().getColor(R.color.bg_color_white));

        layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        nodata_LL.setVisibility(View.GONE);
        getBankDetail();

    }

    class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {
        String fileUri;
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
            holder.tvName.setText("" + models.get(position).getAccount_holder_name());
            holder.tvAccountNo.setText("" + models.get(position).getAccount_number());
            holder.tvIFSC.setText("" + models.get(position).getIfsc_code());
            if (models.get(position).getIs_other_deposit().equalsIgnoreCase("0")) {
                holder.tvDepositLimit.setText("No");
            } else {
                holder.tvDepositLimit.setText("Yes (" + models.get(position).getDeposit_limit() + ")");
            }

            if (models.get(position).is_default.equalsIgnoreCase("0")) {
                holder.imgStared.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_default_unselected));
            } else if (models.get(position).is_default.equalsIgnoreCase("1")) {
                holder.imgStared.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_default_selected));
            }
            //holder.tvDepositLimit.setText("Dp limit left : " + models.get(position).getDeposit_limit());
            try {
                Picasso.get().load(models.get(position).getImage().replace("https", "http")).into(holder.imgBank);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.imgStared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (models.get(position).is_default.equalsIgnoreCase("0")) {
                        new MaterialAlertDialogBuilder(WalletBankDetailActivity.this)
                                .setMessage("Are you sure you want to set as default?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setAsDefaultBankAndWallet(models.get(position).getBank_id());
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();

                    }

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, BankDetailPreviewActivity.class);
//                    Intent intent = new Intent(activity, BankDetailEditActivity.class);
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
                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });

            holder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermission()) {
                        shareImage(models.get(position).getImage().replace("https", "http"), models.get(position).getShare_data());
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

                            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), BitmapFactory.decodeFile(fileUri), null, null));
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
            int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
                return false;
            }
            return true;
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBank;
            TextView tvName, tvAccountNo, tvIFSC, tvDepositLimit;
            ImageView imgStared, imgShare;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                imgBank = itemView.findViewById(R.id.imgBank);
                tvAccountNo = itemView.findViewById(R.id.tvAccountNo);
                tvIFSC = itemView.findViewById(R.id.tvIFSC);
                tvDepositLimit = itemView.findViewById(R.id.tvDepositLimit);
                imgStared = itemView.findViewById(R.id.imgStared);
                imgShare = itemView.findViewById(R.id.imgShare);
            }
        }
    }

    class ModelBank {
        String bank_id, bank_name_id, add_type, account_number, deposit_limit, is_above, is_other_deposit;
        String bank_name, account_holder_name, ifsc_code, account_type, image, note, share_data, is_default;

        public ModelBank(String bank_id, String bank_name_id, String add_type, String account_number, String deposit_limit, String is_above, String is_other_deposit, String bank_name, String account_holder_name, String ifsc_code, String account_type, String image, String note, String share_data, String is_default) {
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
            this.is_default = is_default;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
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

    class ModelWallet {
        String wallet_id, wallet_name_id, add_type, phone_no;
        String wallet_name, banking_name, upi_id, image, qr_code_full_image, share_data, is_default;

        public ModelWallet(String wallet_id, String wallet_name_id, String add_type, String phone_no, String wallet_name, String banking_name, String upi_id, String image, String qr_code_full_image, String share_data, String is_default) {
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
            this.is_default = is_default;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
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
        public WalletListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallet_details_item, parent, false);
            return new WalletListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final WalletListAdapter.ViewHolder holder, final int position) {
            holder.tvName.setText("" + models.get(position).getBanking_name());
            String arrayUpi;
            arrayUpi = models.get(position).getUpi_id().replace(",", "\n");
            holder.tvUpiId.setText("" + arrayUpi);
            holder.tvNo.setText("" + models.get(position).getPhone_no());
            if (models.get(position).is_default.equalsIgnoreCase("0")) {
                holder.imgStared.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_default_unselected));
            } else if (models.get(position).is_default.equalsIgnoreCase("1")) {
                holder.imgStared.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_default_selected));
            }

            if (models.get(position).getPhone_no().isEmpty()) {
                holder.llMobileNo.setVisibility(View.GONE);
            }
            if (models.get(position).getUpi_id().isEmpty()) {
                holder.llUpiID.setVisibility(View.GONE);
            }
            try {
                Picasso.get().load(models.get(position).getImage().replace("https", "http")).into(holder.imgUpi);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, WalletPreviewDetailActivity.class);
//                    Intent intent = new Intent(activity, WalletEditDetailActivity.class);
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
                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                }
            });

            holder.imgStared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (models.get(position).is_default.equalsIgnoreCase("0")) {
                        new MaterialAlertDialogBuilder(WalletBankDetailActivity.this)
                                .setMessage("Are you sure you want to set as default?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setAsDefaultBankAndWallet(models.get(position).getWallet_id());
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
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

                            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), BitmapFactory.decodeFile(fileUri), null, null));
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
            int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
                return false;
            }
            return true;
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgUpi, imgShare, imgStared;
            TextView tvName, tvNo, tvUpiId;
            LinearLayout llMobileNo, llUpiID;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                imgUpi = itemView.findViewById(R.id.imgUpi);
                tvNo = itemView.findViewById(R.id.tvNo);
                tvUpiId = itemView.findViewById(R.id.tvUpiId);
                imgShare = itemView.findViewById(R.id.imgShare);
                imgStared = itemView.findViewById(R.id.imgStared);
                llMobileNo = itemView.findViewById(R.id.llMobileNo);
                llUpiID = itemView.findViewById(R.id.llUpiID);
            }
        }
    }

    private void setAsDefaultBankAndWallet(String id) {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
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
            parameter.setUri(AppURL.getSetDefaultBaW() + "/" + tabMode + "/" + id);

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
                                            if (tabMode == 1) {
                                                llWallet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                llBank.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                                                onButtonClick(llWallet);
                                            } else {
                                                llBank.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                llWallet.setBackgroundColor(getResources().getColor(R.color.bg_color_white));
                                                onButtonClick(llBank);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
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
            giveErrorOnError();
            e.printStackTrace();
        }
    }

    private void getWalletDetail() {
        try {
            if (!ConnectionManager.isOnline(this)) {
                ConnectionManager.createDialog(this);
                Log.d("Network state", ConnectionManager.isOnline(this) + "");
                return;
            }

            String userId = ShPrefUserDetails.getToken(this);
            if (userId == null) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
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
            parameter.setUri(AppURL.getUserAddWallet() + "/1");

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
                                            JSONArray walletListArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONArray(AppURLParams.data) : null;
                                            if (walletListArray != null && walletListArray.length() > 0) {

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
                                                    String is_default = obj.isNull("is_default") ? "" : obj.getString("is_default");

                                                    models.add(new ModelWallet(wallet_id, wallet_name_id, add_type, phone_no, wallet_name, banking_name, upi_id, image, qr_code_full_image, share_data, is_default));
                                                }
                                                List<ModelWallet> modelWallets = new ArrayList<>();
                                                if (models != null && models.size() > 0) {
                                                    ArrayList<ModelWallet> list = new ArrayList<>();
                                                    for (int j = 0; j < models.size(); j++) {
                                                        ModelWallet modelWallet = models.get(j);
                                                        String add_type1 = modelWallet.getAdd_type();
                                                        if (add_type1.equalsIgnoreCase("1")) {
                                                            list.add(modelWallet);
                                                        }
                                                    }
                                                    modelWallets = list;
                                                } else {

                                                }
                                                walletListAdapter = new WalletListAdapter(modelWallets, activity);
                                                walletList.setLayoutManager(layoutManager);
                                                walletList.setAdapter(walletListAdapter);
                                            } else {
                                                giveErrorOnNoData();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
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
            giveErrorOnError();
            e.printStackTrace();
        }
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
                Snackbar.make(coordinatorLayout, getString(R.string.error_cannot_get_profile), Snackbar.LENGTH_LONG).show();
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
            parameter.setUri(AppURL.getUserAddWallet() + "/2");

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
                                            JSONArray jsonArray = !jsonObject.getString(AppURLParams.data).equals("null") ? jsonObject.getJSONArray(AppURLParams.data) : null;
                                            if (jsonArray != null && jsonArray.length() > 0) {

                                                bankmodels = new ArrayList<>();

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject obj = jsonArray.getJSONObject(i);
                                                    String bank_id = obj.isNull("bank_id") ? "" : obj.getString("bank_id");
                                                    String bank_name_id = obj.isNull("bank_name_id") ? "" : obj.getString("bank_name_id");
                                                    String add_type = obj.isNull("add_type") ? "" : obj.getString("add_type");
                                                    String account_number = obj.isNull("account_number") ? "" : obj.getString("account_number");
                                                    String deposit_limit = obj.isNull("deposit_limit") ? "" : obj.getString("deposit_limit");
                                                    String is_above = obj.isNull("is_above") ? "" : obj.getString("is_above");
                                                    String is_other_deposit = obj.isNull("is_other_deposit") ? "" : obj.getString("is_other_deposit");
                                                    String bank_name = obj.isNull("bank_name") ? "" : obj.getString("bank_name");
                                                    String ifsc_code = obj.isNull("ifsc_code") ? "" : obj.getString("ifsc_code");
                                                    String account_holder_name = obj.isNull("account_holder_name") ? "" : obj.getString("account_holder_name");
                                                    String account_type = obj.isNull("account_type") ? "" : obj.getString("account_type");
                                                    String image = obj.isNull("image") ? "" : obj.getString("image");
                                                    String note = obj.isNull("note") ? "" : obj.getString("note");
                                                    String share_data = obj.isNull("share_data") ? "" : obj.getString("share_data");
                                                    String is_default = obj.isNull("is_default") ? "" : obj.getString("is_default");
                                                    bankmodels.add(new ModelBank(bank_id, bank_name_id, add_type, account_number, deposit_limit, is_above, is_other_deposit, bank_name, account_holder_name, ifsc_code, account_type, image, note, share_data, is_default));
                                                }

                                                List<ModelBank> modelBanks = new ArrayList<>();
                                                if (bankmodels != null && bankmodels.size() > 0) {
                                                    ArrayList<ModelBank> list = new ArrayList<>();
                                                    for (int i = 0; i < bankmodels.size(); i++) {
                                                        ModelBank modelBank = bankmodels.get(i);
                                                        String add_type = modelBank.getAdd_type();
                                                        if (add_type.equalsIgnoreCase("1")) {
                                                            list.add(modelBank);
                                                        }
                                                    }
                                                    modelBanks = list;
                                                } else {

                                                }
                                                BankListAdapter adapter = new BankListAdapter(modelBanks, activity);
                                                // BankListAdapter adapter = new BankListAdapter(models, getActivity());
                                                bankList.setLayoutManager(layoutManager);
                                                bankList.setAdapter(adapter);

                                            } else {
                                                giveErrorOnNoData();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.failure)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnNoData();

                                    } else if (jsonObject.getString(AppURLParams.status).equalsIgnoreCase(AppURLParams.unauthorizedAccess)) {
                                        Snackbar.make(coordinatorLayout, jsonObject.getString(AppURLParams.message), Snackbar.LENGTH_LONG).show();
                                        giveErrorOnError();
                                    }
                                } else {
                                    giveErrorOnError();
                                }

                            } catch (Exception e) {
                                giveErrorOnError();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    giveErrorOnError();
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
            giveErrorOnError();
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

    private void giveErrorOnError() {
        if (progress != null) {
            progress.dismiss();
        }
        Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
    }

    private void giveErrorOnNoData() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            Snackbar.make(coordinatorLayout, getString(R.string.error_no_data_found), Snackbar.LENGTH_SHORT).show();
            layoutWalletFragment.setVisibility(View.GONE);
            layoutBankFragment.setVisibility(View.GONE);
            nodata_LL.setVisibility(View.VISIBLE);
            nodata_textview.setText(getString(R.string.error_no_data_found));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}