package com.myth.shishi.wiget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.myth.shishi.R;

public class CalendarCardPager extends ViewPager
{

    private CardPagerAdapter mCardPagerAdapter;
    
    private int size = 25;
    
    private int currentPos = 12;

    public CalendarCardPager(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        readAttributeSet(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        readAttributeSet(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        mCardPagerAdapter = new CardPagerAdapter(context, size, currentPos);
        setAdapter(mCardPagerAdapter);
        super.setCurrentItem(currentPos);
    }
    
    private void readAttributeSet(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarCardPager);
        size = typedArray.getInt(R.styleable.CalendarCardPager_size, 25);
        currentPos = typedArray.getInt(R.styleable.CalendarCardPager_currentPos, 12);
        typedArray.recycle();
    }

    public CardPagerAdapter getCardPagerAdapter()
    {
        return mCardPagerAdapter;
    }

}
