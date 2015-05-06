package com.myth.shishi.wiget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myth.shishi.R;
import com.myth.shishi.MyApplication;
import com.myth.shishi.activity.EditActivity;
import com.myth.shishi.activity.MainActivity;
import com.myth.shishi.activity.ShareActivity;
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.db.FormerDatabaseHelper;
import com.myth.shishi.db.WritingDatabaseHelper;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.util.DateUtils;
import com.myth.shishi.util.ResizeUtil;
import com.myth.shishi.util.StringUtils;
import com.myth.shishi.wiget.GCDialog.OnCustomDialogListener;

public class WritingView extends RelativeLayout
{

    private Context mContext;

    private Writing writing;

    public WritingView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public WritingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public WritingView(Context context, Writing writing)
    {
        super(context);
        this.writing = writing;
        mContext = context;
        if (writing.getFormer() == null)
        {
            writing.setFormer(FormerDatabaseHelper.getFormerByName(writing.getFormerName()));
        }
        initView();
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.layout_writing, null);

        View content = root.findViewById(R.id.content);

        if (StringUtils.isNumeric(writing.getBgimg()))
        {
            content.setBackgroundResource(MyApplication.bgimgList[Integer.parseInt(writing.getBgimg())]);
        }
        else
        {
            content.setBackgroundDrawable(new BitmapDrawable(getResources(), writing.getBgimg()));
        }

        layoutItemContainer(content);

        TextView time = (TextView) root.findViewById(R.id.time);

        TextView title = (TextView) root.findViewById(R.id.title);

        TextView text = (TextView) root.findViewById(R.id.text);

        time.setText(DateUtils.longToFormat(writing.getUpdate_dt(), DateUtils.YMD_HM_FORMAT));

        title.setText(writing.getFormer().getName());
        text.setText(writing.getText());

        title.setTypeface(MyApplication.typeface);
        text.setTypeface(MyApplication.typeface);

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setItems(new String[] {"分享", "编辑", "删除"},
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if (which == 0)
                        {
                            Intent intent = new Intent(mContext, ShareActivity.class);
                            intent.putExtra("writing", writing);
                            mContext.startActivity(intent);
                        }
                        else if (which == 1)
                        {
                            Intent intent = new Intent(mContext, EditActivity.class);
                            intent.putExtra("writing", writing);
                            mContext.startActivity(intent);
                        }
                        else if (which == 2)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString(GCDialog.DATA_TITLE, mContext.getString(R.string.delete_title));
                            bundle.putString(GCDialog.DATA_CONTENT, mContext.getString(R.string.delete_content));
                            bundle.putString(GCDialog.CONFIRM_TEXT, mContext.getString(R.string.delete));
                            new GCDialog(mContext, new OnCustomDialogListener()
                            {

                                @Override
                                public void onConfirm()
                                {
                                    WritingDatabaseHelper.deleteWriting(mContext, writing);
                                    ((MainActivity) mContext).refresh();
                                }

                                @Override
                                public void onCancel()
                                {
                                }
                            }, bundle).show();
                        }

                        dialog.dismiss();

                    }
                }).create();

        content.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }
                else
                {
                    dialog.show();
                }

            }
        });

        addView(root, new LayoutParams(-1, -1));

    }

    private void layoutItemContainer(View itemContainer)
    {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemContainer.getLayoutParams();
        params.width = ResizeUtil.resize(mContext, 540);
        params.height = ResizeUtil.resize(mContext, 540);
        itemContainer.setLayoutParams(params);
    }

}
