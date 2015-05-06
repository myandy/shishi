package com.myth.shishi.activity;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.util.OthersUtils;
import com.myth.shishi.wiget.CircleShareView;
import com.myth.shishi.wiget.TouchEffectImageView;

public class PoetryActivity extends BaseActivity
{

    private ArrayList<Poetry> ciList = new ArrayList<Poetry>();

    private TextView content;

    private Poetry poetry;

    private TextView title;
    
    CircleShareView editView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ci);

        ciList = PoetryDatabaseHelper.getAll();
        getRandomPoetry();

        initView();
    }

    private void getRandomPoetry()
    {
        poetry = ciList.get(new Random().nextInt(ciList.size()));
    }

    private void initView()
    {
        LinearLayout topView = (LinearLayout) findViewById(R.id.right);
        LayoutParams param = new LayoutParams(DisplayUtil.dip2px(mActivity, 80), DisplayUtil.dip2px(mActivity, 120));
         editView = new CircleShareView(mActivity, MyApplication.getRandomColor());
        topView.addView(editView, 1, param);

        title = (TextView) findViewById(R.id.title);
        title.setTypeface(MyApplication.typeface);
        title.setText(poetry.getTitle());

        content = (TextView) findViewById(R.id.content);
        content.setTypeface(MyApplication.typeface);
        content.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setItems(new String[] {"复制文本"},
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if (which == 0)
                                {
                                    OthersUtils.copy(title.getText() + "\n" + content.getText(), mActivity);
                                    Toast.makeText(mActivity, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        ((TextView) findViewById(R.id.note)).setTypeface(MyApplication.typeface);

        findViewById(R.id.note).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setItems(new String[] {"复制文本"},
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if (which == 0)
                                {
                                    OthersUtils.copy(title.getText() + "\n" + content.getText(), mActivity);
                                    Toast.makeText(mActivity, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        ((TextView) findViewById(R.id.author)).setTypeface(MyApplication.typeface);

        findViewById(R.id.share).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity, ShareEditActivity.class);
                intent.putExtra("poetry", poetry);
                startActivity(intent);
            }
        });

        initBottomRightView();

        initContentView();

    }

    private void initBottomRightView()
    {
        ImageView view = new TouchEffectImageView(mActivity, null);
        view.setImageResource(R.drawable.random);
        view.setScaleType(ScaleType.FIT_XY);
        view.setPadding(5, 5, 5, 5);
        addBottomRightView(view,
                new LayoutParams(DisplayUtil.dip2px(mActivity, 30.4), DisplayUtil.dip2px(mActivity, 24)));
        view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                getRandomPoetry();
                refreshRandomView();
            }
        });

    }

    private void refreshRandomView()
    {
        title.setText(poetry.getTitle());
        setColor();
        initContentView();
    }

    private void setColor()
    {
        editView.setmColor( MyApplication.getRandomColor());
    }

    private void initContentView()
    {

        String note = poetry.getIntro();
        if (note == null)
        {
            note = "";
        }
        content.setText(poetry.getPoetry());
        ((TextView) findViewById(R.id.note)).setText(note);
        ((TextView) findViewById(R.id.author)).setText(poetry.getAuthor() + "\n");
    }

}
