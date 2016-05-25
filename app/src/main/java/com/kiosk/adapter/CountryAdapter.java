package com.kiosk.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiosk.R;
import com.kiosk.staticfunction.Font;

import java.util.ArrayList;
import java.util.List;

import greendao.Category;
import greendao.Country;

/**
 * Created by USER on 12/18/2015.
 */
public class CountryAdapter extends BaseAdapter {

    public interface OnClickOnItemCallback {
        void onCLickItem(String name, String code);
    }

    private OnClickOnItemCallback onClickOnItemCallback;

    private List<Country> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typeface;

    public CountryAdapter(Activity activity, List<Country> listData, OnClickOnItemCallback onClickOnItemCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.onClickOnItemCallback = onClickOnItemCallback;

        typeface = Font.Regular(this.activity);
    }

    public void setListData(List<Country> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_spinner, null);
            holder = new ViewHolder();

            holder.txtSpinner = (TextView) convertView.findViewById(R.id.txtSpinner);

            holder.txtSpinner.setTypeface(typeface);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSpinner.setText(listData.get(position).getName());
        holder.txtSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOnItemCallback.onCLickItem(listData.get(position).getName(), listData.get(position).getCode());
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView txtSpinner;
    }

}
