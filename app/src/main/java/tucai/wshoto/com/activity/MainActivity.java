package tucai.wshoto.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bingoogolapple.titlebar.BGATitlebar;
import tucai.wshoto.com.R;
import tucai.wshoto.com.bean.Disease;
import tucai.wshoto.com.constants.Constants;
import tucai.wshoto.com.fragment.BodyFragment;
import tucai.wshoto.com.fragment.ListFragment;
import tucai.wshoto.com.request.RequestManager;
import tucai.wshoto.com.response.BodyResponse;


public class MainActivity extends AutoLayoutActivity {
    ViewPager viewPager;
    SlidingTabLayout st;
    MyPagerAdapter mAdapter;
    private BGATitlebar titlebar;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
           "人体图","症状列表"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        viewPager = (ViewPager) findViewById(R.id.vp);
        st = (SlidingTabLayout)findViewById(R.id.tl_1);
        st.setTabSpaceEqual(true);
        mFragments.add(new BodyFragment());
        mFragments.add(new ListFragment());
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        st.setViewPager(viewPager);
        titlebar = (BGATitlebar) findViewById(R.id.titlebar);
        initClick();
        initHttp();
    }

    private void initHttp() {
        try {
            RequestManager.getInstance(MainActivity.this).getBody(MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        titlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
            @Override
            public void onClickRightCtv() {
                super.onClickRightCtv();
                startActivity(new Intent(MainActivity.this,MapActivity.class));
            }

            @Override
            public void onClickLeftCtv() {
                super.onClickLeftCtv();
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMoonEvent(String str){
        st.setCurrentTab(1);
        ListFragment fragment = (ListFragment) viewPager.getAdapter().instantiateItem(viewPager,1);
        fragment.setLv1(Integer.parseInt(str)-1);
        fragment.changeList(Integer.parseInt(str)-1);
    }
  /*  public void setTab(String str){

    }*/
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void clear(){
        Constants.list_zhengzhuang_first.clear();
        Constants.list_zhengzhuang_1.clear();
        Constants.list_zhengzhuang_2.clear();
        Constants.list_zhengzhuang_3.clear();
        Constants.list_zhengzhuang_4.clear();
        Constants.list_zhengzhuang_5.clear();
        Constants.list_zhengzhuang_6.clear();
        Constants.list_zhengzhuang_7.clear();
        Constants.list_zhengzhuang_8.clear();
    }
    public void getResult( List<BodyResponse.MessageBean> msgs){
        Constants.list_body =  msgs;
        clear();
        for(int i = 0;i<Constants.list_body.size();i++){
            String title = Constants.list_body.get(i).getTitle();
            Constants.list_zhengzhuang_first.add(title);
            Log.d("wjj",title);
        }
        for(int i = 0;i<Constants.list_body.size();i++){
            List<BodyResponse.MessageBean.ListsBean> lists = Constants.list_body.get(i).getLists();
            for(int j = 0;j<lists.size();j++){
                String zhengzhuang = lists.get(j).getTitle();
                String id = lists.get(j).getId();
                Disease disease = new Disease(id,zhengzhuang);
                Log.d("wjj",zhengzhuang);
                int body_type_id = Integer.parseInt(lists.get(j).getBody_type_id());
                switch (body_type_id){
                    case 1:
                        Constants.list_zhengzhuang_1.add(disease);
                        break;
                    case 2:
                        Constants.list_zhengzhuang_2.add(disease);
                        break;
                    case 3:
                        Constants.list_zhengzhuang_3.add(disease);
                        break;
                    case 4:
                        Constants.list_zhengzhuang_4.add(disease);
                        break;
                    case 5:
                        Constants.list_zhengzhuang_5.add(disease);
                        break;
                    case 6:
                        Constants.list_zhengzhuang_6.add(disease);
                        break;
                    case 7:
                        Constants.list_zhengzhuang_7.add(disease);
                        break;
                    case 8:
                        Constants.list_zhengzhuang_8.add(disease);
                        break;

                }
            }
        }
        ListFragment lf = (ListFragment) mFragments.get(1);
        lf.initList();
    }
}
