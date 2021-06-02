package com.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bookkr.user.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.FileProvider;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.Context.CLIPBOARD_SERVICE;

public class AppUtils {

    private static String fileName = "file";
    private static Activity activity;

    // Function to validate the pin code of India.
    public static boolean isValidPinCode(String pinCode) {

        // Regex to check valid pin code of India.
        String regex
                = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the pin code is empty
        // return false
        if (pinCode == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given pin code
        // and regular expression.
        Matcher m = p.matcher(pinCode);

        // Return if the pin code
        // matched the ReGex
        return m.matches();
    }

    public static String[] getStringArrayTokenize(String s) {
        try{
            return  s.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            View focusedView = activity.getCurrentFocus();
            View focusedView = activity.getWindow().getDecorView().getRootView();
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeImageFromStorage(Activity activity, String path) {
        try {
            File f = new File(path);
            if (f.delete())
                Log.i("image deleted", "Image deleted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parseDateForOpenSale(String time) {
        String inputPattern = "yyyy-MM-dd h:m a";
        String outputPattern = "d MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertDateWithoutTime(String timestampStr) {
        try {
//            long unixSeconds = Long.parseLong(timestampStr);
//            Date date = new Date(unixSeconds * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("d MMM | EEE");

            Date dateObj = sdf.parse(timestampStr);
//            sdf1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            String formattedDate = sdf1.format(dateObj);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseDateTodMMMEEEhmma(String time) {
        String inputPattern = "yyyy-MM-dd h:m a";
        String outputPattern = "d MMM | EEE \n h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static void copyToClipboard(Context ctx, String text) {
        try {
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getFormattedDateTime(String dateStr) {
        String formattedDate = dateStr;
        DateFormat readFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }
    public static String parseDateTohmmaForEventDetails(String time) {
        String inputPattern = "yyyy-MM-dd h:m a";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String parseDateTodMMMEEEForEventDetails(String time) {
        String inputPattern = "yyyy-MM-dd h:m a";
        String outputPattern = "d MMM | EEE";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(
                            tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(
            final String strSpanned, final TextView tv, final int maxLine,
            final String spanableText, final boolean viewMore) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (strSpanned.contains(spanableText)) {
            ssb.setSpan(
                    new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            if (viewMore) {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, -5, "...Read Less",
                                        false);
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, 5, "...Read More",
                                        true);
                                tv.setTextColor(Color.BLACK);
                            }

                        }
                    }, strSpanned.indexOf(spanableText),
                    strSpanned.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static void pasteLink(Activity activity, EditText editText) {
        try {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            String pasteData = "";

            // If it does contain data, decide if you can handle the data.
            if (!(clipboard.hasPrimaryClip())) {

            } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                // since the clipboard has data but it is not plain text

            } else {

                //since the clipboard contains plain text.
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                // Gets the clipboard as text.
                pasteData = item.getText().toString();
                editText.setText(pasteData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
