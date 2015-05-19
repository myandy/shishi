package com.myth.shishi.wiget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.activity.AuthorListActivity;
import com.myth.shishi.activity.AuthorSearchActivity;
import com.myth.shishi.activity.DuiShiActivity;
import com.myth.shishi.activity.PoetryActivity;
import com.myth.shishi.activity.PoetrySearchActivity;
import com.wandoujia.ads.sdk.Ads;

public class MainView extends RelativeLayout
{

    private static final String APP_ID = "100026935";

    private static final String SECRET_KEY = "708cb4040552fd49fb6fab7e00d4b8dd";

    private static final String APP_WALL = "827e532f2d81b35e2b1d226ed6c79759";

    private Context mContext;

    public MainView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public MainView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MainView(Context context)
    {
        super(context);
        mContext = context;
        initView();

        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Void... params)
            {
                try
                {
                    Ads.init(mContext, APP_ID, SECRET_KEY);
                    return true;
                }
                catch (Exception e)
                {
                    Log.e("ads-sample", "error", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success)
            {
                if (success)
                {
                    /**
                     * pre load
                     */
                    Ads.preLoad(APP_WALL, Ads.AdFormat.appwall);
                }
            }
        }.execute();
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.layout_main, null);

        ViewGroup showAll = (ViewGroup) root.findViewById(R.id.show_all);
        showAll.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mContext.startActivity(new Intent(mContext, AuthorListActivity.class));
            }
        });
        for (int i = 0; i < showAll.getChildCount(); i++)
        {
            ((TextView) showAll.getChildAt(i)).setTypeface(MyApplication.typeface);
        }

        TextView favorite = (TextView) root.findViewById(R.id.favorite);
        favorite.setTypeface(MyApplication.typeface);
        favorite.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, PoetrySearchActivity.class);
                intent.putExtra("collect", true);
                mContext.startActivity(intent);

            }
        });

        TextView duishi = (TextView) root.findViewById(R.id.duishi);
        duishi.setTypeface(MyApplication.typeface);
        duishi.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, DuiShiActivity.class);
                mContext.startActivity(intent);

            }
        });

        TextView showOne = (TextView) root.findViewById(R.id.show_one);
        showOne.setTypeface(MyApplication.typeface);
        showOne.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, PoetryActivity.class);
                mContext.startActivity(intent);

            }
        });

        TextView search = (TextView) root.findViewById(R.id.search);
        search.setTypeface(MyApplication.typeface);
        search.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mContext).setItems(new String[] {"搜索诗人", "搜索诗"},
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    Intent intent = new Intent(mContext, AuthorSearchActivity.class);
                                    mContext.startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(mContext, PoetrySearchActivity.class);
                                    mContext.startActivity(intent);
                                }
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        TextView ad = (TextView) root.findViewById(R.id.ad);
        ad.setTypeface(MyApplication.typeface);
        ad.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Ads.showAppWall((Activity) mContext, APP_WALL);
            }
        });

        addView(root, new LayoutParams(-1, -1));
    }

}
