package com.myth.shishi.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.myth.shishi.BaseActivity;
import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.db.PoetryDatabaseHelper;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.Poetry;
import com.myth.shishi.wiget.AuthorView;
import com.myth.shishi.wiget.PoetryView;

public class AuthorPageActivity extends BaseActivity
{

    private ArrayList<Poetry> list = new ArrayList<Poetry>();

    private Author author;

    private ViewPager gallery;

    private int page = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);

        if (getIntent().hasExtra("author"))
        {
            author = (Author) getIntent().getSerializableExtra("author");
        }

        list = PoetryDatabaseHelper.getAllByAuthor(author.getAuthor());
        if (getIntent().hasExtra("title"))
        {
            page = searchAuthor(getIntent().getStringExtra("title"));
        }

        initView();
    }

    private int searchAuthor(String word)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getTitle().contains(word))
            {
                return i;
            }
        }
        return -1;
    }

    private void initView()
    {

        gallery = (ViewPager) findViewById(R.id.viewpage);
        gallery.setAdapter(galleryAdapter);
        if (page != -1)
        {
            gallery.setCurrentItem(page);
        }
        else
        {
            gallery.setCurrentItem(galleryAdapter.getCount() - 1);
        }

    }

    /**
     * 滚图的adapter
     */
    private PagerAdapter galleryAdapter = new PagerAdapter()
    {
        public Object instantiateItem(android.view.ViewGroup container, final int position)
        {
            View root = getLayoutInflater().inflate(R.layout.layout_textview, null);

            LayoutParams param = new LayoutParams(-1, -1);

            if (position == getCount() - 1)
            {
                root = new AuthorView(mActivity, author);
            }

            else
            {
                root=new PoetryView(mActivity,author,list.get(position),(list.size()-position)+"/"+list.size());
            }
            container.addView(root, param);
            return root;
        };

        public void destroyItem(android.view.ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        };

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount()
        {
            if (list == null)
            {
                return 0;
            }
            return list.size() + 1;
        }
    };

}
