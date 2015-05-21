package com.myth.shishi.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.wiget.calendar.CalendarCard;
import com.myth.shishi.wiget.calendar.CardGridItem;
import com.myth.shishi.wiget.calendar.CheckableLayout;
import com.myth.shishi.wiget.calendar.OnItemRender;
import com.umeng.analytics.MobclickAgent;

public class SignActivity extends BaseActivity
{

    private TextView tvSign, tvSerialSignDays, tvSignDaysOfMonth;

    private CalendarCard card;

    /**
     * 本月签到记录
     */
    private String mMonthSignRecord = "";

    /**
     * 连续签到天数
     */
    private int mContinueSignCount = 0;

    // 是否已经签到
    boolean isSigned;

    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
    }

    private void initView()
    {

        tvSign = (TextView) findViewById(R.id.tv_sign);
        tvSign.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                sign();
            }
        });
        tvSerialSignDays = (TextView) findViewById(R.id.tv_serial_sign_days);
        tvSignDaysOfMonth = (TextView) findViewById(R.id.tv_sign_days_of_month);

        card = (CalendarCard) findViewById(R.id.calendar_card);
        card.setOnItemRender(new OnItemRender()
        {
            @Override
            public void onRender(CheckableLayout v, CardGridItem item)
            {
                v.setClickable(false);
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.calendar_card_grid_bg));
                TextView tv = (TextView) v.findViewById(R.id.tv);
                tv.setText(item.isEnabled() ? item.getDayOfMonth().toString() : "");
                tv.setTextColor(0xFF191919);

                if (item.isEnabled())
                {
                    if (item.getDayOfMonth() == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    {
                        tv.setTextColor(0xFFFFFFFF);
                        if (isSignedDay(item.getDayOfMonth()))
                        {
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.usercenter_sign_orange));
                        }
                        else
                        {
                            v.setBackgroundColor(0xFFFF8A00);
                        }
                    }
                    else
                    {
                        if (isSignedDay(item.getDayOfMonth()))
                        {
                            tv.setTextColor(Color.rgb(55, 134, 219));
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.usercenter_sign_blue));
                        }
                    }
                }
            }
        });


        refresh();

    }

    private void refresh()
    {
        // 本月签到记录
        mMonthSignRecord = MyApplication.getDefaultSignMonth(mActivity);
        Calendar date = Calendar.getInstance();
        dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        isSigned = isSignedDay(dayOfMonth);

        if (!TextUtils.isEmpty(mMonthSignRecord))
        {
            tvSign.setText(isSigned ? R.string.usercenter_sign_finished : R.string.sign_tip);
            tvSign.setEnabled(!isSigned);
            // 显示本月签到记录
            showMonthSignRecord();
        }
        mContinueSignCount = MyApplication.getDefaultSignDay(mActivity);
        tvSerialSignDays.setText(String.valueOf(mContinueSignCount));

        TextView tvSignTip = (TextView) findViewById(R.id.tv_sign_tip);

        if (isSigned)
        {
            tvSignTip.setVisibility(View.VISIBLE);
            int point = MyApplication.getDefaultSignPoint(mActivity);
            tvSignTip.setText("今日获得" + point + "积分，每日签到可获得1-5点积分");
        }
        else
        {
            tvSignTip.setVisibility(View.GONE);
        }

        card.notifyChanges();

    }

    public static boolean isSign(Context context)
    {
        Calendar date = Calendar.getInstance();
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        String mMonthSignRecord = MyApplication.getDefaultSignMonth(context);
        return !TextUtils.isEmpty(mMonthSignRecord) && mMonthSignRecord.charAt(dayOfMonth - 1) == '1';
    }

    protected void sign()
    {
        if (!TextUtils.isEmpty(mMonthSignRecord))
        {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mMonthSignRecord.length(); i++)
            {
                if (i == dayOfMonth - 1)
                {
                    sb.append('1');
                }
                else
                {
                    sb.append(mMonthSignRecord.charAt(i));
                }
            }
            MyApplication.saveDefaultSignMonth(mActivity, sb.toString());

            int point = getRandomPoint();

            Toast.makeText(mActivity, "签到成功，获得" + point + "积分", Toast.LENGTH_SHORT).show();

            MyApplication.setDefaultSignPoint(mActivity, point);

            MyApplication.setDefaultPoint(mActivity, point + MyApplication.getDefaultPoint(mActivity));

            if (dayOfMonth == 1 || isSignedDay(dayOfMonth - 1))
            {
                MyApplication.setDefaultSignDay(mActivity, MyApplication.getDefaultSignDay(mActivity) + 1);
            }
            else
            {
                MyApplication.setDefaultSignDay(mActivity, 1);
            }
            
            refresh();
            MobclickAgent.onEventValue(mActivity, "user_sign", null, 1);
        }
    }

    private int getRandomPoint()
    {
        Random random = new Random();
        if (mContinueSignCount == 0)
        {
            return random.nextInt(6);
        }
        else if (mContinueSignCount > 1 && mContinueSignCount < 3)
        {
            return random.nextInt(5) + 1;
        }
        else
        {
            return random.nextInt(4) + 2;
        }
    }

    /**
     * 显示本月签到记录
     * 
     * @see [类、类#方法、类#成员]
     */
    private void showMonthSignRecord()
    {
        tvSignDaysOfMonth.setText(String.valueOf(getTotalSignCount()));
        card.notifyChanges();
    }

    /**
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int getTotalSignCount()
    {
        int totalSignCount = 0;
        if (!TextUtils.isEmpty(mMonthSignRecord))
        {
            for (int i = 0; i < mMonthSignRecord.length(); i++)
            {
                if (mMonthSignRecord.charAt(i) == '1')
                {
                    totalSignCount++;
                }
            }
        }
        return totalSignCount;
    }

    /**
     * 判断某一天是否为签到的
     * 
     * @param dayOfMonth
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isSignedDay(int dayOfMonth)
    {
        return !TextUtils.isEmpty(mMonthSignRecord) && mMonthSignRecord.charAt(dayOfMonth - 1) == '1';
    }

}
