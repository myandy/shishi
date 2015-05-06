package com.myth.shishi.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.activity.AuthorPageActivity;
import com.myth.shishi.adapter.AuthorListAdapter.ViewHolder.ViewHolderItem;
import com.myth.shishi.entity.Author;
import com.myth.shishi.entity.ColorEntity;
import com.myth.shishi.util.DisplayUtil;
import com.myth.shishi.wiget.CircleStringView;
import com.myth.shishi.wiget.StoneView;
import com.myth.shishi.wiget.VerticalTextView;

public class AuthorListAdapter extends RecyclerView.Adapter<AuthorListAdapter.ViewHolder>
{
    private Context mContext;

    private List<Author> list;

    public void setList(List<Author> list)
    {
        this.list = list;
    }

    public AuthorListAdapter(Context context)
    {
        mContext = context;
    }

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ViewHolderItem holder1;

        ViewHolderItem holder2;

        public class ViewHolderItem
        {
            Author author;

            View item;

            RelativeLayout head;

            ViewGroup middle;

            TextView num;

            TextView name;

            VerticalTextView enname;

            CircleStringView stoneView;
        }

        public ViewHolder(View convertView, ViewGroup parent)
        {
            super(convertView);
            View view = convertView.findViewById(R.id.content);
            view.setLayoutParams(new LinearLayout.LayoutParams(-2, parent.getMeasuredHeight()));
            holder1 = new ViewHolderItem();
            holder2 = new ViewHolderItem();
            holder1.item = convertView.findViewById(R.id.item1);
            holder1.head = (RelativeLayout) convertView.findViewById(R.id.head1);
            holder1.middle = (ViewGroup) convertView.findViewById(R.id.middle1);
            holder1.num = (TextView) convertView.findViewById(R.id.num_1);
            holder1.name = (TextView) convertView.findViewById(R.id.name1);
            holder1.enname = (VerticalTextView) convertView.findViewById(R.id.enname1);
            holder1.stoneView = new CircleStringView(parent.getContext());
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(
                    DisplayUtil.dip2px(parent.getContext(), 40), DisplayUtil.dip2px(parent.getContext(), 40));
            holder1.middle.addView(holder1.stoneView, layoutParams);

            holder2.item = convertView.findViewById(R.id.item2);
            holder2.head = (RelativeLayout) convertView.findViewById(R.id.head2);
            holder2.middle = (ViewGroup) convertView.findViewById(R.id.middle2);
            holder2.num = (TextView) convertView.findViewById(R.id.num_2);
            holder2.name = (TextView) convertView.findViewById(R.id.name2);
            holder2.enname = (VerticalTextView) convertView.findViewById(R.id.enname2);
            holder2.stoneView = new CircleStringView(parent.getContext());
            holder2.middle.addView(holder2.stoneView, layoutParams);

            holder1.name.setTypeface(MyApplication.typeface);
            holder2.name.setTypeface(MyApplication.typeface);
            holder1.enname.setTypeface(MyApplication.typeface);
            holder2.enname.setTypeface(MyApplication.typeface);
            holder1.num.setTypeface(MyApplication.typeface);
            holder2.num.setTypeface(MyApplication.typeface);
        }

        TextView name;

        TextView tag;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AuthorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cipai_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder holder = new ViewHolder(convertView, parent);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        initHolderView(holder.holder1, 2 * position);
        initHolderView(holder.holder2, 2 * position + 1);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return list == null ? 0 : list.size() / 2;
    }

    private void initHolderView(final ViewHolderItem holder, int pos)
    {
        holder.author = list.get(pos);
        if (holder.author != null)
        {
            holder.item.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(mContext, AuthorPageActivity.class);
                    intent.putExtra("author", holder.author);
                    mContext.startActivity(intent);
                }
            });

            ColorEntity colorEntity = MyApplication.getColorByPos(pos / 2);
            int color = 0xffffff;
            if (colorEntity != null)
            {
                color = Color.rgb(colorEntity.getRed(), colorEntity.getGreen(), colorEntity.getBlue());
            }
            holder.author.setColor(color);
            holder.head.setBackgroundColor(color);
            holder.num.setTextColor(color);
            holder.name.setTextColor(color);
            holder.enname.setTextColor(color);

            holder.stoneView.setType(holder.author.getDynasty(), color);
            String count = holder.author.getP_num() + "";
            if (holder.author.getP_num() < 100)
            {
                count = "0" + holder.author.getP_num();
            }
            holder.num.setText(count);
            holder.name.setText(holder.author.getAuthor() + "");
            holder.enname.setText(holder.author.getEn_name());
        }
    }

}
