package com.myth.shishi.activity;

import android.os.Bundle;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;

public class AboutActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("former"))
        {
            setContentView(R.layout.activity_former_help);
        }
        else
        {
            setContentView(R.layout.activity_about);
        }
    }

}
