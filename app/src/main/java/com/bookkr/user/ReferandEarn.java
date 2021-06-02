package com.bookkr.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.preferences.ShPrefUserDetails;

public class ReferandEarn extends AppCompatActivity {

    TextView referal, copyReferal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referand_earn);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_bar_gray)));
        actionBar.setTitle("Refer and Earn");
        actionBar.show();

        String refer = ShPrefUserDetails.getUser_code(this);
        referal = findViewById(R.id.referal);
        referal.setText(refer + "");

        copyReferal = findViewById(R.id.copyReferal);
        copyReferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Referal", referal.getText().toString());
                clipboard.setPrimaryClip(clipData);

                Toast.makeText(ReferandEarn.this, "Copied", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void invite(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Click here to download App https://play.google.com/store/apps/ & Use this Referral code "+ShPrefUserDetails.getUser_code(this)+" to get Rs 50";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}