package com.myth.shishi.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.adapter.DuiShiAdapter;
import com.myth.shishi.util.HttpUtil;
import com.myth.shishi.wiget.DuishiEditView;
import com.myth.shishi.wiget.GProgressDialog;

public class DuiShiActivity extends BaseActivity
{

    private DuiShiAdapter adapter;

    RecyclerView listview;

    private final static String URL = "http://superapp.cloudapp.net/couplets/api/xialian?";

    private final static int LOAD_SUCCESS = 1;

    private final static int LOAD_FAILED = 2;

    private final static int LOAD_SUCCESS_RE = 3;

    private DuishiEditView editView;

    private int count;

    private RelativeLayout topView;

    private String s;

    private GProgressDialog progress;

    private Handler mhandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case LOAD_SUCCESS:
                    adapter.notifyDataSetChanged();
                    if (editView == null)
                    {
                        editView = new DuishiEditView(mActivity, count);
                        topView.addView(editView);
                        topView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        editView.refresh(count);
                    }
                    progress.dismiss();
                    break;
                case LOAD_FAILED:
                    Toast.makeText(mActivity, "请求出错", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    break;
                case LOAD_SUCCESS_RE:
                    adapter.notifyDataSetChanged();
                    progress.dismiss();
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duishi);
        initView();
    }

    private void initView()
    {
        progress = new GProgressDialog(mActivity);
        topView = (RelativeLayout) findViewById(R.id.top);
        topView.setVisibility(View.GONE);
        final EditText et = (EditText) findViewById(R.id.et);
        Button button = (Button) findViewById(R.id.button);
        listview = (RecyclerView) findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new DuiShiAdapter();
        listview.setAdapter(adapter);
        button.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                progress.show();
                new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        s = et.getText().toString().trim();
                        if (!TextUtils.isEmpty(s))
                        {
                            count = s.length();
                            String url = URL + "shanglian=" + URLEncoder.encode(s);
                            String response = HttpUtil.httpGet(url, "");
                            if (response != null)
                            {
                                List<String> list = getData(response);
                                adapter.setList(list);
                                mhandler.sendEmptyMessage(LOAD_SUCCESS);
                            }
                            else
                            {
                                mhandler.sendEmptyMessage(LOAD_FAILED);
                            }

                        }

                    }
                }).start();

            }
        });
        findViewById(R.id.refresh).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                progress.show();
                new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        String url = URL + "shanglian=" + URLEncoder.encode(s);
                        url += "&placeholder=" + editView.getText();
                        String response = HttpUtil.httpGet(url, "");
                        if (response != null)
                        {
                            List<String> list = getData(response);
                            adapter.setList(list);
                            mhandler.sendEmptyMessage(LOAD_SUCCESS_RE);
                        }
                        else
                        {
                            mhandler.sendEmptyMessage(LOAD_FAILED);
                        }

                    }
                }).start();

            }
        });

    }

    private List<String> getData(String s)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("set"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("set");
                for (int i = 0; i < jsonArray.length(); i++)
                {

                    list.add(jsonArray.getString(i));

                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return list;
    }

}
