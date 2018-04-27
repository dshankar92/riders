package com.bikegroup.riders.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.model.DataModel;
import com.bikegroup.riders.view.utils.AndroidAppUtils;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;
    /**
     * Debuggable TAG
     */
    private String TAG = DrawerItemCustomAdapter.class.getSimpleName();

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        DataModel folder = data[position];
        AndroidAppUtils.showInfoLog(TAG, "folder.name : " + folder.name+
        "\n folder.color : "+folder.color);
        mViewHolder.rlNavigationButtonLayout.setBackgroundColor(mContext.getResources().getColor(folder.color));
        mViewHolder.imageViewIcon.
                setImageBitmap(AndroidAppUtils.decodeSampledBitmapFromResource(mContext.getResources(),folder.icon
                        ,128,128));
        mViewHolder.textViewName.setText(folder.name);
        switch (position) {

            case 0:
                mViewHolder.textViewName.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case 1:
                mViewHolder.textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 2:
                mViewHolder.textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 3:
                mViewHolder.textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 4:
                mViewHolder.textViewName.setTextColor(mContext.getResources().getColor(R.color.white));
                break;

        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageViewIcon;
        TextView textViewName;
        RelativeLayout rlNavigationButtonLayout;

        public ViewHolder(View view) {
            imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            rlNavigationButtonLayout = (RelativeLayout) view.findViewById(R.id.rlNavigationButtonLayout);
        }
    }
}

