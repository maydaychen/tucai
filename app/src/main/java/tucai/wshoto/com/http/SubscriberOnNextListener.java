package tucai.wshoto.com.http;

import org.json.JSONException;

/**
 * 作者：JTR on 2016/11/25 14:55
 * 邮箱：2091320109@qq.com
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t) throws JSONException;
}