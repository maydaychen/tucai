package tucai.wshoto.com.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import tucai.wshoto.com.R;
import tucai.wshoto.com.adapter.MyBaseAdapter;
import tucai.wshoto.com.constants.Constants;



public class TakePhotoPopWin extends PopupWindow {

    private Context mContext;

    private View view;
    public int age;
    private LinearLayout btn_cancel;
    private ListView lv;

    public TakePhotoPopWin(final Context mContext, View.OnClickListener itemsOnClick) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_wheel, null);
        final SharedPreferences sf = mContext.getSharedPreferences("age",Context.MODE_PRIVATE);
        age = sf.getInt("age",20);
        lv = (ListView) view.findViewById(R.id.lv);
        lv.setAdapter(new MyBaseAdapter(mContext,R.layout.item_age,Constants.list_age));
        lv.requestFocusFromTouch();
        lv.setSelection(age);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    age = position+1;
                    SharedPreferences.Editor et = sf.edit();
                    et.putInt("age",position+1);
                    et.commit();
                    lv.setSelection(age);
                    dismiss();
                Log.d("wjj","itemclick == "+age);
            }
        });
        btn_cancel = (LinearLayout) view.findViewById(R.id.btn_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
    public int getAge(){
        return age;
    }
}