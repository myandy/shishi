package com.myth.shishi.wiget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.myth.shishi.R;
import com.myth.shishi.util.DisplayUtil;

public class ToolsDialog extends Dialog
{
    private Context context;

    private LayoutInflater inflater;

    public ToolsDialog(Context context)
    {
        super(context, R.style.detail_popup_dialog_style);
        this.context = context;
        initView();
    }

    @Override
    public void show()
    {
        setCanceledOnTouchOutside(true);
        Window w = getWindow();
        w.setGravity(Gravity.BOTTOM);
        w.getAttributes().width = DisplayUtil.screenHeightPx(getContext());
        w.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setWindowAnimations(R.style.detail_popwindow_anim_style);
        super.show();
    }

    private void initView()
    {
        inflater = LayoutInflater.from(getContext());
    }

}
