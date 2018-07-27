package com.max_plus.homedooropenplate.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.max_plus.homedooropenplate.Bean.DanYuanBean;
import com.max_plus.homedooropenplate.Bean.LouYuBean;
import com.max_plus.homedooropenplate.R;

import java.util.ArrayList;
import java.util.List;


public class ChooseDanYuanAdapter extends BaseAdapter {

    private Context mContext;
    private List<DanYuanBean> mDatas = new ArrayList();

    public ChooseDanYuanAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public DanYuanBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //int index = position;
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.xiaoqu_listview, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            mViewHolder.cb_checkbox = convertView.findViewById(R.id.cb_checkbox);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
//        mViewHolder.mCbCheckbox.setTag(mDatas.get(position));
//        mViewHolder.mCbCheckbox.setOnCheckedChangeListener(this);
//        mViewHolder.mCbCheckbox.setChecked(mDatas.get(position).getCheckStatus());
//        mDatas.get(position).setCheckStatus(mDatas.get(position).getCheckStatus()?true:false);


        if (mDatas.get(position).isChecked()) {
            mViewHolder.cb_checkbox.setChecked(true);
        } else {
            mViewHolder.cb_checkbox.setChecked(false);
        }

        mViewHolder.tv_title.setText(mDatas.get(position).getDanyuan_Name());
        return convertView;
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        Person person = (Person) buttonView.getTag();
//        person.setCheckStatus(isChecked);
//    }

    static class ViewHolder {
        TextView tv_title;
        CheckBox cb_checkbox;
    }
}