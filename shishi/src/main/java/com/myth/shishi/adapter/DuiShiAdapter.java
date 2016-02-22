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
import com.myth.shishi.listener.MyListener;

import java.util.List;

public class DuiShiAdapter extends RecyclerView.Adapter<DuiShiAdapter.ViewHolder> {

    private List<String> list;

    private ViewHolder holder;

    private MyListener myListener;

    private Context mContext;

    public void setList(List<String> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private MyListener myListener;

        public ViewHolder(View arg0) {
            super(arg0);
            name = (TextView) arg0.findViewById(R.id.name);
            arg0.setOnClickListener(this);
        }

        TextView name;

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


    public DuiShiAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DuiShiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_duishi, parent, false);

        holder = new ViewHolder(convertView);
        holder.myListener = myListener;
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(list.get(position));
        MyApplication myApplication = (MyApplication) ((Activity) mContext).getApplication();
        holder.name.setTypeface(myApplication.getTypeface());

    }

    public List<String> getDatas() {
        return list;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }

}
