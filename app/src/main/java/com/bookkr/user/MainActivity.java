package com.bookkr.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MainActivity extends Activity {

    private CoordinatorLayout coordinatorLayout;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        try {
            coordinatorLayout = findViewById(R.id.coordinatorLayout);

            try {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            public void run() {
                                proceedToNext();
                            }
                        }, 1000);
                    }
                }, 100);
            } catch (Exception e) {
                Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private void proceedToNext() {
        try {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        } catch (Exception e) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_try_later), Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
