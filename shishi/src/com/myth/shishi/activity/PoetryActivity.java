package com.myth.shishi.activity;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

    private PopupWindow menu;

    int[] location;

    private View menuView;

    private TouchEffectImageView more;

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
        author = AuthorDatabaseHelper.getAuthorByName(poetry.getAuthor());
    }

    private void initView()
    {
        LinearLayout topView = (LinearLayout) findViewById(R.id.right);
        LayoutParams param = new LayoutParams(DisplayUtil.dip2px(mActivity, 80), DisplayUtil.dip2px(mActivity, 120));
        shareView = new CircleImageView(mActivity, author.getColor(), R.drawable.share3_white);
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
        ((TextView) findViewById(R.id.note)).setTypeface(MyApplication.typeface);

        ((TextView) findViewById(R.id.author)).setTypeface(MyApplication.typeface);

        title.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(mActivity, AuthorPageActivity.class);
                intent.putExtra("author", author);
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
        view.setPadding(15, 15, 15, 15);
        ViewGroup.LayoutParams lps = new LayoutParams(DisplayUtil.dip2px(mActivity, 30.4), DisplayUtil.dip2px(
                mActivity, 24));
        addBottomRightView(view, lps);
        view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                getRandomPoetry();
                refreshRandomView();
            }
        });

        more = new TouchEffectImageView(mActivity, null);
        more.setImageResource(R.drawable.setting);
        more.setScaleType(ScaleType.FIT_XY);
        addBottomRightView(more, new LayoutParams(DisplayUtil.dip2px(mActivity, 48), DisplayUtil.dip2px(mActivity, 48)));
        more.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showMenu();
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
        if (note != null && note.length() > 10)
        {
            ((TextView) findViewById(R.id.note)).setText(note);
        }
        content.setText(poetry.getPoetry());
        ((TextView) findViewById(R.id.author)).setText(poetry.getTitle() + "\n");
        setTextSize();

    }

    public void isAddTextSize(boolean add)
    {
        int size = MyApplication.getDefaultTextSize(mActivity);
        if (add)
        {
            size += 2;
        }
        else
        {
            size -= 2;
        }
        MyApplication.setDefaultTextSize(mActivity, size);
        setTextSize();
    }

    public void setTextSize()
    {

        int size = MyApplication.getDefaultTextSize(mActivity);
        ((TextView) findViewById(R.id.author)).setTextSize(size);
        content.setTextSize(size);
        ((TextView) findViewById(R.id.note)).setTextSize(size - 2);
    }

    private void showMenu()
    {
        if (menu == null)
        {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            menuView = inflater.inflate(R.layout.dialog_poetry, null);

            // PopupWindow定义，显示view，以及初始化长和宽
            menu = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            // 必须设置，否则获得焦点后页面上其他地方点击无响应
            menu.setBackgroundDrawable(new BitmapDrawable());
            // 设置焦点，必须设置，否则listView无法响应
            menu.setFocusable(true);
            // 设置点击其他地方 popupWindow消失
            menu.setOutsideTouchable(true);

            // 让view可以响应菜单事件
            menuView.setFocusableInTouchMode(true);

            menuView.setOnKeyListener(new OnKeyListener()
            {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_MENU)
                    {
                        if (menu != null)
                        {
                            menu.dismiss();
                        }
                        return true;
                    }
                    return false;
                }
            });
            location = new int[2];

            menuView.findViewById(R.id.tv1).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    isAddTextSize(true);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            menuView.findViewById(R.id.tv2).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    isAddTextSize(false);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            TextView collect = (TextView) menuView.findViewById(R.id.tv3);
            if (PoetryDatabaseHelper.isCollect(poetry.getPoetry()))
            {
                collect.setText("取消收藏");
            }
            else
            {
                collect.setText("收藏");
            }
            collect.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    boolean isCollect = PoetryDatabaseHelper.isCollect(poetry.getPoetry());
                    PoetryDatabaseHelper.updateCollect(poetry.getPoetry(), !isCollect);
                    if (isCollect)
                    {
                        Toast.makeText(mActivity, "已取消收藏", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(mActivity, "已收藏", Toast.LENGTH_LONG).show();
                    }
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            menuView.findViewById(R.id.tv4).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mActivity, PoetrySearchActivity.class);
                    intent.putExtra("author", author);
                    mActivity.startActivity(intent);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            menuView.findViewById(R.id.tv5).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mActivity, WebviewActivity.class);
                    intent.putExtra("string", poetry.getAuthor());
                    mActivity.startActivity(intent);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            menuView.findViewById(R.id.tv6).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mActivity, WebviewActivity.class);
                    intent.putExtra("string", poetry.getTitle());
                    mActivity.startActivity(intent);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });
            menuView.findViewById(R.id.tv7).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    OthersUtils.copy(title.getText() + "\n" + content.getText(), mActivity);
                    Toast.makeText(mActivity, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });

            menuView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int popupWidth = menuView.getMeasuredWidth();
            int popupHeight = menuView.getMeasuredHeight();

            more.getLocationOnScreen(location);

            location[0] = location[0] + more.getWidth() / 2 - popupWidth / 2;
            location[1] = location[1] - popupHeight;

            menu.showAtLocation(more, Gravity.NO_GRAVITY, location[0], location[1]);
            // 显示在某个位置

        }
        else
        {
            TextView collect = (TextView) menuView.findViewById(R.id.tv3);
            if (PoetryDatabaseHelper.isCollect(poetry.getPoetry()))
            {
                collect.setText("取消收藏");
            }
            else
            {
                collect.setText("收藏");
            }
            menu.showAtLocation(more, Gravity.NO_GRAVITY, location[0], location[1]);
        }

    }

}
