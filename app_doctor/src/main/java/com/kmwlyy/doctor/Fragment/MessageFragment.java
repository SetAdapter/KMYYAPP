package com.kmwlyy.doctor.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.OrderListActivity;
import com.kmwlyy.doctor.R;
import com.kmwlyy.login.EventMessageClear;
import com.kmwlyy.login.MessageApi;
import com.kmwlyy.login.MessageEvent;
import com.kmwlyy.login.MsgDetailActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.InsideListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;


public class MessageFragment extends Fragment{
	public static final String TAG = "MessageFragment";
	private Context mContext;
	public List<MessageApi.Message> mListData;
	public ListAdapter mListAdapter;
	private ViewGroup mContent;

	private InsideListView mMsgListView;
	private Boolean isUpdate = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		EventBus.getDefault().register(this);
		mContext = getActivity();
		mContent = (ViewGroup) view.findViewById(R.id.content);
		mListData = new ArrayList<>();
		mListAdapter = new ListAdapter(mContext,mListData);

		mMsgListView = (InsideListView)view.findViewById(R.id.lv_message);
		mMsgListView.setAdapter(mListAdapter);
		mMsgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goToDetail(position);
			}
		});

		getListData();
		return view;
	}

	private void getListData() {
		MessageApi.getMessageList event = new MessageApi.getMessageList("0", CommonUtils.getCurrentDate("yyyy-MM-dd"),CommonUtils.getFutureDate(CommonUtils.getCurrentDate("yyyy-MM-dd"),"yyyy-MM-dd",1), new HttpListener<List<MessageApi.Message>>() {
			@Override
			public void onError(int code, String msg) {
				mListAdapter.clearData();
				EmptyViewUtils.removeAllView(mContent);
				EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_network, "请求错误", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
//						EmptyViewUtils.removeAllView(mContent);
//						getListData();
					}
				});
			}

			@Override
			public void onSuccess(List<MessageApi.Message> messages) {
				if(messages == null || messages.size() == 0){
					mListAdapter.clearData();
					EmptyViewUtils.removeAllView(mContent);
					EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_message, "暂无数据", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
//							EmptyViewUtils.removeAllView(mContent);
//							getListData();
						}
					});
				}else {
					EmptyViewUtils.removeAllView(mContent);
					mListAdapter.addData(messages);
				}
			}
		});
		new HttpClient(mContext, event).start();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void clearUnreadCount(EventMessageClear arg) {
		for (MessageApi.Message message : mListData){
			message.setIsRead(true);
			mListAdapter.notifyDataSetChanged();
		}
	}

	static class ViewHolder {
		@ViewInject(R.id.iv_status)
		ImageView mStatusImage;
		@ViewInject(R.id.tv_title)
		TextView mTitleText;
		@ViewInject(R.id.tv_time)
		TextView mTimeText;
		@ViewInject(R.id.tv_content)
		TextView mContentText;
		@ViewInject(R.id.iv_image)
		ImageView iv_image;

		ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public class ListAdapter extends CommonAdapter<MessageApi.Message> {
		private Context context;
		private List<MessageApi.Message> mListDate;
		private Map<Integer , Integer> Imgs = new HashMap<Integer , Integer>(){{
			put(0, R.drawable.icon_msg_sys);
			put(1, R.drawable.icon_msg_sys);
			put(4, R.drawable.icon_msg_sys);
			put(2, R.drawable.icon_msg_order);
			put(3, R.drawable.icon_msg_bus);
			put(5, R.drawable.icon_msg_msg);
			put(6, R.drawable.icon_msg_diag);
		}};

		public ListAdapter(Context mContext,List<MessageApi.Message> list) {
			super(mContext, R.layout.item_msglist,list );
			context = mContext;
			mListDate = list;
		}

		public void addData(List<MessageApi.Message> list){
			mListDate.clear();
			mListDate.addAll(list);
			notifyDataSetChanged();
		}

		public void clearData(){
			mListDate.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mListDate == null ? 0 : mListDate.size();
		}

		@Override
		public MessageApi.Message getItem(int position) {
			return mListDate.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(context, com.kmwlyy.login.R.layout.item_msglist, null);
				viewHolder = new ViewHolder(convertView);
				ViewUtils.inject(viewHolder, convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MessageApi.Message item = mListDate.get(position);
			viewHolder.mTitleText.setText(item.getTitle());
			viewHolder.mTimeText.setText(item.getNoticeDate().substring(0,16).replace("T"," "));
			viewHolder.mContentText.setSingleLine(true);
			viewHolder.mContentText.setEllipsize(TextUtils.TruncateAt.END);
			viewHolder.mContentText.setText(item.getContent());
			viewHolder.mStatusImage.setVisibility(item.isIsRead() ? View.INVISIBLE : View.VISIBLE);
			if(Imgs.containsKey(item.getNoticeFirstType())){
				viewHolder.iv_image.setImageResource(Imgs.get(item.getNoticeFirstType()));
			}
			return convertView;
		}

		@Override
		public void convert(com.winson.ui.widget.ViewHolder viewHolder, MessageApi.Message obj, int position) {

		}
	}

	//点击跳转
	public void goToDetail(final int position){
		final MessageApi.Message item = mListData.get(position);
		if (!item.isIsRead()) {
			MessageApi.setMsgToReaded event = new MessageApi.setMsgToReaded(item.getMessageID(), new HttpListener() {
				@Override
				public void onError(int code, String msg) {
					ToastUtils.showShort(mContext, msg);
				}

				@Override
				public void onSuccess(Object o) {
					mListData.get(position).setIsRead(true);
					mListAdapter.notifyDataSetChanged();
				}
			});
			new HttpClient(mContext, event).start();
		}

		switch (item.getNoticeFirstType()){
			case 0:
			case 1:
			case 4:
			case 5:
			case 6:
				Intent intent = new Intent(mContext,MsgDetailActivity.class);
				intent.putExtra("item",item);
				startActivity(intent);
				break;
			case 2:
				Intent intent1 = new Intent(mContext, OrderListActivity.class);
				intent1.putExtra("item",item);
				startActivity(intent1);
				break;
			case 3:
				EventBus.getDefault().post(new MessageEvent(MessageEvent.TYPE_DOCTOR_CONSULT));
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isUpdate = false;
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 接收到推送，刷新界面
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void updateDoctorInfo(MessageApi.Message bean){
		LogUtils.i(TAG,"接收到推送，刷新界面");
		getListData();
	}
}
