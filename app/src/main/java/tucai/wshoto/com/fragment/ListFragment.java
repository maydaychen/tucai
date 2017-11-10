package tucai.wshoto.com.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import tucai.wshoto.com.R;
import tucai.wshoto.com.activity.WebAppActivity;
import tucai.wshoto.com.adapter.MyBaseAdapter;
import tucai.wshoto.com.adapter.MyBaseAdapter2;
import tucai.wshoto.com.constants.Constants;



/**
 * Created by Weshine on 2017/4/20.
 */

public class ListFragment extends Fragment{
    private ListView lv1;
    private ListView lv2;
    private int current;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_list,null);
        lv1 = (ListView) view.findViewById(R.id.lv1);
        lv2= (ListView) view.findViewById(R.id.lv2);
        initItemClick();
        clicker();
        if(Constants.list_zhengzhuang_first.size()!=0){
            initList();
        }
        return view;
    }

    public void initList() {
        lv1.setAdapter(new MyBaseAdapter(getActivity(),R.layout.item_base,Constants.list_zhengzhuang_first));
        lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_1));
        Log.d("wjj",Constants.list_zhengzhuang_first.size()+"");
    }

    private void initItemClick() {
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //切换二级数组
                changeList(position);
                for(int i=0;i<lv1.getCount();i++){
                    View v =  lv1.getChildAt(i);
                    TextView tv = (TextView) v.findViewById(R.id.tv);
                    if (position == i) {
                        Log.d("wjj",position+"");
                        v.setBackgroundColor(Color.WHITE);
                        tv.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        v.setBackgroundColor(getResources().getColor(R.color.grey_f9));
                        tv.setTextColor(Color.BLACK);
                    }
                }
            }
        });
    }

    public void changeList(int i) {
        current = i;
        switch (i){
            case 0:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_1));
                break;
            case 1:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_2));
                break;
            case 2:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_3));
                break;
            case 3:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_4));
                break;
            case 4:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_5));
                break;
            case 5:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_6));
                break;
            case 6:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_7));
                break;
            case 7:
                lv2.setAdapter(new MyBaseAdapter2(getActivity(),R.layout.item_second,Constants.list_zhengzhuang_8));
                break;
        }
    }
    private void clicker(){
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebAppActivity.class);
                String temp = "";
                String title = "";
                switch (current){
                    case 0:
                        temp = Constants.list_zhengzhuang_1.get(position).getId();
                        title = Constants.list_zhengzhuang_1.get(position).getTitle();
                        break;
                    case 1:
                        temp = Constants.list_zhengzhuang_2.get(position).getId();
                        title = Constants.list_zhengzhuang_2.get(position).getTitle();
                        break;
                    case 2:
                        temp = Constants.list_zhengzhuang_3.get(position).getId();
                        title = Constants.list_zhengzhuang_3.get(position).getTitle();
                        break;
                    case 3:
                        temp = Constants.list_zhengzhuang_3.get(position).getId();
                        title = Constants.list_zhengzhuang_3.get(position).getTitle();
                        break;
                    case 4:
                        temp = Constants.list_zhengzhuang_4.get(position).getId();
                        title = Constants.list_zhengzhuang_4.get(position).getTitle();
                        break;
                    case 5:
                        temp = Constants.list_zhengzhuang_5.get(position).getId();
                        title = Constants.list_zhengzhuang_5.get(position).getTitle();
                        break;
                    case 6:
                        temp = Constants.list_zhengzhuang_6.get(position).getId();
                        title = Constants.list_zhengzhuang_6.get(position).getTitle();
                        break;
                    case 7:
                        temp = Constants.list_zhengzhuang_7.get(position).getId();
                        title = Constants.list_zhengzhuang_7.get(position).getTitle();
                        break;
                }
                intent.putExtra("id",temp);
                intent.putExtra("title",title);
                Log.d("wjj",temp);
                Log.d("wjj",title);
                startActivity(intent);
             /*   getActivity().setResult(0,intent);
                getActivity().finish();*/
            }
        });
    }
    public void setLv1(int position){
        Log.d("wjj","asdasdasdas");
        for(int i=0;i<lv1.getAdapter().getCount();i++){
            View v =  lv1.getChildAt(i);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            if (position == i) {
                v.setBackgroundColor(Color.WHITE);
                tv.setTextColor(getResources().getColor(R.color.green));
            } else {
                v.setBackgroundColor(getResources().getColor(R.color.grey_f9));
                tv.setTextColor(Color.BLACK);
            }
        }
    }

}
