package com.winson.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winson.ui.R;

import org.w3c.dom.Text;

/**
 * Created by Winson on 2016/2/5.
 */
public class EmptyViewUtils {

    static void show(ViewGroup parent, View child) {
        if (parent instanceof FrameLayout
                || parent instanceof AbsoluteLayout
                || parent instanceof RelativeLayout) {
            child.bringToFront();
        }
        child.setVisibility(View.VISIBLE);
    }

    static ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static View showLoadingView(ViewGroup parent) {
        if (parent == null) {
            return null;
        }
        parent.removeView(parent.findViewById(R.id.error_view));
        parent.removeView(parent.findViewById(R.id.empty_view));
        View loadingView = parent.findViewById(R.id.loading_view);
        if (loadingView == null) {
            loadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_view, parent, false);
            loadingView.setId(R.id.loading_view);
            loadingView.setLayoutParams(getLayoutParams());
            parent.addView(loadingView, 0);
        }
        show(parent, loadingView);
        return loadingView;
    }

    public static View showErrorView(ViewGroup parent, View.OnClickListener listener) {
        return showErrorView(parent, null, 0, listener);
    }

    public static View showErrorView(ViewGroup parent, int resId, View.OnClickListener listener) {
        return showErrorView(parent, null, resId, listener);
    }

    public static View showErrorView(ViewGroup parent, String msg, int resId, View.OnClickListener listener) {
        if (parent == null) {
            return null;
        }
        parent.removeView(parent.findViewById(R.id.loading_view));
        parent.removeView(parent.findViewById(R.id.empty_view));
        View errorView = parent.findViewById(R.id.error_view);
        if (errorView == null) {
            errorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_view, parent, false);
            ((ImageView) errorView.findViewById(R.id.error_iv)).setImageResource(resId);
            errorView.setLayoutParams(getLayoutParams());
            errorView.setId(R.id.error_view);
            errorView.setOnClickListener(listener);
            parent.addView(errorView, 0);
        }
        if (msg != null) {
            ((TextView) errorView.findViewById(R.id.msg)).setText(msg);
        }
        show(parent, errorView);
        return errorView;
    }

    public static View showEmptyView(ViewGroup parent, View.OnClickListener listener) {
        return showEmptyView(parent, 0, null, listener);
    }

    public static View showEmptyView(ViewGroup parent, int resId, View.OnClickListener listener) {
        return showEmptyView(parent, resId, null, listener);
    }

    public static View showEmptyView(ViewGroup parent, String msg, View.OnClickListener listener) {
        return showEmptyView(parent, 0, null, listener);
    }

    public static View showEmptyView(ViewGroup parent, int resId, String msg, View.OnClickListener listener) {
        if (parent == null) {
            return null;
        }
        parent.removeView(parent.findViewById(R.id.loading_view));
        parent.removeView(parent.findViewById(R.id.error_view));
        View emptyView = parent.findViewById(R.id.empty_view);
        if (emptyView == null) {
            emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            ((ImageView) emptyView.findViewById(R.id.empty_iv)).setImageResource(resId);
            emptyView.setLayoutParams(getLayoutParams());
            emptyView.setId(R.id.empty_view);
            emptyView.setOnClickListener(listener);
            parent.addView(emptyView, 0);
        }
        if (msg != null) {
            ((TextView) emptyView.findViewById(R.id.msg)).setText(msg);
        }
        show(parent, emptyView);
        return emptyView;
    }

    public static View showEmptyView(ViewGroup parent, String msg, int color, View.OnClickListener listener) {
        if (parent == null) {
            return null;
        }
        parent.removeView(parent.findViewById(R.id.loading_view));
        parent.removeView(parent.findViewById(R.id.error_view));
        View emptyView = parent.findViewById(R.id.empty_view);
        if (emptyView == null) {
            emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            emptyView.setLayoutParams(getLayoutParams());
            emptyView.setId(R.id.empty_view);
            emptyView.setOnClickListener(listener);
            parent.addView(emptyView, 0);
        }
        if (msg != null) {
            ((TextView) emptyView.findViewById(R.id.msg)).setText(msg);
        }
        emptyView.setBackgroundColor(color);
        show(parent, emptyView);
        return emptyView;
    }

    public static void removeEmptyView(ViewGroup parent) {
        if (parent == null) {
            return;
        }
        parent.removeView(parent.findViewById(R.id.empty_view));
    }

    public static void removeErrorView(ViewGroup parent) {
        if (parent == null) {
            return;
        }
        parent.removeView(parent.findViewById(R.id.error_view));
    }

    public static void removeLoadingView(final ViewGroup parent, boolean animation) {
        if (parent == null) {
            return;
        }
        final View loadingView = parent.findViewById(R.id.loading_view);
        parent.removeView(loadingView);
    }

    public static void removeAllView(ViewGroup parent) {
        if (parent == null) {
            return;
        }
        parent.removeView(parent.findViewById(R.id.empty_view));
        parent.removeView(parent.findViewById(R.id.loading_view));
        parent.removeView(parent.findViewById(R.id.error_view));
    }

}
