package tucai.wshoto.com.request;

import tucai.wshoto.com.response.BaseBean;
import tucai.wshoto.com.response.BodyResponse;
import tucai.wshoto.com.response.HospitalResponse;
import tucai.wshoto.com.response.StoreBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Weshine on 2017/5/8.
 */

public interface AppRequest {
    @GET(HttpConstants.BODY_URL)
    Call<BodyResponse> getBody();
    @GET(HttpConstants.HOSPITAL_MAP_URL)
    Call<HospitalResponse> getMap(@Query("id")String id);
    @GET(HttpConstants.STORE_MAP_URL)
    Call<StoreBean> getMap2(@Query("id")String id);
    @POST(HttpConstants.PHOTO_URL)
    Call<BaseBean> getAva(@Body RequestBody updatedBody);
 /*   @Multipart
    @POST(HttpConstants.PHOTO_URL)
    Call<BaseBean> postPhoto(@PartMap Map<String, RequestBody> params);*/
}
