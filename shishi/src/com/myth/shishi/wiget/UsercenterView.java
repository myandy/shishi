package com.myth.shishi.wiget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.activity.PoetrySearchActivity;
import com.myth.shishi.activity.SignActivity;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.db.WritingDatabaseHelper;
import com.myth.shishi.util.ResizeUtil;

public class UsercenterView extends RelativeLayout
{

    private Context mContext;

    private TextView sign;

    private TextView point;

    private TextView collect;

    private TextView writing;

    public UsercenterView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public UsercenterView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public UsercenterView(Context context)
    {
        super(context);
        mContext = context;
        initView();
    }

    private void layoutItemContainer(View itemContainer)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemContainer.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = ResizeUtil.resize(mContext, 864);
        itemContainer.setLayoutParams(params);
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.layout_usercenter, null);
        
        TextView title = (TextView) root.findViewById(R.id.title);
        title.setTypeface(MyApplication.getTypeface());

        final TextView username = (TextView) root.findViewById(R.id.username_text);
        root.findViewById(R.id.username).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                final EditText et = new EditText(mContext);
                new AlertDialog.Builder(mContext).setTitle("请输入用户名").setIcon(android.R.drawable.ic_dialog_info).setView(
                        et).setPositiveButton("确定", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        username.setText(et.getText().toString().trim());
                        MyApplication.setDefaultUserName(mContext, et.getText().toString().trim());
                    }
                }).setNegativeButton("取消", null).show();

            }
        });
        String name = MyApplication.getDefaultUserName(mContext);
        if (!TextUtils.isEmpty(name))
        {
            username.setText(name);
        }

        point = (TextView) root.findViewById(R.id.point_text);
        root.findViewById(R.id.point).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            }
        });

        sign = (TextView) root.findViewById(R.id.sign_btn_text);
        root.findViewById(R.id.sign).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, SignActivity.class);
                mContext.startActivity(intent);
            }
        });

        writing = (TextView) root.findViewById(R.id.writing_text);
        root.findViewById(R.id.writing).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            }
        });

        collect = (TextView) root.findViewById(R.id.collect_text);
        root.findViewById(R.id.collect).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, PoetrySearchActivity.class);
                intent.putExtra("collect", true);
                mContext.startActivity(intent);
            }
        });

        refresh();
        addView(root, new LayoutParams(-1, -1));
    }

    public void refresh()
    {
        if (SignActivity.isSign(mContext))
        {
            sign.setText("今天已签到");
        }
        else
        {
            sign.setText("点击签到");
        }

        point.setText("积分：" + MyApplication.getDefaultPoint(mContext));

        int writingNum = WritingDatabaseHelper.getAllWriting(mContext).size();
        int collectNum = PoetryDatabaseHelper.getAllCollect().size();

        writing.setText("作品数：" + writingNum);
        collect.setText("收藏数：" + collectNum);

    }

}
