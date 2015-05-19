package com.myth.shishi.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.myth.shishi.R;
import com.myth.shishi.util.DisplayUtil;

public class PingzeView extends View
{

    private static final int TYPE_PING = 1;

    private static final int TYPE_ZE = 2;

    private static final int TYPE_ZHONG = 3;
    
    private static final int TYPE_ZHONG_PING = 4;
    
    private static final int TYPE_ZHONG_ZE = 5;

    private static final int TYPE_PING_YUN = 7;

    private static final int TYPE_ZE_YUN = 8;

    private int mType;

    private Context mContext;

    public PingzeView(Context context, int type)
    {
        super(context);
        mType = type;
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (mType == TYPE_PING)
        {
            paint.setColor(mContext.getResources().getColor(R.color.yun_white));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 5), paint);
        }
        else if (mType == TYPE_ZE)
        {
            paint.setColor(mContext.getResources().getColor(R.color.black_light));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 5), paint);
        }
        else if (mType == TYPE_ZHONG_PING)
        {
            paint.setColor(mContext.getResources().getColor(R.color.yun_white));
            paint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 3), paint);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 6), paint);
        }
        else if (mType == TYPE_ZHONG_ZE)
        {
            paint.setColor(mContext.getResources().getColor(R.color.yun_white));
            paint.setStrokeWidth(DisplayUtil.dip2px(mContext, 1));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 6), paint);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 3), paint);
            
        }
        else if (mType == TYPE_ZE_YUN)
        {
            paint.setColor(mContext.getResources().getColor(R.color.pingze_red));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 5), paint);
        }
        else if (mType == TYPE_PING_YUN)
        {
            paint.setColor(mContext.getResources().getColor(R.color.pingze_blue));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 10),
                    DisplayUtil.dip2px(mContext, 5), paint);
        }
    }

}
