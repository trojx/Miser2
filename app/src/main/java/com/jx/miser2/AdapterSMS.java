package com.jx.miser2;

/**
 * Created by li on 16-9-10.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdapterSMS extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    List<BeanSMS> mMsgList;



    public AdapterSMS(Context context, List<BeanSMS> smslist){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(context);
        this.mMsgList= smslist;//new ArrayList<BeanSMS>();
    }

    @Override
    public int getCount() {
        if(mMsgList==null){
            return 0;
        }else{
            return mMsgList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(mMsgList==null ){
            return null;
        }else{
            return mMsgList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_sms, null);
            viewHolder=new ViewHolder();


            viewHolder.lly=(LinearLayout)convertView.findViewById(R.id.lly);
            viewHolder.tvTime=(TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tvName=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tvMsg=(TextView)convertView.findViewById(R.id.tv_msg);
            viewHolder.ivHead=(ImageView)convertView.findViewById(R.id.iv_head);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        BeanSMS msgInfo=(BeanSMS)getItem(position);


        //viewHolder.lly.setVisibility(View.VISIBLE);
        viewHolder.tvTime.setText(msgInfo.mTime);
        viewHolder.tvName.setText(msgInfo.mName);
        viewHolder.tvMsg.setText(msgInfo.mMsg);
        //viewHolder.ivHead.setImageDrawable(mContext.getResources().getDrawable(msgInfo.mHead));
        viewHolder.ivHead.setImageResource(R.mipmap.head_4);
        //viewHolder.llyMy.setVisibility(View.GONE);

        return convertView;
    }
    class ViewHolder{
        /***
         * 显示别人的信息的控件
         */

        LinearLayout lly;
        TextView tvTime;
        TextView tvName;
        TextView tvMsg;
        ImageView ivHead;;
    }
}