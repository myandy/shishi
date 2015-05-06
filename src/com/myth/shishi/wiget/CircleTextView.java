package com.myth.shishi.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.myth.shishi.R;
import com.myth.shishi.MyApplication;
import com.myth.shishi.util.DisplayUtil;

public class CircleTextView extends View
{

    private int mColor;

    private String mText;

    private Context mContext;

    public CircleTextView(Context context, String text, int color)
    {
        super(context);
        mContext = context;
        mColor = color;
        mText = text;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(DisplayUtil.dip2px(mContext, 32), DisplayUtil.dip2px(mContext, 32),
                DisplayUtil.dip2px(mContext, 30), paint);
        paint.setColor(R.color.white);
        paint.setTextSize(DisplayUtil.dip2px(mContext, 16));
        paint.setTypeface(MyApplication.typeface);
        canvas.drawText(mText, DisplayUtil.dip2px(mContext, 21), DisplayUtil.dip2px(mContext, 37), paint);
    }

}
