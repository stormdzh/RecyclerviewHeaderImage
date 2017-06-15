package stormdzh.com.recyclerviewheaderimage;

/**
 * Created by Administrator on 2017/6/15.
 *
 * @author Administrator.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by sunxiaokun on 2016/8/24.
 */
public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder> {
    private static final int IS_HEADER = 0;
    private static final int IS_NORMAL = 1;
    private Context mContext;
    private OnTouchClick mOnTouchClick;

    public void setOnTouchClick(OnTouchClick onTouchClick) {
        mOnTouchClick = onTouchClick;
    }


    public TransAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? IS_HEADER : IS_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case IS_HEADER:
                View view = View.inflate(mContext, R.layout.item_header_layout, null);
                viewHolder = new ViewHolder(view);
                viewHolder.trans_iv = (ImageView) view.findViewById(R.id.trans_iv_item);
                break;
            case IS_NORMAL:
                View view1 = View.inflate(mContext, R.layout.item_normal_layout, null);
                viewHolder = new ViewHolder(view1);
                viewHolder.iv_anim = (ImageView) view1.findViewById(R.id.iv_anim);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) {
            mOnTouchClick.onTouch(holder.trans_iv);
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_anim;
        public ImageView trans_iv;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}