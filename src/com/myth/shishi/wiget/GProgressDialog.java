package com.myth.shishi.wiget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.myth.shishi.R;

public class GProgressDialog extends Dialog
{

    public GProgressDialog(Context context)
    {
        super(context);
        initUI();
    }

    public GProgressDialog(Context context, String message)
    {
        super(context);
        initUI();
        msg.setText(message);
    }

    private void initUI()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.gprogress_dialog);
        msg = (TextView) findViewById(R.id.msg);
    }

    private TextView msg;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
    }

    public void setMsg(String message)
    {
        msg.setText(message);
    }

    @Override
    public void dismiss()
    {
        if (isShowing())
        {
            super.dismiss();
        }
    }

}
