package tucai.wshoto.com.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tucai.wshoto.com.bean.ImageBean;

/**
 * Created by user on 2017/7/13.
 */

public class HttpJsonMethod {
    public static final String BASE_URL = "http://tczp.wshoto.com";
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private BlueService movieService;

    private HttpJsonMethod() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();


        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(BlueService.class);
    }

    private static class SingletonHolder {
        private static final HttpJsonMethod INSTANCE = new HttpJsonMethod();
    }

    //获取单例
    public static HttpJsonMethod getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            return httpResult.getOthers();
        }
    }




    public void getAva(Subscriber<ImageBean> subscriber, String avatar) {
        movieService.getAva(avatar)
//                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
