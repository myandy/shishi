package com.myth.shishi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.adapter.ImageAdapter;
import com.myth.shishi.entity.Former;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.util.ResizeUtil;
import com.myth.shishi.wiget.HorizontalListView;

public class ChangeBackgroundFragment extends Fragment
{

    private Context mContext;

    private LinearLayout content;

    private TextView text;

    private Former former;

    private Writing writing;

    private int bg_index = 0;

    public ChangeBackgroundFragment()
    {
    }

    public static ChangeBackgroundFragment getInstance(Former former, Writing writing)
    {
        ChangeBackgroundFragment fileViewFragment = new ChangeBackgroundFragment();
        fileViewFragment.former = former;
        fileViewFragment.writing = writing;
        return fileViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_background, null);
        initViews(view);
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refresh();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        save();
    }

    public void save()
    {
        writing.setBgimg(bg_index + "");
    }

    private void refresh()
    {
        text.setText(writing.getText());
        content.setBackgroundResource(MyApplication.bgimgList[bg_index]);
    }

    private void initViews(View view)
    {
        HorizontalListView imgs = (HorizontalListView) view.findViewById(R.id.imgs);

        ImageAdapter adapter = new ImageAdapter(mContext);
        imgs.setAdapter(adapter);
        imgs.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                bg_index = position;
                content.setBackgroundResource(MyApplication.bgimgList[position]);
            }
        });

        content = (LinearLayout) view.findViewById(R.id.content);
        layoutItemContainer(content);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(former.getName());
        text = (TextView) view.findViewById(R.id.text);
        title.setTypeface(MyApplication.typeface);
        text.setTypeface(MyApplication.typeface);
    }

    private void layoutItemContainer(View itemContainer)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemContainer.getLayoutParams();
        params.width = ResizeUtil.resize(mContext, 720);
        params.height = ResizeUtil.resize(mContext, 720);
        itemContainer.setLayoutParams(params);
    }
}
