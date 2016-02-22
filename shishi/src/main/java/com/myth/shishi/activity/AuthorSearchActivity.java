package com.myth.shishi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.adapter.AuthorAdapter;
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.listener.MyListener;

import java.util.ArrayList;

public class AuthorSearchActivity extends BaseActivity
{

    private View clear;

    private RecyclerView listview;

    private AuthorAdapter adapter;

    private ArrayList<Author> ciList;

    private ArrayList<Author> sortList;

    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cipai);
        setBottomGone();
        ciList = AuthorDatabaseHelper.getAll();
        initView();
        refreshData();
    }

    private void refreshData()
    {
        String word = search.getText().toString().trim();
        if (TextUtils.isEmpty(word))
        {
            sortList = ciList;
        }
        else
        {
            sortList = searchAuthor(word);
        }
        adapter.setList(sortList);
        adapter.notifyDataSetChanged();

    }

    private ArrayList<Author> searchAuthor(String word)
    {
        ArrayList<Author> list = new ArrayList<Author>();
        for (Author author : ciList)
        {
            if (author.getAuthor().contains(word))
            {
                list.add(author);
            }
        }
        return list;
    }

    private void initView()
    {
        listview = (RecyclerView) findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new AuthorAdapter(mActivity);
        adapter.setMyListener(new MyListener()
        {

            @Override
            public void onItemClick(int position)
            {
                Intent intent = new Intent(mActivity, AuthorPageActivity.class);
                intent.putExtra("author", sortList.get(position));
                startActivity(intent);
            }
        });
        listview.setAdapter(adapter);


        search = (EditText) findViewById(R.id.search);

        search.setHint(R.string.search_cipai_hint);
        search.setHintTextColor(getResources().getColor(R.color.black_hint));
        search.setTextColor(getResources().getColor(R.color.black));
        findViewById(R.id.exit).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                search.setText("");
                search.requestFocus();
            }
        });
        search.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (TextUtils.isEmpty(s.toString()))
                {
                    clear.setVisibility(View.GONE);
                }
                else
                {
                    clear.setVisibility(View.VISIBLE);
                }
                refreshData();
            }
        });
    }
}
