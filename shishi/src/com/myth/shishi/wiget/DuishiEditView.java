package com.myth.shishi.wiget;

import java.util.ArrayList;
import java.util.List;

import com.myth.shishi.R;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.util.OthersUtils;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DuishiEditView extends LinearLayout
{

    private Context mContext;

    private int count;

    List<EditText> eList;

    public DuishiEditView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public DuishiEditView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DuishiEditView(Context context)
    {
        super(context);
        mContext = context;
        initView();
    }

    public DuishiEditView(Context context, int count)
    {
        super(context);
        this.count = count;
        mContext = context;
        initView();
    }

    private void initView()
    {
        removeAllViews();
        setOrientation(LinearLayout.HORIZONTAL);

        eList = new ArrayList<EditText>();

        LinearLayout.LayoutParams lps = new LayoutParams(DisplayUtil.dip2px(mContext, 34), DisplayUtil.dip2px(mContext,
                34));

        lps.setMargins(20, 0, 0, 0);
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < count; i++)
        {
            EditText et = (EditText) inflater.inflate(R.layout.edittext2, null);

            eList.add(et);
            addView(et, lps);
        }
    }

    public void refresh(int count)
    {
        this.count = count;
        initView();
    }

    public String getText()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            String s = eList.get(i).getText().toString().trim();
            if (TextUtils.isEmpty(s))
            {
                sb.append("0");
            }
            else
            {
                sb.append(s);
            }
        }
        return sb.toString();
    }

}
