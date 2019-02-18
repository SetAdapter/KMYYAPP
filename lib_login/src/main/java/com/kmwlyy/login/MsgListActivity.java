package com.kmwlyy.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class MsgListActivity extends BaseActivity implements PageListView.OnPageLoadListener{

    public static final String TAG = MsgListActivity.class.getSimpleName();
    /*********************医生*************************
     * 1	通知/公告
     * 2	新图文咨询订单（所有类型的图文咨询）
     * 3	新音视频问诊订单（所有类型的音视频问诊）
     * 4	新家庭医生订单
     * 5	新远程会诊订单
     * 6	音视频问诊患者进入诊室
     * 7	有新药店处方
     * 8	有会诊正在进行
     * *******************患者*************************
     * 9	通知/公告
     * 10	图文咨询医生回复（所有类型的图文咨询）
     * 11	你的看诊处方已生成
     * 12	音视频看诊医生呼叫
     */

    private Context mContext = MsgListActivity.this;
    private List<MessageApi.Message> mListData;
    public ListAdapter mListAdapter;

    private PageListView mMsgListView;
    private PageListViewHelper<MessageApi.Message> mPageListViewHelper;

    @Override
    public void onRefreshData() {getListData(true);}

    @Override
    public void onLoadPageData() {getListData(false);}

    public abstract void goToDetail(MessageApi.Message item);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msglist;
    }

    @Override
    protected void afterBindView() {
        setTitle(getString(R.string.my_message));
        mRightText.setText(R.string.mark_all_read);
        mRightText.setVisibility(View.VISIBLE);
        mListData = new ArrayList<>();
        mListAdapter = new ListAdapter(mContext,mListData);

        mMsgListView = (PageListView)findViewById(R.id.lv_message);
        mPageListViewHelper = new PageListViewHelper<>(mMsgListView, mListAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);

        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MessageApi.Message item = mListData.get(position);
                if (!item.isIsRead()) {
                    MessageApi.setMsgToReaded event = new MessageApi.setMsgToReaded(item.getMessageID(), new HttpListener() {
                        @Override
                        public void onError(int code, String msg) {
                            ToastUtils.showShort(mContext, msg);
                        }

                        @Override
                        public void onSuccess(Object o) {
                            item.setIsRead(true);
                            mListAdapter.notifyDataSetChanged();
                            EventBus.getDefault().post(new MessageApi.Message());
                        }
                    });
                    new HttpClient(mContext, event).start();
                }
                goToDetail(item);
            }
        });
        getListData(true);
    }

    private void getListData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        MessageApi.getMessageList event = new MessageApi.getMessageList(refresh ? "1" : mPageListViewHelper.getPageIndex()+"", "20", new HttpListener<List<MessageApi.Message>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
                mPageListViewHelper.setRefreshing(false);
                Log.i(TAG, "onError: "+code+msg);
            }

            @Override
            public void onSuccess(List<MessageApi.Message> messages) {
                if (refresh) {
                    if (messages == null || messages.size() == 0) {//没有数据
                        ToastUtils.showShort(mContext, getString(R.string.no_message));
                    } else {
//                        mListData = messages;
                        mPageListViewHelper.refreshData(messages);
                    }
                } else {
                    if (messages == null || messages.size() == 0) {//没有更多数据
                        ToastUtils.showShort(mContext, getString(R.string.no_more_message));
                    } else {
//                        mListData.addAll(messages);
                        mPageListViewHelper.addPageData(messages);
                    }
                }
                mPageListViewHelper.setRefreshing(false);
                mPageListViewHelper.notifyDataSetChanged();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mListAdapter.notifyDataSetChanged();
//                        mListView.onHeaderRefreshComplete();
//                        mListView.onFooterRefreshComplete();
//                    }
//                });
            }
        });
        new HttpClient(mContext, event).start();
    }


    @Override
    public void onRightClick(View view) {
        AlterDialogView.Builder builder = new AlterDialogView.Builder(this);
        builder.setTitle(R.string.string_dialog_title);
        builder.setMessage(R.string.ask_mark_all_read);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.error_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        markAllMsgRead();
                        arg0.dismiss();
                    }
                });
        builder.create().show();
    }

    /*设置所有消息已读*/
    private void markAllMsgRead(){

        if (mListData.size() <= 0 ){
            return;
        }

        for (MessageApi.Message message : mListData){
            message.setIsRead(true);
            mListAdapter.notifyDataSetChanged();
        }

        EventBus.getDefault().post(new EventMessageClear());

        MessageApi.setAllMsgReaded event = new MessageApi.setAllMsgReaded(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
            }

            @Override
            public void onSuccess(String messages) {
            }
        });
        new HttpClient(mContext, event).start();
    }

    static class ViewHolder {
        @BindView(R2.id.iv_status)
        ImageView mStatusImage;
        @BindView(R2.id.tv_title)
        TextView mTitleText;
        @BindView(R2.id.tv_time)
        TextView mTimeText;
        @BindView(R2.id.tv_content)
        TextView mContentText;
        @BindView(R2.id.iv_image)
        ImageView iv_image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class ListAdapter extends CommonAdapter<MessageApi.Message> {
        private Context context;
        private List<MessageApi.Message> mListDate;
        //0，1,4系统公告，2订单消息，3业务消息，5服务消息，6远程会诊平台消息
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

        @Override
        public int getCount() {
            return mListData == null ? 0 : mListData.size();
        }

        @Override
        public MessageApi.Message getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_msglist, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MessageApi.Message item = mListData.get(position);
            viewHolder.mTitleText.setText(item.getTitle());
            viewHolder.mTimeText.setText(item.getNoticeDate().substring(0,16).replace("T"," "));
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
    };
}
