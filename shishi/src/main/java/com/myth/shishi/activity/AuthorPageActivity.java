package com.myth.shishi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.R;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.db.WritingDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.entity.Writing;
import com.myth.shishi.wiget.AuthorView;
import com.myth.shishi.wiget.PoetryView;
import com.myth.shishi.wiget.ScanView;

import java.util.ArrayList;

public class AuthorPageActivity extends BaseActivity {

    private ArrayList<Poetry> list = new ArrayList<Poetry>();

    private ArrayList<Writing> writings = new ArrayList<Writing>();

    private Author author;

    private ScanView gallery;

    private int page = -1;

    boolean isSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);

        refresh();

    }

    private void refresh() {
        if (getIntent().hasExtra("author")) {
            author = (Author) getIntent().getSerializableExtra("author");
            list = PoetryDatabaseHelper.getAllByAuthor(author.getAuthor());
        } else {
            isSelf = true;
            writings = WritingDatabaseHelper.getAllWriting();
        }

        if (getIntent().hasExtra("id")) {
            int id = getIntent().getIntExtra("id", 0);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == id) {
                    page = i;
                }
            }
        }

        initView();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refresh();

    }

    private int searchAuthor(String word) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().contains(word)) {
                return i;
            }
        }
        return -1;
    }

    private void initView() {

        gallery = (ScanView) findViewById(R.id.scanview);

        if (!isSelf) {
            if (page != -1) {
                gallery.setAdapter(galleryAdapter, page + 2);
            } else {
                gallery.setAdapter(galleryAdapter, 1);
            }
        } else {
            if (page != -1) {
                gallery.setAdapter(galleryAdapter, page + 1);
            } else {
                gallery.setAdapter(galleryAdapter, 1);
            }
        }

    }

    /**
     * 滚图的adapter
     */
    private PagerAdapter galleryAdapter = new PagerAdapter() {
        public Object instantiateItem(android.view.ViewGroup container,
                                      int position) {
            View root = getLayoutInflater().inflate(R.layout.layout_textview,
                    null);

            LayoutParams param = new LayoutParams(-1, -1);

            if (!isSelf) {
                if (position <= 0 || position > list.size() + 1) {
                    return new View(mActivity);
                } else if (position == 1) {
                    root = new AuthorView(mActivity, author);
                } else {
                    root = new PoetryView(mActivity, author,
                            list.get(position - 2), position - 1 + "/"
                            + list.size());
                }
            } else {

                if (position <= 0 || position > writings.size()) {
                    return new View(mActivity);
                } else {
                    root = new PoetryView(mActivity,
                            writings.get(position - 1), position + "/"
                            + writings.size());
                }
            }
            // container.addView(root, param);
            return root;
        }

        public void destroyItem(android.view.ViewGroup container, int position,
                                Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {

            if (isSelf) {
                if (writings == null) {
                    return 0;
                } else {
                    return writings.size();
                }
            } else {
                if (list == null) {
                    return 0;
                } else {
                    return list.size() + 1;
                }
            }
        }
    };

}
