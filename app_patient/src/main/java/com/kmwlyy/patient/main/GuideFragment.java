package com.kmwlyy.patient.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/9/23.
 */
public class GuideFragment extends BaseFragment {

    public static GuideFragment newInstance(int res) {
        GuideFragment gf = new GuideFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("res", res);
        gf.setArguments(bundle);

        return gf;
    }

    int mRes;

    @BindView(R.id.content)
    ImageView mContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mRes = bundle.getInt("res");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guide_item, container, false);
        ButterKnife.bind(this, root);

        mContent.setImageResource(mRes);

        return root;
    }

}
