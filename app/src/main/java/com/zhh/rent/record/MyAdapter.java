package com.zhh.rent.record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhh.rent.R;

import java.util.List;

/**
 * Created by hx on 2017/11/7.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ItemBean> mList;

    private OnItemClickListener mlistener;

    private OnItemLongClickListener mLongListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View myView;
        ImageView imageView;
        TextView title;
        TextView content;
        RelativeLayout rLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            rLayout = itemView.findViewById(R.id.rl_recycler_item);
        }
    }

    public MyAdapter(List<ItemBean> list) {
        this.mList = list;
    }

    public MyAdapter(List<ItemBean> list, OnItemClickListener listener) {
        this.mList = list;
        //从外部传进来一个OnItemClickListener子类的变量
        this.mlistener=listener;
    }

    public MyAdapter(List<ItemBean> list, OnItemClickListener listener,OnItemLongClickListener longClickListener) {
        this.mList = list;
        //从外部传进来一个OnItemClickListener子类的变量
        this.mlistener=listener;
        this.mLongListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //将数据绑定到控件上
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //绑定内容
        ItemBean bean = mList.get(position);
        holder.imageView.setBackgroundResource(bean.itemImage);
        holder.title.setText(bean.itemTitle);
        holder.content.setText(bean.itemContent);
        //设置监听
        holder.rLayout.setOnClickListener(new ClickListener(position));  //只监听布局
        holder.rLayout.setOnLongClickListener(new LongClickListener(position));
//        holder.imageView.setOnClickListener(new ClickListener(position));
//        holder.title.setOnClickListener(new ClickListener(position));
//        holder.content.setOnClickListener(new ClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<ItemBean> addMessageList) {
        //增加数据
        int position = mList.size();
        mList.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<ItemBean> newList) {
        //刷新数据
        mList.removeAll(mList);
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    //定义一个接口
    public interface OnItemClickListener{
        //接口默认都是抽象的方法，且类型都是public
        void OnClick(int position, View v);
    }

    private class ClickListener implements View.OnClickListener {

        private int mPosition;

        public ClickListener(Integer position){
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mlistener.OnClick(mPosition,v);
        }
    }

    //定义一个接口
    public interface OnItemLongClickListener{
        //接口默认都是抽象的方法，且类型都是public
        boolean onLongClick(int position, View v);
    }

    public class LongClickListener implements View.OnLongClickListener{

        private int mPosition;

        public LongClickListener(Integer position){
            this.mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            mLongListener.onLongClick(mPosition,v);
            return false;
        }
    }
}
