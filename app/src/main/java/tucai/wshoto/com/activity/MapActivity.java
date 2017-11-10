package tucai.wshoto.com.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tucai.wshoto.com.R;
import tucai.wshoto.com.map.BNDemoGuideActivity;
import tucai.wshoto.com.request.RequestManager;
import tucai.wshoto.com.response.HospitalResponse;
import tucai.wshoto.com.response.StoreBean;
import tucai.wshoto.com.test.Info;
import tucai.wshoto.com.utils.GlideCircleTransform;
import tucai.wshoto.com.utils.clusterutil.clustering.ClusterItem;
import tucai.wshoto.com.utils.clusterutil.clustering.ClusterManager;


/**
 * Created by Weshine on 2017/4/19.
 */

public class MapActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback,BDLocationListener {
    MapView mapView;
    static BaiduMap mBaiduMap;
    MapStatus ms;
    PoiSearch mPoiSearch;
    private LinearLayout ll_nv;
    private InfoWindow mInfoWindow;
    private LinearLayout ll;
    private boolean isFirstLocate = true;
    private ClusterManager<MyItem> mClusterManager;
    private LocationClient mLocationClient;
    private LatLng myLl;
    private TextView tv_name,tv_dis,tv_qu;
    HospitalResponse.MessageBean result;
    StoreBean.MessageBean result2;
    private String id;
    private Button mDb06ll = null;
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private double lat;
    private double lon;
    private String myLoc;
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private static final String[] authBaseArr = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION };
    private static final String[] authComArr = { Manifest.permission.READ_PHONE_STATE };
    private static final int authBaseRequestCode = 1;
    private static final int authComRequestCode = 2;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    String authinfo = null;
    private ImageView iv_r;
    int type = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",0);
        mapView = (MapView) findViewById(R.id.bmapView);
        iv_r = (ImageView) findViewById(R.id.iv_r);
        //ms = new MapStatus.Builder().target(new LatLng(39.914935, 116.403119)).zoom(8).build();
        ll = (LinearLayout) findViewById(R.id.ll);
        ll_nv = (LinearLayout) findViewById(R.id.ll_nv);
        mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocationClient.registerLocationListener(this);//注册监听函数
        initLocation();
        // 开启定位图层
        mBaiduMap = mapView.getMap();
        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
        initHttp(id);
        mLocationClient.start();//开启定位
        //initCluster();
       // BaiduNaviManager.getInstance().initEngine();
        initView();
        initClicker();
        initNv();
    }

    private void initNv() {
        if (initDirs()) {
            initNavi();
        }
    }
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MapActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //Toast.makeText(MapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                //Toast.makeText(MapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
               // Toast.makeText(MapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                //Toast.makeText(MapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };
    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9542360");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private BNRoutePlanNode.CoordinateType mCoordinateType = null;
    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(MapActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(MapActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(lon, lat, "myLoc", null, coType);
                if(type == 0){
                    eNode = new BNRoutePlanNode(Double.parseDouble(result.getLng()), Double.parseDouble(result.getLat()), result.getName(), null, coType);
                }else{
                    eNode = new BNRoutePlanNode(Double.parseDouble(result2.getLng()), Double.parseDouble(result2.getLat()), result2.getTitle(), null, coType);
                }
                break;
            }
            default:
                ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            Toast.makeText(MapActivity.this,"路线计算中...",Toast.LENGTH_SHORT).show();
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new MapActivity.DemoRoutePlanListener(sNode));
        }
    }
    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            Intent intent = new Intent(MapActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            //Toast.makeText(MapActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void initClicker() {
        ll_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
                navigate();
        }
    });
        mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                    ll.setVisibility(View.GONE);
                    iv_r.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void navigate() {
        if (BaiduNaviManager.isNaviInited()) {
            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
        }
    }


    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_dis = (TextView) findViewById(R.id.tv_dis);
    }

    public void addInfosOverlay(List<Info> infos)
    {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (int i =0;i<infos.size();i++)
        {
// 位置
            latLng = new LatLng(infos.get(i).getLatitude(), infos.get(i).getLongitude());
// 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_gcoding)).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", infos.get(i));
            marker.setExtraInfo(bundle);
        }
// 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                button.setText("测试");
                button.setTextColor(Color.BLACK);
                button.setWidth(300);
                Info info = (Info) marker.getExtraInfo().get("info");
                tv_name.setText(info.getName());
                showLayout(true);
                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        mBaiduMap.hideInfoWindow();
                        showLayout(false);
                    }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                //mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }
    private void initHttp(String id) {
        RequestManager request = RequestManager.getInstance(MapActivity.this);
        try {
            Log.d("wjj","http2");
            if(type == 0) {
                request.getMap(MapActivity.this, id);
            }else{
                request.getMap2(MapActivity.this, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getResult(HospitalResponse.MessageBean result){
        this.result = result;
        Info.infos.clear();
        String name = result.getName();
        Log.d("wjj","name == "+name);
        Log.d("wjj","lat == "+result.getLat());
        Log.d("wjj","lon == "+result.getLng());
        Log.d("wjj","thumb == "+result.getThumb());
        String thumb = result.getThumb();
        Double lat = Double.parseDouble(result.getLat());
        Double lon = Double.parseDouble(result.getLng());
        tv_name.setText(name);
        tv_dis.setText(result.getDescription());
        Glide.with(MapActivity.this).load(thumb).transform(new GlideCircleTransform(MapActivity.this)).into(iv_r);
        //Toast.makeText(MapActivity.this,name,Toast.LENGTH_SHORT).show();
        Info info = new Info(lat,lon,0,name,"",0);
        Info.infos.add(info);
        addInfosOverlay(Info.infos);
    }
    public void getResult2(StoreBean.MessageBean result){
        this.result2 = result;
        Info.infos.clear();
        String name = result.getTitle();
        Log.d("wjj","name == "+name);
        Log.d("wjj","thumb == "+result.getThumb());
        String thumb = result.getThumb();
        tv_name.setText(name);
        tv_dis.setText(result.getContent());
        Log.d("wjj","lat == "+result.getLat());
        Log.d("wjj","lon == "+result.getLng());
        Double lat = Double.parseDouble(result.getLat());
        Double lon = Double.parseDouble(result.getLng());
        Glide.with(MapActivity.this).load(thumb).transform(new GlideCircleTransform(MapActivity.this)).into(iv_r);
        //Toast.makeText(MapActivity.this,name,Toast.LENGTH_SHORT).show();
        Info info = new Info(lat,lon,0,name,"",0);
        Info.infos.add(info);
        addInfosOverlay(Info.infos);
    }
    private void showLayout(boolean show){
        if(show) {
            ll.setVisibility(View.VISIBLE);
            iv_r.setVisibility(View.VISIBLE);
        }else{
            ll.setVisibility(View.GONE);
            iv_r.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mLocationClient.stop();
        mBaiduMap.clear();
        mBaiduMap = null;
        mapView.onDestroy();
        mapView = null;
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    @Override
    public void onReceiveLocation(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        //mBaiduMap.setMyLocationData(locData);
        lat = location.getLatitude();
        lon = location.getLongitude();
        myLoc = location.getBuildingName();
        if (isFirstLocate) {
            myLl = new LatLng(location.getLatitude(), location.getLongitude());
         /*   MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(myLl, 16);//设置地图中心及缩放级别
            mBaiduMap.animateMapStatus(update);*/
            isFirstLocate = false;
            Log.d("wjj","http");

        }

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private  LatLng mPosition;

        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }
        public void setPosition(LatLng latLng){
            mPosition = latLng;
        }
        @Override
        public LatLng getPosition() {
            return mPosition;
        }
        //
        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            Bitmap bitmap1 = drawbitmap();
            Bitmap bitmap2 = drawtext(bitmap1,"测试");
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
         /*   return BitmapDescriptorFactory
                    .fromBitmap(bitmap2);*/
        }
    }
    private Bitmap drawbitmap() {
        // TODO Auto-generated method stub
        Bitmap photo = BitmapFactory.decodeResource(this.getResources(),R.drawable.icon_gcoding);
        int width = photo.getWidth();
        int hight = photo.getHeight();
        Bitmap newb = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);// 初始化和方框一样大小的位图
        Paint photoPaint = new Paint(); // 建立画笔
        canvas.drawBitmap(photo, 0, 0, photoPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newb;
    }
    private Bitmap drawtext(Bitmap bitmap3,String info) {
        // TODO Auto-generated method stub
        int width = bitmap3.getWidth(), hight = bitmap3.getHeight();
        Bitmap btm= Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(btm);
        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些
        Rect src = new Rect(0, 0, bitmap3.getWidth(), bitmap3.getHeight());//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(bitmap3, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(30.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.BLACK);//采用的颜色
        canvas.drawText(info, 32, 32, textPaint);//绘制上去字，中间参数为坐标点
        canvas.save(Canvas.ALL_SAVE_FLAG); //保存
        canvas.restore();
        return btm;
    }
   /* *//**
     * 向地图添加Marker点
     *//*
    public void addMarkers() {
        // 添加Marker点
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);
        LatLng llE = new LatLng(39.956965, 116.331394);
        LatLng llF = new LatLng(39.886965, 116.441394);
        LatLng llG = new LatLng(39.996965, 116.411394);
        List<MyItem> items = new ArrayList<MyItem>();
        items.add(new MyItem(llA));
        items.add(new MyItem(llB));
        items.add(new MyItem(llC));
        items.add(new MyItem(llD));
        items.add(new MyItem(llE));
        items.add(new MyItem(llF));
        items.add(new MyItem(llG));
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        InfoWindow.OnInfoWindowClickListener listener = null;
        button.setText("测试");
        button.setTextColor(Color.BLACK);
        button.setWidth(300);
        //showLayout(true);
        listener = new InfoWindow.OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();
                showLayout(false);
            }
        };
        LatLng ll = items.get(0).getPosition();
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
        mClusterManager.addItems(items);

    }*/
    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

}
