package com.ben.data;

import java.util.ArrayList;
import java.util.HashMap;

import com.ben.timemusic.R;
import com.ben.timemusic.R.drawable;
import com.ben.timemusic.R.id;
import com.ben.timemusic.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> currList = null;
	private Context context = null;
	private LayoutInflater mInflater = null;

	public ItemListAdapter(ArrayList<HashMap<String, String>> currList,
			Context context) {
		super();
		this.currList = currList;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return this.currList.size();
	}

	public Object getItem(int arg0) {
		return this.currList.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	class ViewHolder {
		TextView mText;
		ImageView mImage;
		ImageView mImageOperate;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.listitem, null);
			holder.mImage = (ImageView) convertView.findViewById(R.id.itemicon);
			holder.mText = (TextView) convertView.findViewById(R.id.itemname);
			holder.mImageOperate = (ImageView) convertView
					.findViewById(R.id.itemoperate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mText.setText(this.currList.get(position).get("name"));
		if (this.currList.get(position).get("icon").equals("0")) {
			holder.mImage.setBackgroundResource(R.drawable.folder);
		} else {
			holder.mImage.setBackgroundResource(R.drawable.music);
		}
		String operate = this.currList.get(position).get("operate");
		if (operate != null) {
			if (operate.equals("add")) {
				holder.mImageOperate.setBackgroundResource(R.drawable.add);
			} else if (operate.equals("del")) {
				holder.mImageOperate.setBackgroundResource(R.drawable.del);
			} else if(operate.equals("enter")){
				holder.mImageOperate.setBackgroundResource(R.drawable.enter);
			}else if (operate.equals("play")) {
				holder.mImageOperate.setBackgroundResource(R.drawable.play);
			} else if(operate.equals("pause")){
				holder.mImageOperate.setBackgroundResource(R.drawable.pause);
			}
		}else{
			holder.mImageOperate.setBackgroundResource(0);
		}
		return convertView;
	}

}
