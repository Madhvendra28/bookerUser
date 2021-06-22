package com.retrofit;


import com.google.gson.JsonObject;
import com.model.confirmclaim.ConfirmClaimDataResponse;
import com.model.payfailModel.PayFailResponse;
import com.preferences.ShPrefUserDetails;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {



    @POST("reg_user.php")
    Call<JsonObject> getRegister(@Body RequestBody requestBody);

    @GET("confirmClaimDetails/{id}/{id2}")
    Call<ConfirmClaimDataResponse> getConfirmClaimData(@Header("Authorization") String token,@Path("id") String id,@Path("id2") String id2);


    @GET("getPayFailData/{claim_confirm_id}")
    Call<PayFailResponse> getPayfailData(@Header("Authorization") String token, @Path("claim_confirm_id") String claim_confirm_id);

    @POST("userPayFailSubmit")
    Call<ResponseBody> submitPayFail(@Header("Authorization") String token,  @FieldMap Map<String, String> params);

    @POST("userLogin")
    Call<JsonObject> getLogin();

}
