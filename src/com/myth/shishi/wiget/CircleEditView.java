package com.myth.shishi.wiget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.myth.shishi.R;
import com.myth.shishi.util.DisplayUtil;

public class CircleEditView extends View
{

    private int mColor;
    
    private String string;

    private Context mContext;

    public int getmColor()
    {
        return mColor;
    }

    public void setmColor(int mColor)
    {
        this.mColor = mColor;
        invalidate();
    }

    public CircleEditView(Context context,String string)
    {
        super(context);
        mContext = context;
        this.string=string;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(DisplayUtil.dip2px(mContext, 48), DisplayUtil.dip2px(mContext, 48),
//                DisplayUtil.dip2px(mContext, 46), paint);
        paint.setTextSize(38);
        paint.setColor(0xffffff);
        canvas.drawText(string.charAt(0)+"A", getLeft(), getTop()+30, paint);
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.edit_white),
//                DisplayUtil.dip2px(mContext, 16), DisplayUtil.dip2px(mContext, 16), paint);
    }

}
