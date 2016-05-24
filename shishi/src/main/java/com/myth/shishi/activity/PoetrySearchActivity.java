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
import com.myth.shishi.adapter.PoetryAdapter;
import com.myth.shishi.db.AuthorDatabaseHelper;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.db.WritingDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.listener.MyListener;

import java.util.ArrayList;

public class PoetrySearchActivity extends BaseActivity {

    private View clear;

    private RecyclerView listview;

    private PoetryAdapter adapter;

    private ArrayList<Poetry> pList;

    private ArrayList<Writing> writings;

    private ArrayList<Poetry> sortList;

    EditText search;

    private Author author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cipai);
        setBottomGone();

        if (getIntent().hasExtra("author")) {
            author = (Author) getIntent().getSerializableExtra("author");
            pList = PoetryDatabaseHelper.getAllByAuthor(author.getAuthor());
        } else if (getIntent().hasExtra("collect")) {
            pList = PoetryDatabaseHelper.getAllCollect();
        } else if (getIntent().hasExtra("self")) {
            writings = WritingDatabaseHelper.getAllWriting();
            pList = Writing.getPoetryList(writings);
        } else {
            pList = PoetryDatabaseHelper.getAll();
        }
        initView();
        refreshData();
    }

    private void refreshData() {
        String word = search.getText().toString().trim();
        if (TextUtils.isEmpty(word)) {
            sortList = pList;
        } else {
            sortList = searchPoetry(word);
        }
        adapter.setList(sortList);
        adapter.notifyDataSetChanged();
        adapter.setMyListener(new MyListener() {

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mActivity, AuthorPageActivity.class);
                intent.putExtra("id", sortList.get(position).getId());
                if (writings == null) {
                    if (author == null) {
                        intent.putExtra("author", AuthorDatabaseHelper
                                .getAuthorByName(sortList.get(position)
                                        .getAuthor()));
                    } else {
                        intent.putExtra("author", author);
                    }
                }
                startActivity(intent);
//                finish();

            }
        });
    }

    private ArrayList<Poetry> searchPoetry(String word) {
        ArrayList<Poetry> list = new ArrayList<Poetry>();
        for (Poetry author : pList) {
            if (author.getTitle().contains(word)
                    || author.getPoetry().contains(word)) {
                list.add(author);
            }
        }
        return list;
    }

    private int findPosition(Poetry poetry) {
        for (int i = 0; i < pList.size(); i++) {
            if (poetry.getTitle().equals(pList.get(i).getTitle())
                    && poetry.getPoetry().equals(pList.get(i).getPoetry())) {
                return i;
            }
        }
        return 0;
    }

    private void initView() {
        listview = (RecyclerView) findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                mActivity);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new PoetryAdapter(mActivity);
        adapter.setMyListener(new MyListener() {

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mActivity, AuthorPageActivity.class);
                intent.putExtra("index", pList.get(position).getId());
                if (writings == null) {
                    if (author == null) {
                        intent.putExtra("author", AuthorDatabaseHelper
                                .getAuthorByName(pList.get(position)
                                        .getAuthor()));
                    } else {
                        intent.putExtra("author", author);
                    }
                }
                startActivity(intent);
//                finish();

            }
        });
        listview.setAdapter(adapter);

        search = (EditText) findViewById(R.id.search);

        search.setHint(R.string.search_cipai_hint);
        search.setHintTextColor(getResources().getColor(R.color.black_hint));
        search.setTextColor(getResources().getColor(R.color.black));
        findViewById(R.id.exit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                search.setText("");
                search.requestFocus();
            }
        });
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    clear.setVisibility(View.GONE);
                } else {
                    clear.setVisibility(View.VISIBLE);
                }
                refreshData();
            }
        });
    }
}
