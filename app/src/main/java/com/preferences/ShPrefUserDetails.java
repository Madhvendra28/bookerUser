package com.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.model.UserProfile;

/**
 * Created by ANKIT on 29-Sep-19.
 */
public class ShPrefUserDetails {
    public static final String userDetails = "sh_pref_user_details";

    public static final String user_code = "user_code";
    public static final String name = "name";
    public static final String email_id = "email_id";
    public static final String contact_no = "contact_no";
    public static final String whatsapp_no = "whatsapp_no";
    public static final String gender = "gender";
    public static final String date_of_birth = "date_of_birth";

    public static final String address = "address";
    public static final String area = "area";
    public static final String state = "state";
    public static final String city = "city";
    public static final String postal_code = "postal_code";

    public static final String profile_image = "profile_image";
    public static final String document_type = "document_type";
    public static final String document_number = "document_number";
    public static final String document_image = "document_image";

    public static final String facebook_link = "facebook_link";
    public static final String twitter_link = "twitter_link";
    public static final String instagram_link = "instagram_link";
    public static final String telegram_link = "telegram_link";

    public static final String verification_status = "verification_status";
    public static final String status = "status";
    public static final String token = "token";

    public static final String fcmRegId = "sh_pref_fcm_regId";

//    ---------------------------   methods   ----------------------------------------------------


    public static boolean storeUserProfile(Activity activity, UserProfile userProfile) {
        try {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(ShPrefUserDetails.userDetails, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(ShPrefUserDetails.user_code, userProfile.getUser_code());
            editor.putString(ShPrefUserDetails.name, userProfile.getName());
            editor.putString(ShPrefUserDetails.email_id, userProfile.getEmail_id());
            editor.putString(ShPrefUserDetails.contact_no, userProfile.getContact_no());
            editor.putString(ShPrefUserDetails.whatsapp_no, userProfile.getWhatsappNumber());
            editor.putString(ShPrefUserDetails.gender, userProfile.getGender());
            editor.putString(ShPrefUserDetails.date_of_birth, userProfile.getDate_of_birth());

            editor.putString(ShPrefUserDetails.address, userProfile.getAddress());
            editor.putString(ShPrefUserDetails.area, userProfile.getArea());
            editor.putString(ShPrefUserDetails.postal_code, userProfile.getPostal_code());
            editor.putString(ShPrefUserDetails.state, userProfile.getState());
            editor.putString(ShPrefUserDetails.city, userProfile.getCity());

            editor.putString(ShPrefUserDetails.profile_image, userProfile.getProfileImage());
            editor.putString(ShPrefUserDetails.document_type, userProfile.getDocument_type());
            editor.putString(ShPrefUserDetails.document_number, userProfile.getDocument_number());
            editor.putString(ShPrefUserDetails.document_image, userProfile.getDocumentImage());

            editor.putString(ShPrefUserDetails.facebook_link, userProfile.getFacebook_link());
            editor.putString(ShPrefUserDetails.twitter_link, userProfile.getTwitter_link());
            editor.putString(ShPrefUserDetails.instagram_link, userProfile.getInstagram_link());
            editor.putString(ShPrefUserDetails.telegram_link, userProfile.getTelegram_link());

            editor.putString(ShPrefUserDetails.verification_status, userProfile.getVerification_status());
            editor.putString(ShPrefUserDetails.status, userProfile.getStatus());

            if(!userProfile.getToken().equals("")){
                editor.putString(ShPrefUserDetails.token, userProfile.getToken());
            }

            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeUserDataAfterSignout(Activity activity) {
        try {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(ShPrefUserDetails.userDetails, activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

//            editor.remove(dealer_id);
            editor.remove(user_code);
            editor.remove(name);
            editor.remove(email_id);
            editor.remove(contact_no);
            editor.remove(whatsapp_no);
            editor.remove(gender);
            editor.remove(date_of_birth);

            editor.remove(address);
            editor.remove(area);
            editor.remove(state);
            editor.remove(city);
            editor.remove(postal_code);

            editor.remove(profile_image);
            editor.remove(document_type);
            editor.remove(document_number);
            editor.remove(document_image);

            editor.remove(facebook_link);
            editor.remove(twitter_link);
            editor.remove(instagram_link);
            editor.remove(telegram_link);

            editor.remove(verification_status);
            editor.remove(status);
            editor.remove(token);

            boolean b = editor.commit();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getToken(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(token, null);
        return s;
    }

    public static String getWhatsapp_no(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(whatsapp_no, "");
        return s;
    }

    public static String getName(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(name, "");
        return s;
    }

    public static String getUser_code(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(user_code, "");
        return s;
    }

    public static String getVerification_status(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(verification_status, "");
        return s;
    }

    public static String getArea(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(area, null);
        return s;
    }

    public static void setFCMRegId(Context context, String regId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fcmRegId, regId);
        editor.commit();
    }

    public static String getFCMRegId(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(fcmRegId, null);
        return s;
    }

    public static String getProfile_image(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(profile_image, "");
        return s;
    }


    public static String getStringData(String key ,Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(key, null);
        return s;
    }
    public static void setStringData(Context context,String key, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }
}
