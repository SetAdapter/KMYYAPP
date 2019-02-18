package com.kmwlyy.doctor.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.PhotoViewActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.httpEvent.Http_acceptQuestion_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getMyConsult_Event;
import com.kmwlyy.imchat.TimApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.LineCheckTextView;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

public class QuestionListFragment extends Fragment implements OnClickListener, PageListView.OnPageLoadListener {


    private static final java.lang.String TAG = QuestionListFragment.class.getSimpleName();

    private PageListView mQuestionList;
    private PageListViewHelper<ConsultBean> mQuestionListHelper;
    private QuestionListAdapter mQuestionListAdapter;

    static class RefreshQuestionEvent {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshQuestionList(RefreshQuestionEvent event) {
        getQuestionList(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_query, null);

        mQuestionListAdapter = new QuestionListAdapter(getActivity(), null);
        mQuestionList = (PageListView) view.findViewById(R.id.question_list);
        mQuestionListHelper = new PageListViewHelper<>(mQuestionList, mQuestionListAdapter);
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mQuestionListHelper.getListView().setDividerHeight(0);
        mQuestionListHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mQuestionListHelper.getListView().setClipToPadding(false);
        mQuestionListHelper.setOnPageLoadListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getQuestionList(true);
    }

    private void getQuestionList(final boolean refresh) {

        mQuestionListHelper.setRefreshing(refresh);
//        Log.d(TAG, "getQuestionList refresh " + refresh);
        Http_getMyConsult_Event getMyConsult_event = new Http_getMyConsult_Event(1,
                refresh ? "1" : mQuestionListHelper.getPageIndex() + ""
                , null, new HttpListener<List<ConsultBean>>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, DebugUtils.errorFormat("getQuestionList", code, msg));
                mQuestionListHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<ConsultBean> list) {
                LogUtils.d(TAG, DebugUtils.successFormat("getQuestionList", DebugUtils.toJson(list)));
                mQuestionListHelper.setRefreshing(false);

                if (refresh) {
                    mQuestionListHelper.refreshData(list);
                } else {
                    mQuestionListHelper.addPageData(list);
                }

            }
        });

        HttpClient client = NetService.createClient(getActivity(), getMyConsult_event);
        client.start();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefreshData() {
        getQuestionList(true);
    }

    @Override
    public void onLoadPageData() {
        getQuestionList(false);
    }

    static class QuestionListAdapter extends CommonAdapter<ConsultBean> {

        private final String MALE;
        private final String FEMALE;

        public QuestionListAdapter(Context context, List<ConsultBean> datas) {
            super(context, R.layout.question_list_item, datas);

            MALE = context.getResources().getString(R.string.male);
            FEMALE = context.getResources().getString(R.string.female);

        }

        @Override
        public void convert(ViewHolder viewHolder, final ConsultBean data, int position) {
            View paddingView = viewHolder.findViewById(R.id.padding);
            if (position == 0) {
                paddingView.setVisibility(View.GONE);
            } else {
                paddingView.setVisibility(View.VISIBLE);
            }

            ((TextView) viewHolder.findViewById(R.id.user_name)).setText(data.UserMember.MemberName);
            final LineCheckTextView describtion = (LineCheckTextView) viewHolder.findViewById(R.id.describtion);
            final TextView display = (TextView) viewHolder.findViewById(R.id.display);
            describtion.setText(data.ConsultContent);
            describtion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    describtion.getViewTreeObserver().removeOnPreDrawListener(this);

                    if (describtion.checkIsOver()) {
                        if (describtion.isShowAll()) {
                            display.setText(R.string.pack_up);
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.coll);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            display.setCompoundDrawables(null, null, drawable, null);
                        } else {
                            display.setText(R.string.display);
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.display);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            display.setCompoundDrawables(null, null, drawable, null);
                        }
                        display.setVisibility(View.VISIBLE);
                    } else {
                        display.setVisibility(View.GONE);
                    }

                    return false;
                }
            });

            display.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (describtion.isShowAll()) {
                        display.setText(R.string.display);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.display);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.hiddenPart();
                    } else {
                        display.setText(R.string.pack_up);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.coll);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                        describtion.showAll();
                    }
                }
            });

            TextView age = (TextView) viewHolder.findViewById(R.id.age);
            if (data.UserMember.Age == null || "0".equals(data.UserMember.Age)) {
                age.setVisibility(View.INVISIBLE);
            } else {
                age.setVisibility(View.VISIBLE);
            }
            ((TextView) viewHolder.findViewById(R.id.age)).setText(
                    String.format(context.getResources().getString(R.string.age_format), data.UserMember.Age));
            ((TextView) viewHolder.findViewById(R.id.sex)).setText(
                    "0".equals(data.UserMember.Gender) ? MALE : FEMALE);

            View imageGroup = viewHolder.findViewById(R.id.image_group);
            ImageView imageOne = (ImageView) viewHolder.findViewById(R.id.image_one);
            ImageView imageTwo = (ImageView) viewHolder.findViewById(R.id.image_two);
            ImageView imageThree = (ImageView) viewHolder.findViewById(R.id.image_three);

            if (data.UserFiles == null || data.UserFiles.isEmpty()) {
                imageGroup.setVisibility(View.GONE);
            } else {
                imageGroup.setVisibility(View.VISIBLE);
                int length = data.UserFiles.size();
                if (length > 2) {
                    ConsultBean.UserFile oneFile = data.UserFiles.get(0);
                    ConsultBean.UserFile twoFile = data.UserFiles.get(1);
                    ConsultBean.UserFile threeFile = data.UserFiles.get(2);

                    String oneUrl = HttpClient.IMAGE_URL + File.separator + oneFile.FileUrl;
                    String twoUrl = HttpClient.IMAGE_URL + File.separator + twoFile.FileUrl;
                    String threeUrl = HttpClient.IMAGE_URL + File.separator + threeFile.FileUrl;

                    final String[] photos = new String[]{
                            oneUrl,
                            twoUrl,
                            threeUrl
                    };

                    ImageLoader.getInstance().displayImage(oneUrl,
                            imageOne, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    ImageLoader.getInstance().displayImage(twoUrl,
                            imageTwo, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    ImageLoader.getInstance().displayImage(threeUrl,
                            imageThree, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    imageOne.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 0);
                        }
                    });
                    imageTwo.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 1);
                        }
                    });
                    imageThree.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 2);
                        }
                    });

                } else if (length > 1) {

                    ConsultBean.UserFile oneFile = data.UserFiles.get(0);
                    ConsultBean.UserFile twoFile = data.UserFiles.get(1);

                    String oneUrl = HttpClient.IMAGE_URL + File.separator + oneFile.FileUrl;
                    String twoUrl = HttpClient.IMAGE_URL + File.separator + twoFile.FileUrl;

                    final String[] photos = new String[]{
                            oneUrl,
                            twoUrl
                    };

                    ImageLoader.getInstance().displayImage(oneUrl,
                            imageOne, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    ImageLoader.getInstance().displayImage(twoUrl,
                            imageTwo, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    imageThree.setImageResource(R.drawable.transparent);

                    imageOne.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 0);
                        }
                    });
                    imageTwo.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 1);
                        }
                    });
                    imageThree.setOnClickListener(null);

                } else {

                    ConsultBean.UserFile oneFile = data.UserFiles.get(0);

                    String oneUrl = HttpClient.IMAGE_URL + File.separator + oneFile.FileUrl;

                    final String[] photos = new String[]{
                            oneUrl
                    };

                    ImageLoader.getInstance().displayImage(oneUrl,
                            imageOne, ImageUtils.getRectDisplayOptions(R.drawable.default_res));

                    imageTwo.setImageResource(R.drawable.transparent);
                    imageThree.setImageResource(R.drawable.transparent);

                    imageOne.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoViewActivity.startPhotoViewActivity(context, photos, 0);
                        }
                    });
                    imageTwo.setOnClickListener(null);
                    imageThree.setOnClickListener(null);

                }
            }


            TextView time = (TextView) viewHolder.findViewById(R.id.time);

            if (data.ConsultTime != null) {
                String dateStr = data.ConsultTime.replace("T", " ");
                time.setText(MyUtils.convertTime(context, dateStr));
            } else {
                time.setText("");
            }

            viewHolder.findViewById(R.id.receive_action).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlterDialogView.Builder builder = new AlterDialogView.Builder(context);
                    builder.setTitle("");
                    builder.setMessage(R.string.receive_question_notify);
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.receive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            final ProgressDialog ld = new ProgressDialog(context);
                            ld.setMessage(context.getResources().getString(R.string.on_accept_question));
                            ld.setCancelable(false);
                            ld.show();

                            Http_acceptQuestion_Event acceptQuestionEvent = new Http_acceptQuestion_Event(
                                    data.UserConsultID, new HttpListener() {
                                @Override
                                public void onError(int code, String msg) {
                                    LogUtils.d(TAG, DebugUtils.errorFormat("acceptQuestionEvent", code, msg));

                                    ld.dismiss();
                                    ToastUtils.show(context, R.string.accept_question_failed, Toast.LENGTH_SHORT);

                                }

                                @Override
                                public void onSuccess(Object o) {
                                    LogUtils.d(TAG, DebugUtils.successFormat("acceptQuestionEvent", DebugUtils.toJson(o)));
                                    EventBus.getDefault().post(new RefreshQuestionEvent());
                                    EventBus.getDefault().post(new QueryChatFragment.RefreshOrderChatData());
                                    ld.dismiss();

                                    TimApplication.enterTimchat((FragmentActivity) context,
                                            o != null ? o.toString() : "0",
                                            data.UserMember.MemberName, true);


                                }
                            });
                            HttpClient acceptQuestionClient = NetService.createClient(context, acceptQuestionEvent);
                            acceptQuestionClient.start();

                        }
                    });
                    builder.create().show();
                }
            });

        }
    }

}
