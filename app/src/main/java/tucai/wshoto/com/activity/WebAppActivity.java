package tucai.wshoto.com.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import tucai.wshoto.com.R;
import tucai.wshoto.com.Utils;
import tucai.wshoto.com.alipay.AliPayManager;
import tucai.wshoto.com.alipay.AliPayMessage;
import tucai.wshoto.com.application.App;
import tucai.wshoto.com.bean.ImageBean;
import tucai.wshoto.com.http.HttpJsonMethod;
import tucai.wshoto.com.http.ProgressErrorSubscriber;
import tucai.wshoto.com.http.SubscriberOnNextAndErrorListener;
import tucai.wshoto.com.request.HttpConstants;
import tucai.wshoto.com.wxapi.login.WXLoginInfoMessage;
import tucai.wshoto.com.wxapi.login.WXLoginManager;
import tucai.wshoto.com.wxapi.login.WXLoginMessage;
import tucai.wshoto.com.wxapi.login.response.AccessToken;
import tucai.wshoto.com.wxapi.login.response.RefreshToken;
import tucai.wshoto.com.wxapi.pay.WXPayEntry;
import tucai.wshoto.com.wxapi.pay.WXPayMessage;
import tucai.wshoto.com.wxapi.pay.WXUtils;

import static tucai.wshoto.com.R.id.webview;


/**
 * Created by chenyi on 2017/4/20.
 */

public class WebAppActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback, BDLocationListener, EasyPermissions.PermissionCallbacks {
    private Bitmap bmp;
    BridgeWebView webView;
    Button btn;
    private App app;
    private LinearLayout ll_gone;
    private ProgressDialog updateDialog = null;
    private IUiListener loginListener;
    private boolean isFirstLocate = true;
    //返回给Js的结果
    private final static String RESPONSE_TEXT_SUCCESS = "{\"statusCode\":\"0\", \"data\":\"\"}";
    private final static String RESPONSE_TEXT_FAIL = "{\"statusCode\":\"-1\", \"data\":\"\"}";
    private String fileName = Environment.getExternalStorageDirectory() + "/logo.png";
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    final public static int REQUEST_WRITE = 222;
    private static final int RC_STORAGE_CONTACTS_PERM = 125;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    //微信登录有效期
    private AccessToken mSavedAccessToken;
    private SubscriberOnNextAndErrorListener<ImageBean> uploadOnNext;
    /**
     * 上传拍照或图库中图片，处理后保存的路径
     */
    /*
    * 地图
    * */
    private LocationClient mLocationClient;
    private double lat;
    private double lon;
    private String myLoc;
    private SharedPreferences preferences;
    private File picFile;
    private SharedPreferences.Editor editor;
    private PushAgent mPushAgent;
    private View inflate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        setContentView(R.layout.activity_webapp);

        webView = (BridgeWebView) findViewById(webview);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }


        ll_gone = (LinearLayout) findViewById(R.id.ll_gone);
        init();
        //1
        EventBus.getDefault().register(this);
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(id)) {
            String url = HttpConstants.disease + "?id=" + id + "&" + "title=" + title;
            Log.i("chenyi", url);
            webView.loadUrl(url);
            ll_gone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            webView.loadUrl(HttpConstants.BASEURL4);
            //webView.loadUrl(HttpConstants.PAY);
        }
        webView.callHandler("frame", "123", jsResponseData -> Log.i("chenyi", "getLat " + jsResponseData));
        saveImg();
        uploadOnNext = new SubscriberOnNextAndErrorListener<ImageBean>() {
            @Override
            public void onNext(ImageBean jsonObject) {
                if (updateDialog != null) {
                    updateDialog.dismiss();
                    updateDialog = null;
                }
//                String msg = jsonObject.toString();
//                try {
//                    if (jsonObject.getInt("statusCode") == 1) {
//                        Toast.makeText(WebAppActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
//                        msg = jsonObject.getString("result");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                deletePic();
//                Log.d("wjj", msg);
//                getResult(msg);
            }

            @Override
            public void onError(Throwable e) {
                if (updateDialog == null) {
                    updateDialog.dismiss();
                }
                Toast.makeText(WebAppActivity.this, "上传失败，请稍后再试", Toast.LENGTH_SHORT).show();
                Log.i("chenyi", "err");
                deletePic();
                e.printStackTrace();
            }
        };
    }

    private void initMap() {
        mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocationClient.registerLocationListener(this);//注册监听函数
        initLocation();
        mLocationClient.start();//开启定位
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高 精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当                                                                                                                                                                                                                                gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public void getResult(String msg) {
        String result = "{\"status\":\"1\", \"message\":\"" + msg + "\"}";
        webView.callHandler("uploadImg", result, new CallBackFunction() {
            @Override
            public void onCallBack(String jsResponseData) {
                Log.i("chenyi", "uploadImg " + jsResponseData);
            }
        });
    }

    public void loginTencent() {
        app = (App) getApplication();
        if (!app.mTencent.isSessionValid()) {
            app.mTencent.login(this, "all", loginListener);
        }
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(WebAppActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                JSONObject object = (JSONObject) o;
                try {
                    String accessToken = object.getString("access_token");
                    String expires = object.getString("expires_in");
                    String openID = object.getString("openid");
                    app.mTencent.setAccessToken(accessToken, expires);
                    app.mTencent.setOpenId(openID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };
    }

    private void init() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //add by chenyi
        //cache mode
        //设置缓存模式
        if (isNetworkAvailable(WebAppActivity.this)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //open dom storage
        webSettings.setDomStorageEnabled(true);
        //priority high
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webSettings.setAppCacheEnabled(false);
        //如果访问的页面中有bview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);

        //add by chenyi end
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + ";wshoto");
        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        webView.registerHandler("getLocation", (responseData, function) -> {
            Log.i("chenyi", "getLocation: start");
            initMap();
        });
        webView.registerHandler("authLogin", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i("chenyi", "start login " + data);
                try {
                    JSONTokener jsonTokener = new JSONTokener(data);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    String type = wxJson.getString("type");
                    if ("wechat".equals(type)) {
                        Log.i("chenyi", "wechat");
                        WXLoginManager.getInstance(WebAppActivity.this).login();
                        function.onCallBack(RESPONSE_TEXT_SUCCESS);
                    } else if ("tencent".equals(type)) {
                        loginTencent();
                        function.onCallBack(RESPONSE_TEXT_SUCCESS);
                    } else {
                        function.onCallBack(RESPONSE_TEXT_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });

        webView.registerHandler("payment", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(responseData);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    Log.i("chenyi", "payment1");
                    String type = wxJson.getString("type");
                    String params = wxJson.getString("params");
                    Log.i("chenyi", params);
                    if ("wechat".equals(type)) {
                        Log.i("chenyi", "payment2");
                        WXPayEntry entry = WXUtils.parseWXData(params);
                        if (entry != null) {
                            Log.i("chenyi", "payment3");
                            WXUtils.startWeChat(WebAppActivity.this, entry);
                            function.onCallBack(RESPONSE_TEXT_SUCCESS);
                        } else {
                            function.onCallBack(RESPONSE_TEXT_FAIL);
                        }
                    } else if ("alipay".equals(type)) {
                        AliPayManager.getInstance().payV2(WebAppActivity.this, params);
                        function.onCallBack(RESPONSE_TEXT_SUCCESS);
                    }
                } catch (JSONException e) {
                    function.onCallBack(RESPONSE_TEXT_FAIL);

                }
            }
        });

        webView.registerHandler("setStorageSync", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                Log.i("chenyi", "setStorageSync: " + responseData);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", responseData);
                editor.apply();
            }
        });

        webView.registerHandler("outlogin", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                Log.i("chenyi", "outlogin: " + responseData);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
            }
        });

        webView.registerHandler("getStorageSync", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                Log.i("chenyi", "getStorageSync: " + responseData);
                if (preferences.getString("token", "").equals("")) {
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                } else {
                    String result = "{\"statusCode\":\"0\", \"data\":" + preferences.getString("token", "") + "}";
                    Log.i("chenyi", "getStorageSync: " + result);
                    function.onCallBack(result);
                }
            }
        });

        webView.registerHandler("setLat", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(responseData);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    String id = wxJson.getString("id");
                    String params = wxJson.getString("params");
                    Intent intent = new Intent(WebAppActivity.this, MapActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", 0);
                    startActivity(intent);
                    Log.i("chenyi", params + "------" + id);
                    function.onCallBack(RESPONSE_TEXT_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });
        webView.registerHandler("setStore", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(responseData);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    String id = wxJson.getString("id");
                    Log.i("chenyi", "id == " + id);
                    String params = wxJson.getString("params");
                    Log.i("chenyi", "params == " + params);
                    Intent intent = new Intent(WebAppActivity.this, MapActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    function.onCallBack(RESPONSE_TEXT_SUCCESS);
                } catch (JSONException e) {
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });
        webView.registerHandler("setImg", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(responseData);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    Log.i("chenyi", "asdadas");
                    function.onCallBack(RESPONSE_TEXT_SUCCESS);
                } catch (JSONException e) {
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });
        webView.registerHandler("setDisease", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    JSONTokener jsonTokener = new JSONTokener(responseData);
                    JSONObject wxJson = (JSONObject) jsonTokener.nextValue();
                    String url = wxJson.getString("url");
                    Log.i("chenyi", url + "asdasdads");
                    startActivityForResult(new Intent(WebAppActivity.this, MainActivity.class), 0);
                    function.onCallBack(RESPONSE_TEXT_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });
        webView.registerHandler("uploadImg", (responseData, function) -> {
//            try {
            Log.i("chenyi", "handler: uploadImg");
//                ActionSheet.createBuilder(WebAppActivity.this, getSupportFragmentManager())
//                        .setCancelButtonTitle("Cancel")
//                        .setOtherButtonTitles("相机", "图库")
//                        .setCancelableOnTouchOutside(true)
//                        .setListener(new ActionSheet.ActionSheetListener() {
//                            @Override
//                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
//                            }
//
//                            @Override
//                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                                if (index == 0) {
//                                    camera();
//                                    Log.i("chenyi", "camera");
//                                } else {
//                                    selectImage();
//                                    Log.i("chenyi", "selectImage");
//                                }
//                            }
//                        }).show();
//
//                function.onCallBack(RESPONSE_TEXT_SUCCESS);
//            } catch (Exception e) {
//                e.printStackTrace();
//                function.onCallBack(RESPONSE_TEXT_FAIL);
//            }
            showType();
        });
        webView.registerHandler("backHistory", new BridgeHandler() {
            @Override
            public void handler(String responseData, CallBackFunction function) {
                try {
                    Log.i("chenyi", "backHistory");
                    finish();
                    function.onCallBack(RESPONSE_TEXT_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    function.onCallBack(RESPONSE_TEXT_FAIL);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMoonEvent(WXLoginMessage loginMessage) {
        Log.i("chenyi", "onMoonEvent WXLoginMessage " + loginMessage.loginCode);
        WXLoginManager manager = WXLoginManager.getInstance(this);
        try {
            //拉起微信授权后通过code向自己服务器获取accessToken
            AccessToken token = manager.requestAccessToken(loginMessage.loginCode);
            Log.i("chenyi", "token == " + token);
            if (token == null || TextUtils.isEmpty(token.getAccess_token())) {
                sendLoginError();
                return;
            }
            Log.i("chenyi", "onMoonEvent WXLoginMessage AccessToken " + token);
            mSavedAccessToken = token;
            //服务器验证accessToken返回
            String userinfo = manager.requestUserInfo(mSavedAccessToken.getAccess_token(), mSavedAccessToken.getOpenid());
            Log.i("chenyi", "onMoonEvent WXLoginMessage userinfo " + userinfo);
            if (!TextUtils.isEmpty(userinfo)) {
                WXLoginInfoMessage msg = new WXLoginInfoMessage();
                msg.status = 0;
                msg.data = userinfo;
                EventBus.getDefault().post(msg);

                String expiresStr = mSavedAccessToken.getExpires_in();
                if (!TextUtils.isEmpty(expiresStr)) {
                    Integer expires = Integer.valueOf(expiresStr);
                    if (expires > 60) {
                        expires /= 2;
                    } else {
                        expires = 60;
                    }
                    Log.i("chenyi", "start time " + expires);
                    timer.schedule(task, expires * 1000, expires * 1000);
                }
            } else {
                sendLoginError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendLoginError();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(WXLoginInfoMessage loginInfoMessage) {
        String info = loginInfoMessage.getJsonString();
        Log.i("chenyi", "onMoonEvent WXLoginInfoMessage " + info);
        webView.callHandler("authLogin", info, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.i("chenyi", "wxLogin error result " + data);
            }
        });

    }

    private void sendLoginError() {
        Log.i("WebAppActivity", "sendLoginError");
        WXLoginInfoMessage msg = new WXLoginInfoMessage();
        msg.status = -1;
        msg.data = "";
        EventBus.getDefault().post(msg);
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            if (mSavedAccessToken == null) return;
            try {
                RefreshToken refreshToken = WXLoginManager.getInstance(WebAppActivity.this).requestRefreshToken(mSavedAccessToken.getRefresh_token());
                Log.i("WebAppActivity", "refreshToken " + refreshToken);
                if (refreshToken != null && refreshToken.getAccess_token() != null) {
                    mSavedAccessToken = new AccessToken(refreshToken);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //EventBus阿里支付结果回调事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AliPayMessage payMessage) {
        String payString = payMessage.getJsonString();
        Log.i("chenyi", "onMoonEvent AliPayMessage " + payString);

        webView.callHandler("payment", payString, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.i("chenyi", "callHandler AliPayMessage result " + data);
            }
        });
        //webview.send(payString);
    }

    //EventBus微信支付结果回调事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(WXPayMessage payMessage) {
        String payString = payMessage.getJsonString();
        Log.i("chenyi", "onMoonEvent WXPayMessage " + payString);
        //java调用js，通知服务端支付完成
        webView.callHandler("payment", payString, new CallBackFunction() {
            @Override
            public void onCallBack(String jsResponseData) {
                Log.i("chenyi", "callHandler WXPayMessage result " + jsResponseData);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String payMessage) {
        webView.callHandler("getLocation", payMessage, jsResponseData -> {
            Log.i("chenyi", "callHandler getLocation result " + jsResponseData);
        });
    }

    /**
     * 上传用户头像
     */
    private void upDataHeadImg() {
//        try {
//            Log.i("chenyi", "head");
//            RequestManager.getInstance(WebAppActivity.this).getAva(WebAppActivity.this, picFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (updateDialog == null) {
            updateDialog = ProgressDialog.show(WebAppActivity.this, "上传头像", "头像正在上传中，请稍等...", true, false);
        }
        Log.i("chenyi", "upDataHeadImg: " + Utils.bitmaptoString(bmp));
        String test = Utils.bitmaptoString(bmp);
        HttpJsonMethod.getInstance().getAva(
                new ProgressErrorSubscriber<>(uploadOnNext, WebAppActivity.this), Utils.bitmaptoString(bmp));
    }

    public void camera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(WebAppActivity.this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WebAppActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                //上面已经写好的拍照方法
                write(true);
            }
        } else {
            //上面已经写好的拍照方法
            write(true);
        }
    }

    public void write(boolean iscamera) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(WebAppActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WebAppActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                return;
            } else {
                if (iscamera) {
                    //上面已经写好的拍照方法
                    takePhoto();
                } else {
                    selectImage();
                }
            }
        } else {
            if (iscamera) {
                //上面已经写好的拍照方法
                takePhoto();
            } else {
                selectImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    Log.d("chenyi", "100");
                    if (data != null) {
                        Uri uri = data.getData();
                        ContentResolver cr = this.getContentResolver();
                        try {
                            bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));

                            Matrix matrix = new Matrix();
                            matrix.setScale(0.5f, 0.5f);
                            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                                    bmp.getHeight(), matrix, true);
                            upDataHeadImg();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case 101:
                    // 从拍照返回
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        bmp = (Bitmap) bundle.get("data");
                        Matrix matrix = new Matrix();
                        matrix.setScale(0.5f, 0.5f);
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                                bmp.getHeight(), matrix, true);
                        upDataHeadImg();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void deletePic() {
        if (picFile.exists()) {
            picFile.delete();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        createPicFile();
        try {
            // 选择拍照
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 指定调用相机拍照后照片的储存路径
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(picFile));
            startActivityForResult(cameraintent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从相册选择
     */
    private void selectImage() {
        createPicFile();
        Intent intent;
        if (Build.VERSION.SDK_INT >= 23) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }

//        intent.putExtra("output", Uri.fromFile(picFile));
////        intent.putExtra("crop", "true");
////        intent.putExtra("aspectX", 1);// 裁剪框比例
////        intent.putExtra("aspectY", 1);
////        intent.putExtra("outputX", 480);// 输出图片大小
////        intent.putExtra("outputY", 480);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
        if (isIntentAvailable(WebAppActivity.this, intent)) {
            startActivityForResult(Intent.createChooser(intent, "选择图片"), 100);
        } else {
            Toast.makeText(WebAppActivity.this, "请安装相关图片查看应用。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拍照后图片裁减
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    /**
     * 创建上传图片文件
     */
    private void createPicFile() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(WebAppActivity.this, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory().toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        picFile = new File(file
                + "/seawaterHeadImg.jpg");
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        // 设置定位数据
        //mBaiduMap.setMyLocationData(locData);
        lat = bdLocation.getLatitude();
        lon = bdLocation.getLongitude();
        myLoc = bdLocation.getBuildingName();
        if (isFirstLocate) {
            isFirstLocate = false;
            String request = "{\"lat\":\"" + lat + "\",\"lng\":\"" + lon + "\"}";
            Log.i("chenyi", request);

            JSONObject newJson = new JSONObject();
            JSONObject innerJson = new JSONObject();
            try {
                newJson.put("statusCode", "1");
                innerJson.put("lat", lat + "");
                innerJson.put("lng", lon + "");
                newJson.put("data", innerJson);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(newJson.toString());
//            webView.callHandler("getLocation", newJson.toString(), jsResponseData ->
//                    Log.i("chenyi", "callHandler getLocation result " + jsResponseData));
            mLocationClient.stop();
            isFirstLocate = true;
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onMapLoaded() {

    }
    /*
    * Tencent
    * */
    /*@Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_LOGIN) {

            if (resultCode == -1) {

                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);

                Tencent.handleResultData(data, loginListener);

                UserInfo info = new UserInfo(this, mTencent.getQQToken());

                info.getUserInfo(new IUiListener() {

                    @Override

                    public void onComplete(Object o) {

                        try {

                            JSONObject info = (JSONObject) o;

                            String nickName = info.getString("nickname");//获取用户昵称

                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url

                            Toast.makeText(MainActivity.this,"昵称："+nickName, Toast.LENGTH_SHORT).show();

                            Glide.with(MainActivity.this).load(iconUrl).transform(new GlideRoundTransform(MainActivity.this)).into(icon);//Glide解析获取用户头像

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }



                    @Override

                    public void onError(UiError uiError) {



                    }



                    @Override

                    public void onCancel() {



                    }

                });

            }

        }

    }*/

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("涂材直聘邀请函");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("快来下载吧！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(fileName);//确保SDcard下面存在此张图片
//        oks.setImagePath("file:///android_asset/logo.jpg");//确保SDcard下面存在此张图片
//        oks.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503506356816&di=5dadbd01e162deb6601a801dc6258361&imgtype=0&src=http%3A%2F%2Fimg1.bitautoimg.com%2Fautoalbum%2Ffiles%2F20170407%2F958%2F16325395873602_5454777_3.jpg%3Fr%3D20170703");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.example.user.guokun");
//        oks.setUrl("http://www.baidu.com");
        // 启动分享GUI
        oks.show(this);
    }

    @AfterPermissionGranted(RC_STORAGE_CONTACTS_PERM)
    public void saveImg() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_locate),
                    RC_STORAGE_CONTACTS_PERM, perms);
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void showType() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Dialog dialog = new Dialog(this, R.style.BottomDialog);
            inflate = LayoutInflater.from(this).inflate(R.layout.pop_avatar, null);
            Button camera = (Button) inflate.findViewById(R.id.camera);
            Button img_lib = (Button) inflate.findViewById(R.id.img_lib);
            Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);
            camera.setOnClickListener(v -> {
                dialog.dismiss();
                camera();
            });
            img_lib.setOnClickListener(v -> {
                selectImage();
                dialog.dismiss();
            });
            cancel.setOnClickListener(v -> dialog.dismiss());
            dialog.setContentView(inflate);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;
            lp.width = -1;
            dialogWindow.setAttributes(lp);
            dialog.show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.permition),
                    RC_LOCATION_CONTACTS_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("chenyi", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("chenyi", "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
