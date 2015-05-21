package com.myth.shishi.wiget.calendar;

import java.util.Calendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

class CardPagerAdapter extends PagerAdapter
{

    private Context mContext;
    
    private int size;
    
    private int startPos;

    CardPagerAdapter(Context ctx, int size, int startPos)
    {
        mContext = ctx;
        this.size = size;
        this.startPos = startPos;
    }

    @Override
    public Object instantiateItem(View collection, final int position)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position - startPos);
        CalendarCard card = new CalendarCard(mContext);
        card.setDateDisplay(cal);
        card.notifyChanges();

        ((ViewPager) collection).addView(card, 0);

        return card;
    }

    @Override
    public void destroyItem(View collection, int position, Object view)
    {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((View) object);
    }

    @Override
    public void finishUpdate(View arg0)
    {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void startUpdate(View arg0)
    {
    }

    @Override
    public int getCount()
    {
        return size;
    }

}
