package inofa.avesbox.Rest;

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginRespon> loginUser(
            @Field("username") String username,
            @Field("password") String password);


    @GET("record")
    Call<DataSensorRespon> dataHasilSensor(
            @Header("token") String token);
}
