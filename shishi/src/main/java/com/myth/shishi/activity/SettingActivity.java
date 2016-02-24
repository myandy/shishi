package com.myth.shishi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.db.YunDatabaseHelper;

public class SettingActivity extends BaseActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();


    }

    private void initView()
    {

        refreshYun();
        refreshTypeface();
        refreshCheck();

        ((TextView) findViewById(R.id.yun_title)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.yun_value)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.typeface_value)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.typeface_title)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.check_value)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.check_title)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.about_title)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.congratuate_us_title)).setTypeface(myApplication.getTypeface());
        ((TextView) findViewById(R.id.former_title)).setTypeface(myApplication.getTypeface());

        findViewById(R.id.item_yun).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setSingleChoiceItems(YunDatabaseHelper.YUNString,
                        YunDatabaseHelper.getDefaultYunShu(mActivity), new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                YunDatabaseHelper.setDefaultYunShu(mActivity, which);
                                refreshYun();
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        findViewById(R.id.item_typeface).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setSingleChoiceItems(MyApplication.TypefaceString,
                        MyApplication.getDefaulTypeface(mActivity), new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                MyApplication.setDefaultTypeface(mActivity, which);
                                myApplication.setTypeface(mActivity, MyApplication.getDefaulTypeface(mActivity));
                                refreshTypeface();
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        findViewById(R.id.item_check).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String[] s = {mActivity.getString(R.string.check_true), mActivity.getString(R.string.check_false)};
                new AlertDialog.Builder(mActivity).setSingleChoiceItems(s,
                        MyApplication.getCheckAble(mActivity) ? 0 : 1, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                MyApplication.setCheckAble(mActivity, which == 0);
                                refreshCheck();
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        findViewById(R.id.item_about).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mActivity, AboutActivity.class));
            }
        });
        
        
        findViewById(R.id.item_congratuate_us).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.item_former).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity,FormerListActivity.class);
                intent.putExtra("edit", true);
                startActivity(intent);
            }
        });
        
      
    }

    private void refreshYun()
    {
        ((TextView) findViewById(R.id.yun_value)).setText(YunDatabaseHelper.YUNString[YunDatabaseHelper.getDefaultYunShu(mActivity)]);
    }

    private void refreshTypeface()
    {
        ((TextView) findViewById(R.id.typeface_value)).setText(MyApplication.TypefaceString[MyApplication.getDefaulTypeface(mActivity)]);
    }

    private void refreshCheck()
    {
        if (MyApplication.getCheckAble(mActivity))
        {
            ((TextView) findViewById(R.id.check_value)).setText(R.string.check_true);
        }
        else
        {
            ((TextView) findViewById(R.id.check_value)).setText(R.string.check_false);
        }
    }
    
    

}
