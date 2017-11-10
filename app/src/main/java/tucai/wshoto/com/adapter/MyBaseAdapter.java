package tucai.wshoto.com.adapter;

import android.content.Context;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import tucai.wshoto.com.R;


/**
 * Created by Weshine on 2017/4/20.
 */

public class MyBaseAdapter extends CommonAdapter<String> {

    public MyBaseAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        viewHolder.setText(R.id.tv, item);
    }
}
