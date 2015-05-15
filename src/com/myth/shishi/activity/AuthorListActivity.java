package com.myth.shishi.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.adapter.AuthorListAdapter;
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.db.DynastyDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.ColorEntity;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.wiget.TouchEffectImageView;

public class AuthorListActivity extends BaseActivity
{

    private RecyclerView listview;

    private ArrayList<Author> aList;

    private boolean isDefault = true;

    private AuthorListAdapter adapter;

    TextView rectLeft;

    TextView rectRight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipai_list);
        setBottomVisible();
        isDefault = MyApplication.getDefaulListType(mActivity);
        initView();
    }

    private void initView()
    {

        ImageView setting = new TouchEffectImageView(mActivity, null);
        setting.setImageResource(R.drawable.setting);
        setting.setScaleType(ScaleType.FIT_XY);
        addBottomRightView(setting,
                new LayoutParams(DisplayUtil.dip2px(mActivity, 48), DisplayUtil.dip2px(mActivity, 48)));
        setting.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setSingleChoiceItems(DynastyDatabaseHelper.dynastyArray,
                        MyApplication.getDefaultDynasty(mActivity), new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                MyApplication.setDefaultDynasty(mActivity, which);
                                setBackground();
                                addView();
                                dialog.dismiss();
                            }

                        }).show();
            }
        });

        listview = (RecyclerView) findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new AuthorListAdapter(mActivity);
        listview.setAdapter(adapter);
        rectLeft = (TextView) findViewById(R.id.rect_left);
        rectLeft.setTypeface(MyApplication.typeface);
        rectRight = (TextView) findViewById(R.id.rect_right);
        rectRight.setTypeface(MyApplication.typeface);

        setBackground();

        rectLeft.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                if (!isDefault)
                {
                    isDefault = true;
                    MyApplication.setDefaultListType(mActivity, true);
                    setBackground();
                    addView();
                }
            }
        });
        rectRight.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                if (isDefault)
                {
                    isDefault = false;
                    MyApplication.setDefaultListType(mActivity, false);
                    setBackground();
                    addView();
                }
            }
        });
        addView();
        // try
        // {
        // listview.smoothScrollToPosition(ciList.size());
        // }
        // catch (Exception e)
        // {
        // Log.e("AuthorList", e.toString());
        // }

        // new Thread(new Runnable()
        // {
        //
        // @Override
        // public void run()
        // {
        // doIt();
        //
        // }
        // }).start();
    }

    public void doIt()
    {

        List<Author> list = aList;

        for (int i = 0; i < list.size(); i++)
        {
            ColorEntity colorEntity = MyApplication.getColorByPos(i / 2);
            int color = 0xffffff;
            if (colorEntity != null)
            {
                color = Color.rgb(colorEntity.getRed(), colorEntity.getGreen(), colorEntity.getBlue());
            }
            list.get(i).setColor(color);

            AuthorDatabaseHelper.update(list.get(i).getAuthor(), list.get(i).getColor());
        }

    }

    private void addView()
    {
        if (aList == null || aList.size() == 0)
        {
            finish();
        }
        adapter.setList(aList);
        adapter.notifyDataSetChanged();
    }

    private void setBackground()
    {
        if (isDefault)
        {
            rectLeft.setBackgroundResource(R.drawable.rect_left_selected);
            rectRight.setBackgroundResource(R.drawable.rect_right);
            aList = AuthorDatabaseHelper.getAll(MyApplication.getDefaultDynasty(mActivity));
        }
        else
        {
            rectLeft.setBackgroundResource(R.drawable.rect_left);
            rectRight.setBackgroundResource(R.drawable.rect_right_selected);
            aList = AuthorDatabaseHelper.getAllAuthorByPNum(MyApplication.getDefaultDynasty(mActivity));
        }
    }

}
