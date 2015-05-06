package com.myth.shishi.wiget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.myth.shishi.R;
import com.myth.shishi.util.DisplayUtil;

public class CircleStringView extends View
{

    private int mColor;
    
    private String mString;

    private Context mContext;

    public int getmColor()
    {
        return mColor;
    }

    public void setType(String s,int mColor)
    {
        this.mString=s;
        this.mColor = mColor;
        invalidate();
    }

    public CircleStringView(Context context)
    {
        super(context);
        mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(DisplayUtil.dip2px(mContext, 12), DisplayUtil.dip2px(mContext, 12),
                DisplayUtil.dip2px(mContext, 11), paint);
        paint.setTextSize(24);
        canvas.drawText(mString.charAt(0)+"",DisplayUtil.dip2px(mContext, 7), DisplayUtil.dip2px(mContext, 15), paint);
    }

}
