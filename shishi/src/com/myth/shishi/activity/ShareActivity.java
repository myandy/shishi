package com.myth.shishi.activity;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.util.FileUtils;
import com.myth.shishi.util.OthersUtils;
import com.myth.shishi.util.ResizeUtil;
import com.myth.shishi.util.StringUtils;
import com.myth.shishi.wiget.TouchEffectImageView;

public class ShareActivity extends BaseActivity
{

    private Writing writing;

    private LinearLayout content;

    private PopupWindow menu;

    int[] location;

    private View menuView;

    private TextView title;

    private TextView text;

    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setBottomVisible();

        writing = (Writing) getIntent().getSerializableExtra("writing");
        if (writing == null)
        {
            finish();
        }

        initView();

        setting = new TouchEffectImageView(mActivity, null);
        setting.setImageResource(R.drawable.setting);
        setting.setScaleType(ScaleType.FIT_XY);
        addBottomRightView(setting,
                new LayoutParams(DisplayUtil.dip2px(mActivity, 48), DisplayUtil.dip2px(mActivity, 48)));
        setting.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showMenu();
            }
        });
    }

    private void initView()
    {
        content = (LinearLayout) findViewById(R.id.content);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        content.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(mActivity).setItems(new String[] {"复制文本", "保存图片", "分享"},
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {

                                if (which == 0)
                                {
                                    OthersUtils.copy(title.getText() + "\n" + text.getText(), mActivity);
                                    Toast.makeText(mActivity, R.string.copy_text_done, Toast.LENGTH_SHORT).show();
                                }
                                else if (which == 1)
                                {
                                    String filePath = saveImage();
                                    if (!TextUtils.isEmpty(filePath))
                                    {
                                        Toast.makeText(mActivity, "图片已保存在：" + filePath, Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (which == 2)
                                {
                                    String filePath = saveImage();
                                    if (!TextUtils.isEmpty(filePath))
                                    {
                                        OthersUtils.shareMsg(mActivity, "词Ci", "share", "content", filePath);
                                    }

                                }
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        layoutItemContainer(content);
        title.setText(writing.getTitle());
        text.setText(writing.getText());
        title.setTypeface(MyApplication.typeface);
        text.setTypeface(MyApplication.typeface);
        setTextSize();
        setGravity();
        setPadding();

        if (StringUtils.isNumeric(writing.getBgimg()))
        {
            content.setBackgroundResource(MyApplication.bgimgList[Integer.parseInt(writing.getBgimg())]);
        }
        else if (writing.getBitmap() != null)
        {
            content.setBackgroundDrawable(new BitmapDrawable(getResources(), writing.getBitmap()));
        }
        else
        {
            content.setBackgroundDrawable(new BitmapDrawable(getResources(), writing.getBgimg()));
        }

        scaleRotateIn(content, 1000, 0);
    }

    private void layoutItemContainer(View itemContainer)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemContainer.getLayoutParams();
        params.width = ResizeUtil.resize(mActivity, 640);
        params.height = ResizeUtil.resize(mActivity, 640);
        itemContainer.setLayoutParams(params);
    }

    public final int rela1 = Animation.RELATIVE_TO_SELF;

    public void scaleRotateIn(View view, long durationMillis, long delayMillis)
    {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, rela1, 0.5f, rela1, 0.5f);
        RotateAnimation animation2 = new RotateAnimation(0, 357, rela1, 0.5f, rela1, 0.5f);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(animation1);
        animation.addAnimation(animation2);
        animation.setFillAfter(true);
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
        view.setAnimation(animation);
    }

    private String saveImage()
    {
        String filePath = null;
        try
        {
            String filename = writing.getUpdate_dt() + "";
            filePath = FileUtils.saveFile(OthersUtils.createViewBitmap(content), filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return filePath;
    }

    public void isAddTextSize(boolean add)
    {
        int size = MyApplication.getDefaultShareSize(mActivity);
        if (add)
        {
            size += 2;
        }
        else
        {
            size -= 2;
        }
        MyApplication.setDefaultShareSize(mActivity, size);
        setTextSize();
    }

    public void setTextSize()
    {
        int size = MyApplication.getDefaultShareSize(mActivity);
        text.setTextSize(size);
        title.setTextSize(size + 2);
    }

    private void setGravity(boolean isCenter)
    {
        MyApplication.setDefaultShareGravity(mActivity, isCenter);
        setGravity();
    }

    private void setGravity()
    {
        boolean isCenter = MyApplication.getDefaultShareGravity(mActivity);
        if (isCenter)
        {
            text.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        else
        {
            text.setGravity(Gravity.LEFT);
        }
    }

    private void setPadding()
    {
        int margin = MyApplication.getDefaultSharePadding(mActivity);
        LinearLayout.LayoutParams lps = (android.widget.LinearLayout.LayoutParams) text.getLayoutParams();
        lps.leftMargin = margin;
        text.setLayoutParams(lps);
    }

    private void setPadding(boolean isAdd)
    {
        int margin = MyApplication.getDefaultSharePadding(mActivity);
        if (isAdd)
        {
            margin += 8;
        }
        else
        {
            margin -= 8;
        }
        MyApplication.setDefaultSharePadding(mActivity, margin);
        setPadding();
    }

    private void showMenu()
    {
        if (menu == null)
        {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            menuView = inflater.inflate(R.layout.dialog_share, null);

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
            menuView.findViewById(R.id.tv3).setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    setGravity(true);
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
                    setGravity(false);
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
                    setPadding(false);
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
                    setPadding(true);
                    if (menu != null)
                    {
                        menu.dismiss();
                    }
                }
            });

            menuView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int popupWidth = menuView.getMeasuredWidth();
            int popupHeight = menuView.getMeasuredHeight();

            setting.getLocationOnScreen(location);

            location[0] = location[0] + setting.getWidth() / 2 - popupWidth / 2;
            location[1] = location[1] - popupHeight;

            menu.showAtLocation(setting, Gravity.NO_GRAVITY, location[0], location[1]);
            // 显示在某个位置

        }
        else
        {
            menu.showAtLocation(setting, Gravity.NO_GRAVITY, location[0], location[1]);
        }

    }

}
