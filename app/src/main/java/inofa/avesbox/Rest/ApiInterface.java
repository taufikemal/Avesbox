package inofa.avesbox.Rest;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import inofa.avesbox.Model.DataKandangRespon;
import inofa.avesbox.Model.DataListSensorRespon;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Model.LoginResponUser;
import inofa.avesbox.Model.UpdatePassRespon;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static android.content.Context.MODE_PRIVATE;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginRespon> loginUser(
            @Field("username") String username,
            @Field("password") String password);

    @GET("record")
    Call<DataSensorRespon> dataHasilSensor(
            @Header("token") String token);

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<DetailUserRespon> dataProfilUser(
            @Header("token") String token,
            @Path("id") int id,
            @Field("nama") String nama,
            @Field("username") String username,
            @Field("alamat") String alamat);


    @GET("kandang")
    Call<DataKandangRespon> dataListKandang(
            @Header("token") String token);

    @GET("sensor")
    Call<DataListSensorRespon> dataListSensor(
            @Header("token") String token);

    @FormUrlEncoded
    @PUT("users/{id}/update-password")
    Call<UpdatePassRespon> updatePassUser(
            @Header("token") String token,
            @Path ("id") int id,
            @Field("password_current") String passwordCurrent,
            @Field("password") String passwordNew,
            @Field("password_confirmation") String passwordNewConfirm);
}
