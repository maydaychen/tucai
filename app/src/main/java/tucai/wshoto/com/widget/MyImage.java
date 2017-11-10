package tucai.wshoto.com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import tucai.wshoto.com.bean.MyBean;
import tucai.wshoto.com.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjj on 2017/4/19.
 */
@SuppressLint("AppCompatCustomView")
public class MyImage extends ImageView {

    private DisplayMetrics dm;

    private int bodyImageViewHeight = 0;
    private int bodyImageViewWidth = 0;
    //部位反馈值
    private int p_body = -1;
    //红点点击效果
    private List<MyBean> list = null;
    private int MaxAlpha = 255;
    private boolean START = true;
    private boolean isFront = true;
    public MyImage(Context context) {
        super(context);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        list = new ArrayList<MyBean>();
        dm = context.getResources().getDisplayMetrics();
    }
    public void setFront(boolean isFront){
        this.isFront = isFront;
    }
    public boolean getFront(){
        return isFront;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        //初始化各个部位的范围
        initParametersForRegion();

    //end
        if(isTouchPointInTransparent(x, y)) {
            Log.e("mcoy", "the point is in transparent area now");
        } else {
             /*
        * 红点效果
        * */
            MyBean bean = new MyBean();
            bean.radius = 0;
            bean.alpha = MaxAlpha;
            bean.width = bean.radius / 8;
            bean.X = (int) event.getX();
            bean.Y = (int) event.getY();
            bean.paint = initPaint(bean.alpha, bean.width);

            if (list.size() == 0) {
                START = true;
            }
            list.add(bean);
            invalidate();
            if (START) {
                handler.sendEmptyMessage(0);
            }
            positionType position = pointToPosition(x, y);
            Log.e("mcoy", "the position is " + position);
            switch (position){
                case REGION_FRONT_HEAD:
                    p_body = 1;
                    break;
                case REGION_FRONT_NECK:
                    p_body = 2;
                    break;
                case REGION_FRONT_CHEST:
                    p_body = 3;
                    break;
                case REGION_FRONT_BACK:
                    p_body = 4;
                    break;
                case REGION_FRONT_WAIST:
                    p_body = 5;
                    break;
                case REGION_FRONT_GENITALS:
                    p_body = 6;
                    break;
                case REGION_FRONT_LEG:
                    p_body = 7;
                    break;
                case REGION_FRONT_HAND:
                    p_body = 7;
                    break;
            }
            EventBus.getDefault().post(p_body);
        }
        return super.onTouchEvent(event);
    }
    private enum positionType{
        REGION_FRONT_HAND,
        REGION_FRONT_HEAD,
        REGION_FRONT_NECK,
        REGION_FRONT_CHEST,
        REGION_FRONT_WAIST,
        REGION_FRONT_GENITALS,
        REGION_FRONT_LEG,
        REGION_FRONT_BACK
    }

    /**
     * 通过drawable的颜色先把透明区域排除，剩下人体区域,手部范围用左手x1与右手x2确定，头部范围由头胸腰部都由各自y值确定，剩余部位皆为腿
     * @param x
     * @param y
     * @return
     */
    private positionType pointToPosition(int x, int y) {
        if(x < mHandX1 || x > mHandX2)
            return positionType.REGION_FRONT_HAND;
        else if (y < mHeadY)
            return positionType.REGION_FRONT_HEAD;
        else if(y < mNeckY)
            return positionType.REGION_FRONT_NECK;
        else if(y < mChestY)
            if(isFront){
                return positionType.REGION_FRONT_CHEST;
            }else{
                return positionType.REGION_FRONT_BACK;
            }
        else if(y < mWaistY)
            return positionType.REGION_FRONT_WAIST;
        else if(y < genitalsY)
            return positionType.REGION_FRONT_GENITALS;
        else
            return positionType.REGION_FRONT_LEG;
    }

    /**
     *
     * @param x
     * @param y
     * @return 判断点击区域是否在透明区域
     */
    private boolean isTouchPointInTransparent(int x, int y) {

        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();

        int imageHeight = this.getHeight();
        int imageWidth = this.getWidth();

        Drawable drawable = this.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        int intrinsicHeight = drawable.getIntrinsicHeight();
        int intrinsicWidth = drawable.getIntrinsicWidth();

        int locationInBitmapX = (x - paddingLeft) * intrinsicWidth / imageWidth;
        int locationInBitmapY = (y - paddingTop) * intrinsicHeight / imageHeight;

        int pixel = bitmap.getPixel(locationInBitmapX, locationInBitmapY);

        Log.e("mcoy", "x is " + x);
        Log.e("mcoy", "y is " + y);
        Log.e("mcoy", "imageHeight is " + imageHeight);
        Log.e("mcoy", "imageWidth is " + imageWidth);
        Log.e("mcoy", "intrinsicHeight is " + intrinsicHeight);
        Log.e("mcoy", "intrinsicWidth is " + intrinsicWidth);
        Log.e("mcoy", "locationInBitmapX is " + locationInBitmapX);
        Log.e("mcoy", "locationInBitmapY is " + locationInBitmapY);
        Log.e("mcoy", "actualBitmapX is " + locationInBitmapX / dm.density);
        Log.e("mcoy", "actualBitmapY is " + locationInBitmapY / dm.density);
        Log.e("mcoy", "pixel is " + pixel);

        return pixel == 0;
    }

    private int mHeadY;
    private int mHandX1;
    private int mHandX2;
    private int mChestY;
    private int mWaistY;
    private int mNeckY;
    private int genitalsY;

    //以下数据需要同UI同事沟通好每个部位定义的范围，或者自己手动量一下
    private final int HEAD_AREA = 120;
    private final int LEFT_HAND_AREA = 78;
    private final int RIGHT_HAND_AREA = 249;
    private final int NECK_AREA = 159;
    private final int CHEST_AREA = 270;
    private final int WAIST_AREA = 439;
    private final int GENITALS_AREA = 502;
    private void initParametersForRegion() {

        if(bodyImageViewHeight != this.getHeight()) {

            bodyImageViewHeight = this.getHeight();
            bodyImageViewWidth = this.getWidth();
            int imageIntrinsicHeight = this.getDrawable().getIntrinsicHeight();
            int imageIntrinsicWidht = this.getDrawable().getIntrinsicWidth();
            Log.e("danny", "bodyImageViewHeight is " + bodyImageViewHeight);
            Log.e("danny", "bodyImageViewWidth is " + bodyImageViewWidth);
            Log.e("danny", "imageIntrinsicHeight is " + imageIntrinsicHeight);
            Log.e("danny", "imageIntrinsicWidht is " + imageIntrinsicWidht);
            //头部范围*imageview高度/图片资源高度
            mHeadY = DensityUtil.dip2px(getContext(), HEAD_AREA) * bodyImageViewHeight / imageIntrinsicHeight
                    + this.getPaddingTop();
            mNeckY = DensityUtil.dip2px(getContext(), NECK_AREA) * bodyImageViewHeight / imageIntrinsicHeight
                    + this.getPaddingTop();
            mHandX1 = DensityUtil.dip2px(getContext(), LEFT_HAND_AREA) * bodyImageViewWidth / imageIntrinsicWidht
                    + this.getPaddingLeft();
            mHandX2 = DensityUtil.dip2px(getContext(), RIGHT_HAND_AREA) * bodyImageViewWidth / imageIntrinsicWidht
                    + this.getPaddingLeft();
            mChestY = DensityUtil.dip2px(getContext(), CHEST_AREA) * bodyImageViewHeight / imageIntrinsicHeight
                    + this.getPaddingTop();
            mWaistY = DensityUtil.dip2px(getContext(), WAIST_AREA) * bodyImageViewHeight / imageIntrinsicHeight
                    + this.getPaddingTop();
            genitalsY = DensityUtil.dip2px(getContext(), GENITALS_AREA) * bodyImageViewHeight / imageIntrinsicHeight
                    + this.getPaddingTop();
            Log.e("danny", "mHeadY is " + mHeadY);
            Log.e("danny", "mHandX1 is " + mHandX1);
            Log.e("danny", "mHandX2 is " + mHandX2);
            Log.e("danny", "mChestY is " + mChestY);
            Log.e("danny", "mWaistY is " + mWaistY);
            Log.e("danny", "mWaistY is " + mNeckY);
            Log.e("danny", "mWaistY is " + genitalsY);
        }
    }
    /**
     * 以下代码为添加红点点击效果
     */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int i = 0; i < list.size(); i++) {
                MyBean wave = list.get(i);
                canvas.drawCircle(wave.X, wave.Y, wave.radius, wave.paint);
            }
    }
    /**
     *
     */
    private Paint initPaint(int alpha, float width) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(alpha);
        paint.setColor(Color.RED);
        return paint;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Refresh();
                    invalidate();
                    if (list != null && list.size() > 0) {
                        handler.sendEmptyMessageDelayed(0, 50);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    /***
     *
     */
    private void Refresh() {
        for (int i = 0; i < list.size(); i++) {
            MyBean bean = list.get(i);
            if (START == false && bean.alpha == 0) {
                list.remove(i);
                bean.paint = null;
                bean = null;
                continue;
            } else if (START == true) {
                START = false;
            }
            bean.radius += 2;
            bean.alpha -= 10;
            if (bean.alpha < 0) {
                bean.alpha = 0;
            }
            bean.width = bean.radius / 8;
            bean.paint.setAlpha(bean.alpha);
            bean.paint.setStrokeWidth(bean.width);
        }
    }

}
