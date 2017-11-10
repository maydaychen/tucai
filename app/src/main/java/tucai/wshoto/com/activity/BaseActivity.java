package tucai.wshoto.com.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

/**
 * Created by Weshine on 2017/4/20.
 */

public class BaseActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this);
    }
}
