package com.myth.shishi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.util.OthersUtils;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("former")) {
            setContentView(R.layout.activity_former_help);
        } else {
            setContentView(R.layout.activity_about);

            findViewById(R.id.about_email).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mActivity).setItems(new String[]{"复制"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    OthersUtils.copy(getString(R.string.about_email), mActivity);
                                    Toast.makeText(mActivity, R.string.about_email_toast, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
            findViewById(R.id.about_qq).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mActivity).setItems(new String[]{"复制"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    OthersUtils.copy(getString(R.string.about_qq), mActivity);
                                    Toast.makeText(mActivity, R.string.about_qq_toast, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
            findViewById(R.id.about_wexin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mActivity).setItems(new String[]{"复制"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    OthersUtils.copy(getString(R.string.about_wexin), mActivity);
                                    Toast.makeText(mActivity, R.string.about_wexin_toast, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
        }
    }

}
