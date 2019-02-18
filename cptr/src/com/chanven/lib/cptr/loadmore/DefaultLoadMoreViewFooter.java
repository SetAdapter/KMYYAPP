/*
Copyright 2015 Chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.chanven.lib.cptr.loadmore;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.R;

/**
 * default load more view
 */
public class DefaultLoadMoreViewFooter implements ILoadMoreViewFactory {

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }

    private class LoadMoreHelper implements ILoadMoreView {

        protected View footerView;
        protected View mFooterparent;
        protected TextView footerTv;
//        protected ProgressBar footerBar;
        protected ImageView footerImage;

        protected OnClickListener onClickRefreshListener;
        protected AnimationDrawable anim;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {

            mFooterparent = footViewHolder.addFootView(R.layout.loadmore_default_footer);
            footerView = mFooterparent.findViewById(R.id.footview);
            footerTv = (TextView) mFooterparent.findViewById(R.id.loadmore_default_footer_tv);
//            footerBar = (ProgressBar) footerView.findViewById(R.id.loadmore_default_footer_progressbar);
            footerImage = (ImageView) mFooterparent.findViewById(R.id.loadmore_self_loading);
            anim = (AnimationDrawable) footerImage.getBackground();
            anim.setOneShot(false);//设置动画可以重复播放
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            footerTv.setText("点击加载更多");
            footerTv.setVisibility(View.GONE);
//            footerBar.setVisibility(View.GONE);
            footerImage.setVisibility(View.GONE);
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showLoading() {
            footerTv.setText("正在加载中...");
//            footerBar.setVisibility(View.VISIBLE);
//            footerTv.setVisibility(View.GONE);
            footerImage.setVisibility(View.VISIBLE);
            anim.start();
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            footerTv.setText("加载失败，点击重新");
            footerTv.setVisibility(View.VISIBLE);
//            footerBar.setVisibility(View.GONE);
            footerImage.setVisibility(View.GONE);
            anim.stop();
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {
            footerTv.setText("已经加载完毕");
            footerTv.setVisibility(View.VISIBLE);
//            footerBar.setVisibility(View.GONE);
            footerImage.setVisibility(View.GONE);
            anim.stop();
            footerView.setOnClickListener(null);
        }

        @Override
        public void setFooterVisibility(boolean isVisible) {
            footerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

}
