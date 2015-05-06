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

public class AuthorPageActivity extends BaseActivity
{

    private ArrayList<Poetry> list = new ArrayList<Poetry>();

    private Author author;

    private ViewPager gallery;

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
        initView();
    }

    private void initView()
    {

        gallery = (ViewPager) findViewById(R.id.viewpage);
        gallery.setAdapter(galleryAdapter);
        gallery.setCurrentItem(galleryAdapter.getCount() - 1);

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
                TextView textView = (TextView) root.findViewById(R.id.textview);
                textView.setTypeface(MyApplication.typeface);
                String text = list.get(position).getPoetry();
                if (!TextUtils.isEmpty(list.get(position).getAuthor()))
                {
                    text = list.get(position).getAuthor() + "\n" + text;
                }
                textView.setText(text);

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
