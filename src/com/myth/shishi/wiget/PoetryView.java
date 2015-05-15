package com.myth.shishi.wiget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.activity.ShareEditActivity;
import com.myth.shishi.activity.WebviewActivity;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.util.OthersUtils;
import com.myth.shishi.wiget.CircleImageView;

public class PoetryView extends LinearLayout
{

    private Author author;

    private Poetry poetry;

    private Context mContext;

    private View root;

    private String page;

    private View collect;

    public void setData(Author author, Poetry poetry)
    {
        this.poetry = poetry;
        this.author = author;
    }

    public PoetryView(Context context)
    {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.layout_poetry, null);
        initView(root);
        addView(root);
    }

    public PoetryView(Context context, Author author, Poetry poetry, String page)
    {
        super(context);
        this.poetry = poetry;
        this.author = author;
        this.page = page;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.layout_poetry, null);
        initView(root);
        addView(root);
    }

    private TextView content;

    private TextView title;

    CircleImageView shareView;

    private void initView(View root)
    {
        LinearLayout topView = (LinearLayout) root.findViewById(R.id.right);
        LayoutParams param = new LayoutParams(DisplayUtil.dip2px(mContext, 80), DisplayUtil.dip2px(mContext, 120));
        shareView = new CircleImageView(mContext, author.getColor(), R.drawable.share3_white);
        topView.addView(shareView, 1, param);

        shareView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, ShareEditActivity.class);
                intent.putExtra("data", poetry);
                mContext.startActivity(intent);
            }
        });

        title = (TextView) root.findViewById(R.id.title);
        title.setTypeface(MyApplication.typeface);
        title.setText(poetry.getAuthor());

        content = (TextView) root.findViewById(R.id.content);
        content.setTypeface(MyApplication.typeface);
        content.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mContext).setItems(new String[] {"复制文本"}, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if (which == 0)
                        {
                            OthersUtils.copy(title.getText() + "\n" + content.getText(), mContext);
                            Toast.makeText(mContext, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).show();

            }
        });
        ((TextView) root.findViewById(R.id.note)).setTypeface(MyApplication.typeface);

        root.findViewById(R.id.note).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mContext).setItems(new String[] {"复制文本"}, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if (which == 0)
                        {
                            OthersUtils.copy(title.getText() + "\n" + content.getText(), mContext);
                            Toast.makeText(mContext, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }).show();

            }
        });

        ((TextView) root.findViewById(R.id.author)).setTypeface(MyApplication.typeface);

        root.findViewById(R.id.baike).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, WebviewActivity.class);
                intent.putExtra("string", poetry.getTitle());
                mContext.startActivity(intent);
            }
        });

        ((TextView) root.findViewById(R.id.page)).setText(page);

        collect = root.findViewById(R.id.collect);

        collect.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                PoetryDatabaseHelper.updateCollect(poetry.getPoetry(),
                        !PoetryDatabaseHelper.isCollect(poetry.getPoetry()));
                refreshCollect();

            }
        });

        refreshView();

    }

    protected void refreshCollect()
    {
        if (PoetryDatabaseHelper.isCollect(poetry.getPoetry()))
        {
            collect.setBackgroundResource(R.drawable.collect);
        }
        else
        {
            collect.setBackgroundResource(R.drawable.no_collect);
        }

    }

    private void refreshView()
    {
        shareView.setmColor(author.getColor());
        String note = poetry.getIntro();
        if (note == null)
        {
            note = "";
        }
        content.setText(poetry.getPoetry());
        ((TextView) root.findViewById(R.id.note)).setText(note);
        ((TextView) root.findViewById(R.id.author)).setText(poetry.getTitle() + "\n");

    }

}
