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

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.adapter.DuiShiAdapter;
import com.myth.shishi.util.HttpUtil;

public class DuiShiActivity extends BaseActivity
{

    private DuiShiAdapter adapter;

    RecyclerView listview;

    private final static String URL = "http://superapp.cloudapp.net/couplets/api/xialian?";

    private final static int LOAD_SUCCESS = 1;

    private Handler mhandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case LOAD_SUCCESS:
                    adapter.notifyDataSetChanged();
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
                new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        String s = et.getText().toString().trim();
                        if (!TextUtils.isEmpty(s))
                        {
                            String url = URL + "shanglian=" + URLEncoder.encode(s);
                            String response = HttpUtil.httpGet(url, "");
                            List<String> list = getData(response);
                            adapter.setList(list);
                            mhandler.sendEmptyMessage(LOAD_SUCCESS);

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
