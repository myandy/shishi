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
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.util.OthersUtils;
import com.myth.shishi.wiget.CircleImageView;
import com.myth.shishi.wiget.TouchEffectImageView;

public class PoetryActivity extends BaseActivity
{

    private ArrayList<Poetry> ciList = new ArrayList<Poetry>();

    private TextView content;

    private Poetry poetry;

    private TextView title;

    CircleImageView shareView;
    
    private Author author;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry);
        
        setBottomVisible();

        ciList = PoetryDatabaseHelper.getAll();
        getRandomPoetry();

        initView();
    }

    private void getRandomPoetry()
    {
        poetry = ciList.get(new Random().nextInt(ciList.size()));
        author=AuthorDatabaseHelper.getAuthorByName(poetry.getAuthor());
    }

    private void initView()
    {
        LinearLayout topView = (LinearLayout) findViewById(R.id.right);
        LayoutParams param = new LayoutParams(DisplayUtil.dip2px(mActivity, 80), DisplayUtil.dip2px(mActivity, 120));
        shareView = new CircleImageView(mActivity,author.getColor(),R.drawable.share3_white);
        topView.addView(shareView, 1, param);
        
        shareView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity, ShareEditActivity.class);
                intent.putExtra("data", poetry);
                startActivity(intent);
            }
        });

        title = (TextView) findViewById(R.id.title);
        title.setTypeface(MyApplication.typeface);
        title.setText(poetry.getAuthor());

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

        findViewById(R.id.baike).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mActivity, WebviewActivity.class);
                intent.putExtra("string", poetry.getAuthor());
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
        title.setText(poetry.getAuthor());
        setColor();
        initContentView();
    }

    private void setColor()
    {
        shareView.setmColor(author.getColor());
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
        ((TextView) findViewById(R.id.author)).setText(poetry.getTitle() + "\n");

    }

}
