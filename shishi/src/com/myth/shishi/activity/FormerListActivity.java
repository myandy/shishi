package com.myth.shishi.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.adapter.FormerAdapter;
import com.myth.shishi.db.FormerDatabaseHelper;
import com.myth.shishi.entity.Former;
import com.myth.shishi.listener.MyListener;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.wiget.TouchEffectImageView;

public class FormerListActivity extends BaseActivity
{

    private RecyclerView listview;

    private FormerAdapter adapter;

    private ArrayList<Former> list;

    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_former_list);

        list = FormerDatabaseHelper.getAll();

        isEdit = getIntent().getBooleanExtra("edit", false);
        if (isEdit)
        {
            setBottomVisible();
            addBottomView();
        }

        initView();
    }

    private void addBottomView()
    {
        getBottomLeftView().setImageResource(R.drawable.add);
        getBottomLeftView().setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity, FormerActivity.class);
                startActivity(intent);

            }
        });

        ImageView setting = new TouchEffectImageView(mActivity, null);
        setting.setImageResource(R.drawable.dict);
        setting.setScaleType(ScaleType.FIT_XY);
        setting.setPadding(15, 15, 15, 15);
        addBottomRightView(setting,
                new LayoutParams(DisplayUtil.dip2px(mActivity, 48), DisplayUtil.dip2px(mActivity, 48)));
        setting.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity, AboutActivity.class);
                intent.putExtra("former", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        list = FormerDatabaseHelper.getAll();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    private void initView()
    {
        listview = (RecyclerView) findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new FormerAdapter();
        adapter.setList(list);
        adapter.setMyListener(new MyListener()
        {

            @Override
            public void onItemClick(int position)
            {
                if (isEdit)
                {
                    Intent intent = new Intent(mActivity, FormerActivity.class);
                    intent.putExtra("former", list.get(position));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mActivity, EditActivity.class);
                    intent.putExtra("former", list.get(position));
                    startActivity(intent);
                }
            }
        });
        listview.setAdapter(adapter);

    }
}
