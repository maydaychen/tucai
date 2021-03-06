package tucai.wshoto.com.adapter;

import android.content.Context;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import tucai.wshoto.com.R;
import tucai.wshoto.com.bean.Disease;



/**
 * Created by Weshine on 2017/4/20.
 */

public class MyBaseAdapter2 extends CommonAdapter<Disease> {
    public MyBaseAdapter2(Context context, int layoutId, List<Disease> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Disease item, int position) {
        viewHolder.setText(R.id.tv, item.getTitle());
    }
}
