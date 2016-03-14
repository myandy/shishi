package com.myth.shishi.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38728119
 * 
 * @author chenjing
 */
public class ScanView extends RelativeLayout
{
    public static final String TAG = "ScanView";

    private boolean isInit = true;

    // 滑动的时候存在两页可滑动，要判断是哪一页在滑动
    private boolean isPreMoving = true, isCurrMoving = true;

    // 当前是第几页
    private int index;

    private float lastX;

    // 前一页，当前页，下一页的左边位置
    private int prePageLeft = 0, currPageLeft = 0, nextPageLeft = 0;

    // 三张页面
    private View prePage, currPage, nextPage;

    // 页面状态
    private static final int STATE_MOVE = 0;

    private static final int STATE_STOP = 1;

    // 滑动的页面，只有前一页和当前页可滑
    private static final int PRE = 2;

    private static final int CURR = 3;

    private int state = STATE_STOP;

    // 正在滑动的页面右边位置，用于绘制阴影
    private float right;

    // 手指滑动的距离
    private float moveLenght;

    // 页面宽高
    private int mWidth, mHeight;

    // 获取滑动速度
    private VelocityTracker vt;

    // 防止抖动
    private float speed_shake = 20;

    // 当前滑动速度
    private float speed;

    private Timer timer;

    private MyTimerTask mTask;

    // 滑动动画的移动速度
    public static final int MOVE_SPEED = 10;

    // 页面适配器
    private PagerAdapter adapter;

    /**
     * 过滤多点触碰的控制变量
     */
    private int mEvents;

    public void setAdapter(PagerAdapter adapter, int index)
    {
        removeAllViews();
        this.index = index;
        this.adapter = adapter;
        prePage = (View) adapter.instantiateItem(this, index - 1);
        addView(prePage, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        currPage = (View) adapter.instantiateItem(this, index);
        addView(currPage, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        nextPage = (View) adapter.instantiateItem(this, index + 1);
        addView(nextPage, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    public void setCurrentItem(int position)
    {

    }

    /**
     * 向左滑。注意可以滑动的页面只有当前页和前一页
     * 
     * @param which
     */
    private void moveLeft(int which)
    {
        switch (which)
        {
            case PRE:
                prePageLeft -= MOVE_SPEED;
                if (prePageLeft < 0)
                    prePageLeft = 0;
                right = mWidth + prePageLeft;
                break;
            case CURR:
                currPageLeft -= MOVE_SPEED;
                if (currPageLeft < 0)
                    currPageLeft = 0;
                right = mWidth + currPageLeft;
                break;
        }
    }

    /**
     * 向右滑。注意可以滑动的页面只有当前页和前一页
     * 
     * @param which
     */
    private void moveRight(int which)
    {
        switch (which)
        {
            case PRE:
                prePageLeft += MOVE_SPEED;
                if (prePageLeft > mWidth)
                    prePageLeft = mWidth;
                right = mWidth + prePageLeft;
                break;
            case CURR:
                currPageLeft += MOVE_SPEED;
                if (currPageLeft > mWidth)
                    currPageLeft = mWidth;
                right = mWidth + currPageLeft;
                break;
        }
    }

    /**
     * 当往回翻过一页时添加前一页在最左边
     */
    private void addPrePage()
    {
        removeView(nextPage);

        // 从适配器获取前一页内容
        nextPage = (View) adapter.instantiateItem(this, index - 1);
        addView(nextPage, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // adapter.addContent(nextPage, index - 1);
        // 交换顺序
        View temp = nextPage;
        nextPage = currPage;
        currPage = prePage;
        prePage = temp;
        prePageLeft = mWidth;
    }

    /**
     * 当往前翻过一页时，添加一页在最底下
     */
    private void addNextPage()
    {
        removeView(prePage);

        // 从适配器获取后一页内容
        prePage = (View) adapter.instantiateItem(this, index + 1);
        addView(prePage, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // adapter.addContent(prePage, index + 1);
        // 交换顺序
        View temp = currPage;
        currPage = nextPage;
        nextPage = prePage;
        prePage = temp;
        currPageLeft = 0;
    }

     Handler updateHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (state != STATE_MOVE)
                return;
            // 移动页面
            // 翻回，先判断当前哪一页处于未返回状态
            if (prePageLeft < mWidth && speed >= 0)
            {
                // 当前页处于未返回状态
                moveRight(PRE);

            }
            else if (currPageLeft > 0 && speed <= 0)
            {
                // 前一页处于未返回状态
                moveLeft(CURR);
            }
            else if (speed < 0 && index > 1)
            {
                // 向左翻，翻动的是当前页
                moveLeft(PRE);
                if (prePageLeft == 0)
                {
                    index--;
                    // 翻回一页，添加一页在最上层，隐藏在最左边
                    addPrePage();
                }
            }
            else if (speed > 0 && index < adapter.getCount())
            {
                // 向右翻，翻动的是前一页
                moveRight(CURR);
                if (currPageLeft == mWidth)
                {
                    index++;
                    // 翻过一页，在底下添加一页，把最上层页面移除
                    addNextPage();
                }

            }
            if (right == mWidth || right == 2 * mWidth)
            {
                releaseMoving();
                state = STATE_STOP;
                quitMove();
            }
            ScanView.this.requestLayout();
        }

    };


    public ScanView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public ScanView(Context context)
    {
        super(context);
        init();
    }

    public ScanView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /**
     * 退出动画翻页
     */
    public void quitMove()
    {
        if (mTask != null)
        {
            mTask.cancel();
            mTask = null;
        }
    }

    private void init()
    {
        index = 1;
        timer = new Timer();
        mTask = new MyTimerTask(updateHandler);
    }

    /**
     * 释放动作，不限制手滑动方向
     */
    private void releaseMoving()
    {
        isPreMoving = true;
        isCurrMoving = true;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev)
//    {
//        return executeKeyEvent(ev);
//    }

    float xLast = 0;

    float yLast = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean handled = false;
        float xDistance = 0;
        float yDistance = 0;

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xLast = ev.getX();
                yLast = ev.getY();

                lastX = ev.getX();
                try
                {
                    if (vt == null)
                    {
                        vt = VelocityTracker.obtain();
                    }
                    else
                    {
                        vt.clear();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                vt.addMovement(ev);
                mEvents = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);

                if (xDistance > yDistance)
                {
                    handled = true;
                }
        }
        return handled;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0)
        {
            return false;
        }
        if (adapter != null)
            switch (event.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    try
                    {
                        if (vt == null)
                        {
                            vt = VelocityTracker.obtain();
                        }
                        else
                        {
                            vt.clear();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    vt.addMovement(event);
                    mEvents = 0;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    mEvents = -1;
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 取消动画
                    quitMove();
                    vt.addMovement(event);
                    vt.computeCurrentVelocity(500);
                    speed = vt.getXVelocity();
                    moveLenght = event.getX() - lastX;
                    if ((moveLenght > 0 || !isPreMoving) && isCurrMoving && mEvents == 0)
                    {
                        isPreMoving = false;
                        isCurrMoving = true;
                        if (index == adapter.getCount())
                        {
                            // 最后一页不能再往左翻
                            state = STATE_STOP;
                            releaseMoving();
                        }
                        else
                        {
                            currPageLeft += (int) moveLenght;
                            // 防止滑过边界
                            if (currPageLeft > mWidth)
                                currPageLeft = mWidth;
                            else if (currPageLeft < 0)
                            {
                                // 边界判断，释放动作，防止来回滑动导致滑动前一页时当前页无法滑动
                                currPageLeft = 0;
                                releaseMoving();
                            }

                            right = mWidth + currPageLeft;
                            state = STATE_MOVE;
                        }
                    }
                    else if ((moveLenght < 0 || !isCurrMoving) && isPreMoving && mEvents == 0)
                    {

                        isPreMoving = true;
                        isCurrMoving = false;
                        if (index == 1)
                        {
                            // 第一页不能再往右翻，跳转到前一个activity
                            state = STATE_MOVE;
                            releaseMoving();
                        }
                        else
                        {
                            // 非第一页
                            prePageLeft += (int) moveLenght;
                            // 防止滑过边界
                            if (prePageLeft < 0)
                                prePageLeft = 0;
                            else if (prePageLeft > mWidth)
                            {
                                // 边界判断，释放动作，防止来回滑动导致滑动当前页是前一页无法滑动
                                prePageLeft = mWidth;
                                releaseMoving();
                            }
                            right = mWidth + prePageLeft;
                            state = STATE_MOVE;
                        }

                    }
                    else
                        mEvents = 0;
                    lastX = event.getX();
                    requestLayout();
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(speed) < speed_shake)
                        speed = 0;
                    quitMove();
                    mTask = new MyTimerTask(updateHandler);
                    timer.schedule(mTask, 0, 5);
                    break;
                default:
                    break;
            }
        return true;
    }

    /*
     * （非 Javadoc） 在这里绘制翻页阴影效果
     * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
     */
    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        if (right == mWidth || right == 2 * mWidth)
            return;
        RectF rectF = new RectF(0, 0, right - mWidth, mHeight);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        LinearGradient linearGradient = new LinearGradient(right - mWidth - 9, 0, right - mWidth, 0, 0x00bbbbbb,
                0xffbbbbbb, TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setStyle(Style.FILL);
        canvas.drawRect(rectF, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (isInit)
        {
            // 初始状态，一页放在左边隐藏起来，两页叠在一块
            prePageLeft = mWidth;
            currPageLeft = 0;
            nextPageLeft = 0;
            isInit = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if (adapter == null)
            return;
        prePage.layout(prePageLeft, 0, prePageLeft + prePage.getMeasuredWidth(), prePage.getMeasuredHeight());
        currPage.layout(currPageLeft, 0, currPageLeft + currPage.getMeasuredWidth(), currPage.getMeasuredHeight());
        nextPage.layout(nextPageLeft, 0, nextPageLeft + nextPage.getMeasuredWidth(), nextPage.getMeasuredHeight());
        invalidate();
    }

    class MyTimerTask extends TimerTask
    {
        Handler handler;

        public MyTimerTask(Handler handler)
        {
            this.handler = handler;
        }

        @Override
        public void run()
        {
            handler.sendMessage(handler.obtainMessage());
        }

    }
}
