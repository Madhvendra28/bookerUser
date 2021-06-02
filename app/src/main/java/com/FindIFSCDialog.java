package com;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bookkr.user.EventDetailsActivity;
import com.bookkr.user.R;
import com.model.RequestParameter;
import com.preferences.ShPrefUserDetails;
import com.utils.AppURL;
import com.utils.AppURLParams;
import com.utils.AppUtils;
import com.utils.ConnectionManager;
import com.utils.GetServerData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FindIFSCDialog extends Dialog {
    private Activity activity;
    TextView tvByBranchName, tvByStateDistrict;

    public FindIFSCDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_find_ifsc_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        tvByBranchName = findViewById(R.id.tvByBranchName);
        tvByStateDistrict = findViewById(R.id.tvByStateDistrict);

        Spannable span = Spannable.Factory.getInstance().newSpannable("By Branch Name");
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://paymatrix.in/find-ifsc-code"));
                    activity.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(activity, "Unable to open URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, 0, "By Branch Name".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvByBranchName.setText(span);
        tvByBranchName.setMovementMethod(LinkMovementMethod.getInstance());

        Spannable span1 = Spannable.Factory.getInstance().newSpannable("By State & Districts");
        span1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://bankifsccode.com/"));
                    activity.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(activity, "Unable to open URL", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, 0, "By State & Districts".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvByStateDistrict.setText(span1);
        tvByStateDistrict.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
