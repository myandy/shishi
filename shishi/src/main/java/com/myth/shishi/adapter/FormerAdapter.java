package com.myth.shishi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myth.shishi.MyApplication;
import com.myth.shishi.R;
import com.myth.shishi.entity.Former;
import com.myth.shishi.listener.MyListener;

import java.util.List;

public class FormerAdapter extends RecyclerView.Adapter<FormerAdapter.ViewHolder> {

    private List<Former> list;

    private ViewHolder holder;

    private MyListener myListener;

    private Context mContext;

    public void setList(List<Former> list) {
        this.list = list;
    }

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private MyListener myListener;

        public ViewHolder(View arg0) {
            super(arg0);
            name = (TextView) arg0.findViewById(R.id.name);
            tag = (TextView) arg0.findViewById(R.id.tag);
            arg0.setOnClickListener(this);
        }

        TextView name;

        TextView tag;

        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {
            if (myListener != null) {
                myListener.onItemClick(getPosition());
            }
        }

        public void setMyListener(MyListener myListener) {
            this.myListener = myListener;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FormerAdapter(Context context) {
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FormerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cipai, parent, false);
        // set the view's size, margins, paddings and layout parameters

        holder = new ViewHolder(convertView);
        holder.myListener = myListener;

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.name.setText(list.get(position).getName());
        MyApplication myApplication = (MyApplication) ((Activity) mContext).getApplication();
        holder.name.setTypeface(myApplication.getTypeface());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }

}
