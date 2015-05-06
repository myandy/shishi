package com.myth.shishi.activity;

import java.util.ArrayList;

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

public class AuthorListActivity extends BaseActivity
{

    private RecyclerView listview;

    private ArrayList<Author> ciList;

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
//        try
//        {
//            listview.smoothScrollToPosition(ciList.size());
//        }
//        catch (Exception e)
//        {
//            Log.e("AuthorList", e.toString());
//        }
    }

    private void addView()
    {
        if (ciList == null || ciList.size() == 0)
        {
            finish();
        }
        adapter.setList(ciList);
        adapter.notifyDataSetChanged();
    }

    private void setBackground()
    {
        if (isDefault)
        {
            rectLeft.setBackgroundResource(R.drawable.rect_left_selected);
            rectRight.setBackgroundResource(R.drawable.rect_right);
            ciList = AuthorDatabaseHelper.getAllShowAuthor();
        }
        else
        {
            rectLeft.setBackgroundResource(R.drawable.rect_left);
            rectRight.setBackgroundResource(R.drawable.rect_right_selected);
            ciList = AuthorDatabaseHelper.getAllAuthorByWordCount();
        }
    }

}
