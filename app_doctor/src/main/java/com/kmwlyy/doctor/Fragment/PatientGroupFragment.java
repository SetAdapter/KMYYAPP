package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.model.PatientGroup;
import com.kmwlyy.doctor.model.httpEvent.MemberGroup;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Winson on 2017/7/4.
 */

public class PatientGroupFragment extends Fragment {

    public static final String TAG = PatientGroupFragment.class.getSimpleName();

    ListView mPatientGroupListView;
    PatientGroupAdapter mPatientGroupAdapter;
    PatientGroup mAllPatientGroup;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateMemberGroup(AppEventApi.UpdateMemberGroupList updateMemberGroupList) {
        getMemberGroupList();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_group, container, false);

        mPatientGroupListView = (ListView) root.findViewById(R.id.list_view);
        mPatientGroupAdapter = new PatientGroupAdapter(getActivity(), null);
        mPatientGroupListView.setAdapter(mPatientGroupAdapter);
        mPatientGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPatientGroupAdapter.setSelectionItem(position);

                PatientGroup patientGroup = mPatientGroupAdapter.getItem(position);
                AppEventApi.UpdateGroup updateGroup = new AppEventApi.UpdateGroup();
                updateGroup.patientGroup = patientGroup;
                EventBus.getDefault().post(updateGroup);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String allStr = getString(R.string.string_all);
        mAllPatientGroup = new PatientGroup();
        mAllPatientGroup.groupName = allStr;
        ArrayList<PatientGroup> patientGroups = new ArrayList<>();
        patientGroups.add(0, mAllPatientGroup);
        EventBus.getDefault().post(mAllPatientGroup);

        mPatientGroupAdapter.replaceData(patientGroups);

        getMemberGroupList();
    }

    static class PatientGroupAdapter extends CommonAdapter<PatientGroup> {

        private int position;

        public PatientGroupAdapter(Context context, List<PatientGroup> datas) {
            super(context, R.layout.item_patient_group_small, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, PatientGroup patientGroup, int position) {
            if (position == this.position) {
                viewHolder.getConvertView().setBackgroundResource(R.drawable.transparent);
            } else {
                viewHolder.getConvertView().setBackgroundResource(R.drawable.listview_selector_white_reverse);
            }
            ((TextView) viewHolder.findViewById(R.id.tv_name)).setText(patientGroup.groupName);
        }

        public void setSelectionItem(int position) {
            this.position = position;
            notifyDataSetChanged();
        }

    }

    private void getMemberGroupList() {

        MemberGroup.GetList getList = new MemberGroup.GetList(null, 1, 100, new HttpListener<ArrayList<PatientGroup>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getMemberGroupList", code, msg));
                }
            }

            @Override
            public void onSuccess(ArrayList<PatientGroup> patientGroups) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getMemberGroupList", DebugUtils.toJson(patientGroups)));
                }

                mPatientGroupAdapter.setSelectionItem(0);
                AppEventApi.UpdateGroup updateGroup = new AppEventApi.UpdateGroup();
                if (patientGroups == null || patientGroups.isEmpty()) {
                    ArrayList<PatientGroup> emptyPatientGroups = new ArrayList<>();
                    patientGroups = emptyPatientGroups;
                }

                patientGroups.add(0, mAllPatientGroup);
                updateGroup.patientGroup = patientGroups.get(0);
                EventBus.getDefault().post(updateGroup);

                mPatientGroupAdapter.replaceData(patientGroups);
            }
        });
        HttpClient httpClient = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getList);
        httpClient.start();

    }

}
