package tucai.wshoto.com.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import tucai.wshoto.com.R;
import tucai.wshoto.com.widget.MyImage;
import tucai.wshoto.com.widget.TakePhotoPopWin;

/**
 * Created by Weshine on 2017/4/20.
 */

public class BodyFragment extends Fragment{
    private int i = -1;
    private ImageView iv_updown;
    private boolean gender_man = true;
    private MyImage iv1,iv2,iv3,iv4;
    private FrameLayout root;
    private RadioButton rb1,rb2;
    private RadioGroup rg;
    private LinearLayout ll_age;
    private LinearLayout ll_content;
    private TextView tv_age;
    private TakePhotoPopWin takePhotoPopWin;
    //年龄默认值，从sf中取
    private int age = 0;
    private ScaleAnimation sato0 = new ScaleAnimation(1,0,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);

    private ScaleAnimation sato1 = new ScaleAnimation(0,1,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body,null);
        ButterKnife.bind(this,view);//绑定framgent
        EventBus.getDefault().register(this);
        SharedPreferences sf = getActivity().getSharedPreferences("age", Context.MODE_PRIVATE);
        age = sf.getInt("age",20);
        Log.d("wjj","age == "+age);
        initView(view);
        initClick();
        initAnim();
        initData();
        initHttp();
        return view;
    }

    private void initHttp() {

    }

    private void initData() {
        tv_age.setText(age+"岁");
    }

    public void showPopFormBottom(View view) {

//        设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(ll_content, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getActivity().getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha=0.7f;

        getActivity().getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getActivity().getWindow().getAttributes();
                params[0].alpha=1f;
                getActivity().getWindow().setAttributes(params[0]);
                SharedPreferences sf = getActivity().getSharedPreferences("age",Context.MODE_PRIVATE);
                age = sf.getInt("age",20);
                tv_age.setText(age+"岁");
            }
        });

//        takePhotoPopWin.lis
    }

    private void initView(View view) {
        iv_updown = (ImageView) view.findViewById(R.id.iv_updown);
        iv1 = (MyImage) view.findViewById(R.id.iv1);
        iv2 = (MyImage) view.findViewById(R.id.iv2);
        iv3 = (MyImage) view.findViewById(R.id.iv3);
        iv4 = (MyImage) view.findViewById(R.id.iv4);
        root = (FrameLayout) view.findViewById(R.id.root);
        rb1 = (RadioButton) view.findViewById(R.id.rb1);
        rb2 = (RadioButton) view.findViewById(R.id.rb2);
        rg = (RadioGroup) view.findViewById(R.id.rg);
        ll_age = (LinearLayout) view.findViewById(R.id.ll_age);
        ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
        tv_age = (TextView) view.findViewById(R.id.tv_a);
        takePhotoPopWin = new TakePhotoPopWin(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initAnim() {
        sato0.setDuration(500);
        sato1.setDuration(500);
        sato0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rb1.isChecked()) {
                    if (iv1.getVisibility() == View.VISIBLE) {
                        iv1.setAnimation(null);
                        showImage2();
                        iv2.startAnimation(sato1);
                    } else {
                        iv2.setAnimation(null);
                        showImage1();
                        iv1.startAnimation(sato1);
                    }
                }else{
                    if (iv3.getVisibility() == View.VISIBLE) {
                        iv3.setAnimation(null);
                        showImage4();
                        iv4.startAnimation(sato1);
                    } else {
                        iv4.setAnimation(null);
                        showImage3();
                        iv3.startAnimation(sato1);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void showImage1(){
        iv1.setVisibility(View.VISIBLE);
        iv1.setFront(true);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }
    private void showImage2(){
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.VISIBLE);
        iv2.setFront(false);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
    }
    private void showImage3(){
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.VISIBLE);
        iv3.setFront(true);
        iv4.setVisibility(View.GONE);
    }
    private void showImage4(){
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.VISIBLE);
        iv4.setFront(false);
    }
    private void initClick() {
        iv_updown.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                //翻转
                if (rb1.isChecked()) {
                    if (iv1.getVisibility() == View.VISIBLE) {
                        iv1.startAnimation(sato0);
                    } else {
                        iv2.startAnimation(sato0);
                    }
                }else{
                    if (iv3.getVisibility() == View.VISIBLE) {
                        iv3.startAnimation(sato0);
                    } else {
                        iv4.startAnimation(sato0);
                    }
                }
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==rb1.getId()){
                    showImage1();
                }else{
                    showImage3();
                }
            }
        });
        ll_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMoonEvent(Integer i){
        this.i = i;
        switch (i){
            case -1:
                Toast.makeText(getActivity(),"点击非人体图区域",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(),"头部",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 2:
                Toast.makeText(getActivity(),"颈部",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 3:
                Toast.makeText(getActivity(),"胸部",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 4:
                Toast.makeText(getActivity(),"背部",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 5:
                Toast.makeText(getActivity(),"腰部",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 6:
                Toast.makeText(getActivity(),"生殖",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 7:
                Toast.makeText(getActivity(),"四肢",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
            case 8:
                //Toast.makeText(getActivity(),"四肢",Toast.LENGTH_SHORT).show();
                jumpToList();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void jumpToList(){
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                EventBus.getDefault().post(i+"");
            }
        }, 1000);
    }

}
