package com.myth.shishi.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.adapter.AuthorListAdapter;
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.ColorEntity;

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
        isDefault = MyApplication.getDefaulListType(mActivity);
        initView();
    }

    private void initView()
    {
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

//        new Thread(new Runnable()
//        {
//
//            @Override
//            public void run()
//            {
//                doIt();
//
//            }
//        }).start();
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
            aList = AuthorDatabaseHelper.getAll();
        }
        else
        {
            rectLeft.setBackgroundResource(R.drawable.rect_left);
            rectRight.setBackgroundResource(R.drawable.rect_right_selected);
            aList = AuthorDatabaseHelper.getAllAuthorByPNum();
        }
    }

}
