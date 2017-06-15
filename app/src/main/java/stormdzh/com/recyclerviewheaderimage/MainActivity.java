package stormdzh.com.recyclerviewheaderimage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mAnim_rv;
    private ImageView img_mine_background;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;
    private TransAdapter mTransAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setColor(Color.parseColor("#33000000"));

        mAnim_rv = (RecyclerView) findViewById(R.id.rcv_trans);
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = new LinearLayoutManager(this);
        }
        mAnim_rv.setLayoutManager(mLinearLayoutManager);
        if (mTransAdapter == null)
            mTransAdapter = new TransAdapter(this);
        mAnim_rv.setAdapter(mTransAdapter);
        mTransAdapter.setOnTouchClick(new OnTouchClick() {
            @Override
            public void onTouch(ImageView view) {
                img_mine_background = view;
                metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_mine_background.getLayoutParams();
                lp.width = metric.widthPixels;
                lp.height = metric.widthPixels * 9 / 16;
                img_mine_background.setLayoutParams(lp);
                initzoomimage();
            }
        });

    }


    /**
     * 这里我设置的图片在状态栏的下面，并且给状态栏设置了一个浅浅的透明色。
     *
     * @param color
     */
    public void setColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使状态栏透明
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 如果想要给状态栏加点颜色，例如加点透明的阴影，就需要加上下面的三行代码
            // 这是生成一个状态栏大小的矩形，给这个矩形添加颜色，添加 statusView 到布局中
            View statusView = createStatusView(color);
            ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
            decorView.addView(statusView);


            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            // 这个是决定我们的布局是否是在状态栏的下面
            rootView.setFitsSystemWindows(false);
            rootView.setClipToPadding(true);
        }

    }


    /**
     * 这个生成一个状态栏大小的矩形，给这个矩形，添加 statusView 到布局中
     *
     * @param color
     * @return
     */
    private View createStatusView(int color) {
        // 获得状态栏高度
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }


    /**
     * 图片缩放的处理
     */
    private void initzoomimage() {

        mAnim_rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_mine_background.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            //当图片也就是第一个item完全可见的时候，记录触摸屏幕的位置
                            if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                        if (distance < 0) {
                            break;
                        }
                        // 处理放大
                        mScaling = true;
//                        lp.width = metric.widthPixels + distance;
                        lp.width = metric.widthPixels;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        img_mine_background.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });

    }

    private void replyImage() {
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_mine_background.getLayoutParams();
        final float w = img_mine_background.getLayoutParams().width;// 图片当前宽度
        final float h = img_mine_background.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                img_mine_background.setLayoutParams(lp);
            }
        });
        anim.start();

    }


}

