package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.BaseActivity;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.UnSignRecipeAdapter;
import com.kmwlyy.doctor.model.SignRecipeBean;
import com.kmwlyy.doctor.model.httpEvent.Http_cancelRecipe_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getSignList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.AlterDialogView;

import com.winson.ui.widget.DrawableCenterRadioButton;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.org.bjca.sdk.core.entity.UserBean;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.ConstantParams;

import static cn.org.bjca.sdk.core.values.ConstantParams.BATCH_COUNT_ERROR;
import static cn.org.bjca.sdk.core.values.ConstantParams.PERMISSION_REFUSE;
import static cn.org.bjca.sdk.core.values.ConstantParams.SIGN_NOT_CERT;
import static cn.org.bjca.sdk.core.values.ConstantParams.SIGN_NOT_STAMP;
import static cn.org.bjca.sdk.core.values.ConstantParams.SIGN_NULL_PARAMS;
import static cn.org.bjca.sdk.core.values.ConstantParams.SIGN_STATE_CHECK_OK;

public class ElectRecipeFragment extends SearchFragment implements PageListView.OnPageLoadListener, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "ElectRecipeFragment";
    private PageListViewHelper<SignRecipeBean> mPageListViewHelper;
    private Context mContext;
    private UnSignRecipeAdapter adapter;
    private ArrayList<SignRecipeBean> list;
    private Boolean isShow = false;
    private Boolean isSignList = false; //false 显示待签列表   true 显示已签列表
    public static final int TYPE_CLOSE = 0;//取消操作
    public static final int TYPE_CANCEL = 1;//撤回操作
    public static final int TYPE_SIGN = 2;//签名操作
    public static final int TYPE_REFRESH = 5;//刷新页面
    @ViewInject(R.id.rg_sign_tab)
    RadioGroup rg_sign_tab;
    @ViewInject(R.id.rb_sign_list)
    DrawableCenterRadioButton rb_sign_list;
    @ViewInject(R.id.lv_recipe)
    PageListView mRecipeListView;
    @ViewInject(R.id.ll_content)
    ViewGroup mContent;

    @Override
    public void onRefreshData() {
        if (mUseSearchMode) {
            searchData(true, mSearchKey);
        } else {
            loadData(true);
        }
    }

    @Override
    public void onLoadPageData() {
        if (mUseSearchMode) {
            searchData(true, mSearchKey);
        } else {
            loadData(false);
        }
    }

    @Override
    public void updateSearchKey(String searchKey) {
        super.updateSearchKey(searchKey);
        onRefreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elect_recipe, null);
        ViewUtils.inject(this, view); //注入view和事件
        EventBus.getDefault().register(this);
        mContext = getActivity();
        if (mUseSearchMode) {
            rg_sign_tab.setVisibility(View.GONE);
        } else {
            rg_sign_tab.setVisibility(View.VISIBLE);
            rg_sign_tab.setOnCheckedChangeListener(this);
        }
        list = new ArrayList<>();
        adapter = new UnSignRecipeAdapter(mContext, mUseSearchMode, BaseApplication.getInstance().getUserData().BJCA_ClientID, list, new UnSignRecipeAdapter.SignListener() {
            @Override
            public void onSignClick() {
                isShow = true;
                EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(true));
            }
        });

        mPageListViewHelper = new PageListViewHelper<>(mRecipeListView, adapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
        if (mUseSearchMode) {
            mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (list.get(position).getRecipeFileStatus() == 2) {
                        BJCASDK.getInstance().signRecipe(getActivity(), BaseApplication.getInstance().getUserData().BJCA_ClientID, list.get(position).getSignatureID());
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mUseSearchMode) {
            loadData(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(isVisibleToUser ? isShow : false));
    }

    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getSignList_Event event = new Http_getSignList_Event(
                refresh ? "1" : mPageListViewHelper.getPageIndex() + "",
                isSignList ? "1" : "2",
                new HttpListener<ArrayList<SignRecipeBean>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                        }
                        NBSAppAgent.onEvent("诊室-电子处方-获取失败");
                        EmptyViewUtils.removeAllView(mContent);
                        EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_network, "请求错误", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EmptyViewUtils.removeAllView(mContent);
                                loadData(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(ArrayList<SignRecipeBean> list) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, DebugUtils.successFormat("get elect recipe", refresh + " , " + DebugUtils.toJson(list)));
                        }
                        NBSAppAgent.onEvent("诊室-电子处方-获取成功");
                        EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));

                        if (refresh) {
                            if (list == null || list.size() == 0) {
                                mPageListViewHelper.refreshData(new ArrayList<SignRecipeBean>());
                                EmptyViewUtils.removeAllView(mContent);
                                EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_message, "暂无数据", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        EmptyViewUtils.removeAllView(mContent);
                                        loadData(true);
                                    }
                                });
                            }else{
                                EmptyViewUtils.removeAllView(mContent);
                                mPageListViewHelper.refreshData(list);
                            }
                        } else {
                            EmptyViewUtils.removeAllView(mContent);
                            mPageListViewHelper.addPageData(list);
                        }
                    }
                });
        HttpClient httpClient = NetService.createClient(mContext, HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    /**
     * 搜索数据
     */
    private void searchData(final boolean refresh, String key) {
        NBSAppAgent.onEvent("诊室-搜索电子处方:" + key);
        mPageListViewHelper.setRefreshing(refresh);
        Http_getSignList_Event event = new Http_getSignList_Event(
                refresh ? "1" : mPageListViewHelper.getPageIndex() + "",
                isSignList ? "1" : "2",
                key,
                new HttpListener<ArrayList<SignRecipeBean>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<SignRecipeBean> list) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, "request success");
                        }
                        EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));

                        if (refresh) {
                            if (list == null || list.size() == 0) {
                                mPageListViewHelper.refreshData(new ArrayList<SignRecipeBean>());
                                EmptyViewUtils.removeAllView(mContent);
                                EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_message, "暂无数据", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        EmptyViewUtils.removeAllView(mContent);
                                        loadData(true);
                                    }
                                });
                            }else{
                                EmptyViewUtils.removeAllView(mContent);
                                mPageListViewHelper.refreshData(list);
                            }
                        } else {
                            EmptyViewUtils.removeAllView(mContent);
                            mPageListViewHelper.addPageData(list);
                        }
                    }
                });
        HttpClient httpClient = NetService.createClient(mContext, HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_unsign_list:
                isSignList = false;
                loadData(true);
                break;
            case R.id.rb_sign_list:
                EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));
                isSignList = true;
                loadData(true);
                break;
        }

    }

    //是否显示签名窗口
    public static class ShowSignWindow {
        public Boolean showWindow;

        public ShowSignWindow(Boolean showWindow) {
            this.showWindow = showWindow;
        }
    }

    //签名操作
    public static class SignOperation {
        public int type;

        public SignOperation(int type) {
            this.type = type;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void signOperation(SignOperation bean) {//根据操作类型，做不同处理
        switch (bean.type) {
            case TYPE_CLOSE://取消
                isShow = false;
                adapter.setSelectType(UnSignRecipeAdapter.TYPE_SELECT_NULL);
                EventBus.getDefault().post(new ElectRecipeFragment.ShowSignWindow(false));
                break;
            case TYPE_CANCEL://批量撤回
                if (adapter.getSelectRecipe(false).equals("")) {
                    ToastUtils.showShort(getActivity(), mContext.getResources().getString(R.string.string_recipe_warning));
                } else {
                    BatchCancelRecipe(adapter.getSelectRecipe(false));
                }
                break;
            case TYPE_SIGN://批量签名
                if (adapter.getSelectRecipe(true).equals("")) {
                    ToastUtils.showShort(getActivity(), mContext.getResources().getString(R.string.string_recipe_warning));
                } else {
                    BatchSignRecipe(adapter.getSelectRecipe(true));
                }
                break;
            case UnSignRecipeAdapter.TYPE_SELECT_ALL://全选
                adapter.setSelectType(UnSignRecipeAdapter.TYPE_SELECT_ALL);
                break;
            case UnSignRecipeAdapter.TYPE_SELECT_NULL://全取消
                adapter.setSelectType(UnSignRecipeAdapter.TYPE_SELECT_NULL);
                break;
            case TYPE_REFRESH://刷新
//                onRefreshData();
                rb_sign_list.setChecked(true);
                break;
        }
    }

    /**
     * 批量签名处方
     */
    public void BatchSignRecipe(List<String> list) {
        if (existCertYWQ()) {//检查本地证书
            if (userMatchCert()) {//检查本地证书 和 当前用户是否匹配
                batchSign(list);//批量签名
            } else {
                gotoYWQ();//不匹配，去找回证书
            }
        } else {
            gotoYWQ();//本地没有证书，前往去下载证书
        }
    }

    /**
     * 1.调用医网签sdk进行批量签名
     */
    private void batchSign(List<String> list) {
        int result = BJCASDK.getInstance().signBatch(getActivity(), BaseApplication.getInstance().getUserData().BJCA_ClientID, list);
        if (result == ConstantParams.SIGN_CALL_SUCCESS) {
            //TODO 调用成功，等待onActivityResult返回结果
        } else {
            //调用失败，可以根据集成文档查看失败原因
            String str = "";
            switch (result) {
                case SIGN_NOT_CERT:
                    str = mContext.getResources().getString(R.string.string_recipe_warning1);
                    break;
                case SIGN_NOT_STAMP:
                    str = mContext.getResources().getString(R.string.string_recipe_warning2);
                    break;
                case SIGN_NULL_PARAMS:
                    str = mContext.getResources().getString(R.string.string_recipe_warning3);
                    break;
                case PERMISSION_REFUSE:
                    str = mContext.getResources().getString(R.string.string_recipe_warning4);
                    break;
                case BATCH_COUNT_ERROR:
                    str = mContext.getResources().getString(R.string.string_recipe_warning6);
                    break;
            }
            ToastUtils.showShort(mContext, "调用失败！错误返回码为:" + str);
        }
    }

    /**
     * 批量撤回签名
     */
    public void BatchCancelRecipe(List<String> list) {
        ((BaseActivity) getActivity()).showLoadDialog(R.string.submit_wait);
        Http_cancelRecipe_Event event = new Http_cancelRecipe_Event(list, new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                ((BaseActivity) getActivity()).dismissLoadDialog();
                ToastUtils.showShort(mContext, msg);
            }

            @Override
            public void onSuccess(String msg) {
                if (DebugUtils.debug) {
                }
                ((BaseActivity) getActivity()).dismissLoadDialog();
                ToastUtils.showShort(mContext, getResources().getString(R.string.string_recipe_cancel_success));
                loadData(true);
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }

    /**
     * 本地是否存在 医网签证书
     */
    public boolean existCertYWQ() {
        return BJCASDK.getInstance().existsCert(mContext);
    }

    /**
     * 检查本地证书 和 当前用户是否匹配
     */
    public boolean userMatchCert() {
        UserBean userBean = BJCASDK.getInstance().getCertUser(mContext);
        if (userBean.getUserName().equals(BaseApplication.getInstance().getUserData().mUserCNName) && userBean.isHasStamp()) {
            return true;
        }
        return false;
    }

    /**
     * 前往医网签下载证书
     */
    public void gotoYWQ() {
        AlterDialogView.Builder builder = new AlterDialogView.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.string_dialog));
        builder.setMessage(mContext.getResources().getString(R.string.string_dialog_cert));
        builder.setNegativeButton(mContext.getResources().getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(mContext.getResources().getString(R.string.string_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                BJCASDK.getInstance().startDoctor(mContext, BaseApplication.getInstance().getUserData().BJCA_ClientID);
            }
        });
        builder.create().show();
    }
}
