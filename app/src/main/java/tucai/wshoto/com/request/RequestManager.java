package tucai.wshoto.com.request;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tucai.wshoto.com.activity.MainActivity;
import tucai.wshoto.com.activity.MapActivity;
import tucai.wshoto.com.activity.WebAppActivity;
import tucai.wshoto.com.response.BaseBean;
import tucai.wshoto.com.response.BodyResponse;
import tucai.wshoto.com.response.HospitalResponse;
import tucai.wshoto.com.response.StoreBean;

/**
 * Created by Weshine on 2017/5/8.
 */

public class RequestManager {
    private AppRequest request;

    private static RequestManager instance;
    private Context context;
    public static RequestManager getInstance(Context context){
        if(instance == null){
            instance = new RequestManager(context);
        }
        return instance;
    }
    protected RequestManager(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        request = retrofit.create(AppRequest.class);
    }
    public void getMap(Context context,String id) throws IOException {
        this.context = context;
        Call<HospitalResponse> call = request.getMap(id);
        final MapActivity activity = (MapActivity) context;
         call.enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, Response<HospitalResponse> response) {
             String result = response.body().toString();
                HospitalResponse.MessageBean msgs = response.body().getMessage();
                activity.getResult(msgs);
            }

            @Override
            public void onFailure(Call<HospitalResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void getMap2(Context context,String id) throws IOException {
        this.context = context;
        Call<StoreBean> call = request.getMap2(id);
        final MapActivity activity = (MapActivity) context;
        call.enqueue(new Callback<StoreBean>() {
            @Override
            public void onResponse(Call<StoreBean> call, Response<StoreBean> response) {
                String result = response.body().toString();
                StoreBean.MessageBean msgs = response.body().getMessage();
                activity.getResult2(msgs);
            }

            @Override
            public void onFailure(Call<StoreBean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void getBody(Context context) throws IOException {
        Log.d("wjj","getBody");
        Call<BodyResponse> call = request.getBody();
        final MainActivity activity = (MainActivity) context;
        call.enqueue(new Callback<BodyResponse>() {
            @Override
            public void onResponse(Call<BodyResponse> call, Response<BodyResponse> response) {
                String result = response.body().toString();
                Log.d("wjj","body");
              List<BodyResponse.MessageBean>  msgs = response.body().getMessage();
                activity.getResult(msgs);
            }

            @Override
            public void onFailure(Call<BodyResponse> call, Throwable t) {
                Log.d("wjj","err");
                t.printStackTrace();
            }
        });
    }
    public void getAva(Context context,File headPicFile) throws IOException {
        Log.d("chenyi","getAva");
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("img", fileToBase64(headPicFile));
        Log.d("wjj",fileToBase64(headPicFile));
        RequestBody requestBody = bodyBuilder.build();
        Call<BaseBean> call = request.getAva(requestBody);
        final WebAppActivity activity = (WebAppActivity) context;
        call.enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                String msg = response.body().getMessage();
                activity.deletePic();
                Log.d("wjj",msg);
                activity.getResult(msg);
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                Log.d("wjj","err");
                activity.deletePic();
                t.printStackTrace();
            }
        });
    }
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }
}
