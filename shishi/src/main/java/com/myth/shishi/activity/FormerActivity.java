package com.myth.shishi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.db.FormerDatabaseHelper;
import com.myth.shishi.entity.Former;
import com.myth.shishi.wiget.GCDialog;
import com.myth.shishi.wiget.GCDialog.OnCustomDialogListener;

public class FormerActivity extends BaseActivity
{

    private Former former;

    private boolean isEdit;

    private EditText name;

    private EditText yun;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_former);

        if (getIntent().hasExtra("former"))
        {
            isEdit = true;
            former = (Former) getIntent().getSerializableExtra("former");
        }
        else
        {
            former = new Former();
        }

        initView();
    }

    private void initView()
    {
        name = (EditText) findViewById(R.id.name);
        yun = (EditText) findViewById(R.id.yun);

        if (isEdit)
        {
            name.setText(former.getName());
            yun.setText(former.getYun());
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.delete).setVisibility(View.GONE);
        }

        findViewById(R.id.delete).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString(GCDialog.DATA_TITLE, getString(R.string.delete_title));
                bundle.putString(GCDialog.DATA_CONTENT, getString(R.string.delete_former_content));
                bundle.putString(GCDialog.CONFIRM_TEXT, getString(R.string.delete));
                new GCDialog(mActivity, new OnCustomDialogListener()
                {

                    @Override
                    public void onConfirm()
                    {
                        FormerDatabaseHelper.delete(former);
                        Toast.makeText(mActivity, "已删除", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancel()
                    {
                    }
                }, bundle).show();

            }
        });

        findViewById(R.id.button).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                former.setName(name.getText().toString().trim());
                former.setYun(yun.getText().toString().trim());
                if (isEdit)
                {
                    FormerDatabaseHelper.update(former);
                    Toast.makeText(mActivity, "已保存", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    FormerDatabaseHelper.add(former);
                    Toast.makeText(mActivity, "已添加", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}
